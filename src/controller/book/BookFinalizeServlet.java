package controller.book;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.util.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.*;

import integration.PythonChatClient;
import model.dao.BookDAO;
import model.dao.BookPageDAO;
import model.dao.ParentGuideDAO;
import model.dto.Book;
import model.dto.BookPage;
import model.dto.ParentGuide;
import model.dto.User;
import util.process.UserInfoProcessor;

@WebServlet("/BookFinalize")
public class BookFinalizeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	response.setContentType("application/json; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession(true);

		ServletContext sc = getServletContext();
		Connection conn = (Connection) sc.getAttribute("DBconnection");

		User currUser = (User) session.getAttribute("currUser");

        String threadId = (String) session.getAttribute("chatThreadId");
        if (threadId == null || threadId.trim().isEmpty()) {
        	response.setStatus(400);
        	response.getWriter().write("{\"error\":\"thread_id missing\"}");
            return;
        }

        // FastAPI base url
        String pyBase = getServletContext().getInitParameter("PY_BASE_URL");
        if (pyBase == null || pyBase.trim().isEmpty()) pyBase = "http://127.0.0.1:8000";
		
        String childName = currUser.getUserName();
        Date userBirth = currUser.getUserBirth();
		int nowAge = UserInfoProcessor.getUserBirthToCurrAge(userBirth);
		String childGender = currUser.getUserGender();
		
        // 세션에서 수집값
        String storyLanguage = (String) session.getAttribute("story_language"); // 선택 언어
        String storyElements = (String) session.getAttribute("story_elements"); // 등장 요소
        Integer selCode = (Integer) session.getAttribute("story_theme_choice"); // 1~7번 SEL 번호
        String selLabel = (String) session.getAttribute("story_main_theme");	// 1~6번 세부 설명 or 7번 직접 입력
        
        try {
            // 1) FastAPI /finalize 호출
            PythonChatClient client = new PythonChatClient(pyBase);
            JsonObject out = client.finalizeBook(
                    threadId,
                    childName, nowAge, childGender,
                    storyLanguage, storyElements, selLabel
            );
            
            String storyTitle = out.has("story_title") && !out.get("story_title").isJsonNull() ? out.get("story_title").getAsString(): "";
        	
        	// 2) DB 저장
            Book book = new Book();
            book.setUserId(currUser.getUserId());
            book.setStoryTitle(storyTitle);
            book.setStoryLanguage(storyLanguage);
            book.setStoryElements(storyElements);
            book.setSelGoalCode(selCode);
            book.setSelGoalLabel(selLabel);

            // contextSituation/extraNotes는 아직 수집 안 하면 null로 두면 됨
            book.setContextSituation(null);
            book.setExtraNotes(null);

            long bookId = BookDAO.insertBook(conn, book);

            // pages 저장
            ArrayList<BookPage> pages = new ArrayList<>();
            JsonArray arr = out.getAsJsonArray("pages");
            for (int i = 0; i < arr.size(); i++) {
                JsonObject p = arr.get(i).getAsJsonObject();
                int pageNo = p.get("page_no").getAsInt();
                String pageText = p.get("page_text").getAsString();

                // tomcat 기준: /PSLE/generated/{threadId}/page_01.png
                String imageUrl = request.getContextPath() + "/generated/" + threadId + "/page_" + String.format("%02d", pageNo) + ".png";
                
                // String imageUrl = p.get("image_url").getAsString();
                // String fullImageUrl = pyBase + imageUrl; // 절대 URL로 저장

                BookPage bp = new BookPage();
                bp.setBookId(bookId);
                bp.setPageNo(pageNo);
                bp.setPageContent(pageText);
                bp.setPageImagePath(imageUrl);
                pages.add(bp);
            }
            BookPageDAO.insertPages(conn, pages);

            // parent guide 저장
            String guide = out.has("parent_guide") ? out.get("parent_guide").getAsString() : "";
            ParentGuide pg = new ParentGuide();
            pg.setBookId(bookId);
            pg.setGuideText(guide);
            ParentGuideDAO.upsert(conn, pg);

            // 3) redirect URL 응답
            String redirectUrl = request.getContextPath() + "/GetBookView?bookId=" + bookId + "&page=1";

            JsonObject res = new JsonObject();
            res.addProperty("redirectUrl", redirectUrl);
            response.getWriter().write(res.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"error\":\"" + escape(e.getMessage()) + "\"}");
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}