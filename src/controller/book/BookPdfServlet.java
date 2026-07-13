package controller.book;

import model.dao.BookDAO;
import model.dao.BookPageDAO;
import model.dto.Book;
import model.dto.BookPage;
import model.dto.User;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.*;

@WebServlet("/BookPdf")
public class BookPdfServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String IMAGE_BASE_DIR = "C:/SskImageData";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(true);
        User currUser = (User) session.getAttribute("currUser");
        if (currUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        long bookId = parseLong(request.getParameter("bookId"), -1L);
        if (bookId <= 0) {
            response.sendError(400, "bookId required");
            return;
        }
        
        

        ServletContext sc = getServletContext();
        Connection conn = (Connection) sc.getAttribute("DBconnection");

        try {
            Book book = BookDAO.getBookById(conn, bookId);
            if (book == null) {
                response.sendError(404, "book not found");
                return;
            }
            if (book.getUserId() != currUser.getUserId()) {
                response.sendError(403, "forbidden");
                return;
            }

            ArrayList<BookPage> pages = BookPageDAO.getPagesByBookId(conn, bookId);
            pages.sort(Comparator.comparingInt(BookPage::getPageNo));

            // PDF 응답 헤더
            String safeTitle = (book.getStoryTitle() == null || book.getStoryTitle().trim().isEmpty())
                    ? "storybook"
                    : book.getStoryTitle().trim();
            safeTitle = safeTitle.replaceAll("[\\\\/:*?\"<>|]", "_"); // 파일명 안전 처리

            String filename = safeTitle + ".pdf";

	         // RFC5987 인코딩
            String encoded = java.net.URLEncoder.encode(filename, java.nio.charset.StandardCharsets.UTF_8)
	                 .replaceAll("\\+", "%20");
	
	         response.setContentType("application/pdf");
	         response.setHeader("Content-Disposition",
	                 "attachment; filename=\"storybook.pdf\"; filename*=UTF-8''" + encoded);

            // PDF 생성
            try (PDDocument doc = new PDDocument()) {

                // 한글 폰트 로드(필수)
                PDType0Font font;
                try (InputStream fontIs = sc.getResourceAsStream("/WEB-INF/fonts/NotoSansKR-Regular.ttf")) {
                    if (fontIs == null) throw new RuntimeException("폰트 파일이 없습니다: /WEB-INF/fonts/NotoSansKR-Regular.ttf");
                    font = PDType0Font.load(doc, fontIs, true);
                }

                for (BookPage p : pages) {
                    addOnePage(request, doc, font, p);
                }

                // 브라우저로 바로 스트림
                try (OutputStream os = response.getOutputStream()) {
                    doc.save(os);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "pdf generation failed: " + e.getMessage());
        }
    }

    private void addOnePage(HttpServletRequest request, PDDocument doc, PDType0Font font, BookPage page) throws IOException {
        PDPage pdfPage = new PDPage(PDRectangle.A4);
        doc.addPage(pdfPage);

        PDRectangle mediaBox = pdfPage.getMediaBox();
        float pageW = mediaBox.getWidth();
        float pageH = mediaBox.getHeight();

        float margin = 40f;
        float gap = 14f;

        float imgAreaH = pageH * 0.60f;
        float textAreaH = pageH - margin * 2 - imgAreaH - gap;

        float imgX = margin;
        float imgY = pageH - margin - imgAreaH;
        float imgW = pageW - margin * 2;

        float textX = margin;
        float textY = margin;
        float textW = pageW - margin * 2;

        try (PDPageContentStream cs = new PDPageContentStream(doc, pdfPage)) {
            String imageUrl = page.getPageImagePath();
            if (imageUrl != null && !imageUrl.trim().isEmpty()) {
                PDImageXObject pdImage = loadImageSmart(request, doc, imageUrl.trim());
                if (pdImage != null) {
                    float iw = pdImage.getWidth();
                    float ih = pdImage.getHeight();
                    float scale = Math.min(imgW / iw, imgAreaH / ih);
                    float drawW = iw * scale;
                    float drawH = ih * scale;

                    float dx = imgX + (imgW - drawW) / 2f;
                    float dy = imgY + (imgAreaH - drawH) / 2f;

                    cs.drawImage(pdImage, dx, dy, drawW, drawH);
                } else {
                    System.out.println("[PDF] image NULL: " + imageUrl);
                }
            }

            String text = page.getPageContent();
            if (text == null) text = "";

            float fontSize = 14f;
            float leading = 1.45f * fontSize;

            List<String> lines = wrapTextKoreanFriendly(font, fontSize, text, textW);

            cs.beginText();
            cs.setFont(font, fontSize);

            float startY = textY + textAreaH - fontSize;
            cs.newLineAtOffset(textX, startY);

            int maxLines = (int) Math.floor((textAreaH) / leading);
            int count = 0;

            for (String line : lines) {
                if (count >= maxLines) break;
                cs.showText(line);
                cs.newLineAtOffset(0, -leading);
                count++;
            }
            cs.endText();
        }
    }

    private PDImageXObject loadImageFromUrl(PDDocument doc, String urlStr) {
    	HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(60000);
            conn.setRequestMethod("GET");

            int code = conn.getResponseCode();
            if (code < 200 || code >= 300) return null;

            String contentType = conn.getContentType();
            try (InputStream is = conn.getInputStream()) {

                // JPG면 JPEGFactory가 빠르고 용량도 좋음
                if (contentType != null && contentType.toLowerCase().contains("jpeg")) {
                    return JPEGFactory.createFromStream(doc, is);
                }

                // 그 외(PNG 등)는 ImageIO로 읽어서 LosslessFactory
                BufferedImage bi = ImageIO.read(is);
                if (bi == null) return null;
                return LosslessFactory.createFromImage(doc, bi);
            }
        } catch (Exception e) {
            System.out.println("image load failed: " + urlStr + " / " + e.getMessage());
            return null;
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
    
    
    private PDImageXObject loadImageSmart(HttpServletRequest req, PDDocument doc, String pathOrUrl) {
        // 1) 로컬 파일로 먼저 시도 (가장 안정적)
        try {
            File f = resolveToLocalFile(req, pathOrUrl);
            if (f != null && f.exists() && f.isFile()) {
                System.out.println("[PDF] load local file: " + f.getAbsolutePath());
                return createImageFromFile(doc, f);
            }
        } catch (Exception e) {
            System.out.println("[PDF] local load failed: " + pathOrUrl + " / " + e);
        }

        // 2) 로컬이 안 되면 HTTP로 fallback
        try {
            String absUrl = toAbsoluteUrl(req, pathOrUrl);
            System.out.println("[PDF] load http url: " + absUrl);
            return loadImageFromUrl(doc, absUrl);
        } catch (Exception e) {
            System.out.println("[PDF] http load failed: " + pathOrUrl + " / " + e);
            return null;
        }
    }

    private File resolveToLocalFile(HttpServletRequest req, String pathOrUrl) throws IOException {
        String p = pathOrUrl;

        if (p.startsWith("http://") || p.startsWith("https://")) return null;

        String ctx = req.getContextPath();
        if (p.startsWith(ctx + "/")) {
            p = p.substring(ctx.length());
        }

        if (p.startsWith("/images/")) {
            p = p.substring("/images".length());
        }

        if (!p.startsWith("/")) p = "/" + p;

        if (p.startsWith("/generated/")) {
            File baseDir = new File(IMAGE_BASE_DIR).getCanonicalFile();
            File file = new File(baseDir, p.substring(1)).getCanonicalFile();
            if (!file.getPath().startsWith(baseDir.getPath() + File.separator)) return null;
            return file;
        }

        String real = getServletContext().getRealPath(p);
        if (real == null) return null;
        return new File(real);
    }

    private PDImageXObject createImageFromFile(PDDocument doc, File f) throws IOException {
        String name = f.getName().toLowerCase();
        try (InputStream is = new FileInputStream(f)) {
            if (name.endsWith(".jpg") || name.endsWith(".jpeg")) {
                return JPEGFactory.createFromStream(doc, is);
            }
            BufferedImage bi = ImageIO.read(is);
            if (bi == null) return null;
            return LosslessFactory.createFromImage(doc, bi);
        }
    }

    private static String toAbsoluteUrl(HttpServletRequest req, String path) {
        if (path.startsWith("http://") || path.startsWith("https://")) return path;
        String scheme = req.getScheme();
        String host = req.getServerName();
        int port = req.getServerPort();
        boolean defaultPort = ("http".equals(scheme) && port == 80) || ("https".equals(scheme) && port == 443);
        String base = defaultPort ? (scheme + "://" + host) : (scheme + "://" + host + ":" + port);
        if (path.startsWith("/")) return base + path;
        return base + "/" + path;
    }

    // 한글/띄어쓰기 적어도 줄바꿈 가능한 래핑
    private static List<String> wrapTextKoreanFriendly(PDType0Font font, float fontSize, String text, float maxWidth) throws IOException {
        List<String> out = new ArrayList<>();
        if (text == null) return out;

        // 줄 단위(원문 개행 유지)
        String[] paras = text.replace("\r\n", "\n").split("\n");
        for (String para : paras) {
            if (para.trim().isEmpty()) {
                out.add("");
                continue;
            }

            StringBuilder line = new StringBuilder();
            for (int i = 0; i < para.length(); i++) {
                char ch = para.charAt(i);
                line.append(ch);

                float w = font.getStringWidth(line.toString()) / 1000f * fontSize;
                if (w > maxWidth) {
                    // 마지막 글자 빼고 줄 확정
                    String full = line.toString();
                    String cut = full.substring(0, full.length() - 1);
                    if (!cut.isEmpty()) out.add(cut);

                    // 새 줄 시작(현재 글자부터)
                    line = new StringBuilder();
                    line.append(ch);
                }
            }
            if (line.length() > 0) out.add(line.toString());
        }
        return out;
    }

    private static long parseLong(String s, long fallback) {
        try { return Long.parseLong(s); } catch (Exception e) { return fallback; }
    }
}
