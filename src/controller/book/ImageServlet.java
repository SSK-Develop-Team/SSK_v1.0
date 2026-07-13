package controller.book;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/images/*")
public class ImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String BASE_DIR = "C:/SskImageData";
    private static final int BUFFER_SIZE = 8192;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        serveFile(request, response);
    }

    private void serveFile(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
    	/* 요청된 파일 경로를 가져옴 */
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        pathInfo = URLDecoder.decode(pathInfo, "UTF-8");

        while (pathInfo.startsWith("/") || pathInfo.startsWith("\\")) {
            pathInfo = pathInfo.substring(1);
        }

        File baseDir = new File(BASE_DIR).getCanonicalFile();
        File file = new File(baseDir, pathInfo).getCanonicalFile();

        if (!file.getPath().startsWith(baseDir.getPath() + File.separator)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        if (!file.exists() || file.isDirectory()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mime = getServletContext().getMimeType(file.getName());
        if (mime == null) {
            mime = "application/octet-stream";
        }

        long fileLength = file.length();
        long start = 0;
        long end = fileLength - 1;
        boolean partial = false;

        response.setContentType(mime);
        /* 동영상 재생바 이동 */
        response.setHeader("Accept-Ranges", "bytes");

        String rangeHeader = request.getHeader("Range");

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String range = rangeHeader.substring("bytes=".length()).trim();

            int dashIndex = range.indexOf("-");
            try {
                if (dashIndex > 0) {
                    start = Long.parseLong(range.substring(0, dashIndex));

                    if (dashIndex < range.length() - 1) {
                        end = Long.parseLong(range.substring(dashIndex + 1));
                    }
                }
            } catch (NumberFormatException e) {
                response.setHeader("Content-Range", "bytes */" + fileLength);
                response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }

            if (start < 0 || start >= fileLength || end < start) {
                response.setHeader("Content-Range", "bytes */" + fileLength);
                response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }

            end = Math.min(end, fileLength - 1);
            partial = true;
        }

        long contentLength = end - start + 1;

        if (partial) {
            response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT); // 206
            response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
        }

        response.setContentLengthLong(contentLength);

        if (partial) {
            try (RandomAccessFile raf = new RandomAccessFile(file, "r");
                 OutputStream out = response.getOutputStream()) {

                raf.seek(start);

                byte[] buffer = new byte[BUFFER_SIZE];
                long remaining = contentLength;

                while (remaining > 0) {
                    int len = raf.read(buffer, 0, (int) Math.min(buffer.length, remaining));
                    if (len == -1) break;

                    out.write(buffer, 0, len);
                    remaining -= len;
                }
            }
        } else {
            try (FileInputStream in = new FileInputStream(file);
                 OutputStream out = response.getOutputStream()) {

                byte[] buffer = new byte[BUFFER_SIZE];
                int len;

                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            }
        }
    }
}