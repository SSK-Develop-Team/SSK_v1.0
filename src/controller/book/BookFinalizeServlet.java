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
import model.dao.LangResultAnalysisDAO;
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
		int ageInMonths = UserInfoProcessor.getUserBirthToCurrAge(userBirth);
		int nowAge = ageInMonths / 12;
		String childGender = currUser.getUserGender();
		
		// 언어점수 결과 연동
		int storyTargetAge = nowAge;
		int userId = currUser.getUserId();
		// 1. 최신 검사 결과 가져오기
		int latestLangTestLogId = LangResultAnalysisDAO.findLatestLangTestLogId(conn, userId);
		if (latestLangTestLogId != -1) {
		    int totalLangScore = LangResultAnalysisDAO.findTotalLangScoreByLogId(conn, latestLangTestLogId);

		    storyTargetAge = getStoryTargetAge(nowAge, totalLangScore);

		    System.out.println("[PSLE] TotalLangScore(언어점수 합산 결과) = " + totalLangScore);
		    System.out.println("[PSLE] nowAge(만 나이) = " + nowAge);
		    System.out.println("[PSLE] storyTargetAge(언어점수 기반 연령대 조정) = " + storyTargetAge);
		} else {
		    System.out.println("[PSLE] 검사 기록 없음 → 기본 나이 사용");
		}
		
        // 세션에서 수집값
        String storyLanguage = (String) session.getAttribute("story_language"); // 선택 언어
        String storyElements = (String) session.getAttribute("story_elements"); // 등장 요소
        Integer selCode = (Integer) session.getAttribute("story_theme_choice"); // 1~7번 SEL 번호
        String selLabel = (String) session.getAttribute("story_main_theme");	// 1~6번 세부 설명 or 7번 직접 입력
        
        Integer depthCode = (Integer) session.getAttribute("input_depth_choice"); // 간단/심화 모드 번호
        
        String simpleInput = (String) session.getAttribute("simple_input"); // 간단 모드 -> 고민 입력값
        
        Integer hardSituationCode = (Integer) session.getAttribute("hard_situation_choice"); // 심화 모드 <Step1> 1~5번 상황 시작 번호
        String hardSituation = (String) session.getAttribute("hard_situation"); // 심화 모드 <Step1> 1~4번 세부 설명 or 5번 직접 입력
        String hardSituationDetail = (String) session.getAttribute("hard_situation_detail"); // 심화 모드 <Step2> 세부 사항 입력
        Integer hardEmotionCode = (Integer) session.getAttribute("hard_emotion_choice"); // 심화 모드 <Step3> 1~8번 감정 번호
        String hardEmotion = (String) session.getAttribute("hard_emotion"); // 심화 모드 <Step3> 1~7번 세부 설명 or 8번 직접 입력
        Integer hardDesireCode = (Integer) session.getAttribute("hard_desire_choice"); // 심화 모드 <Step4> 1~6번 바람 번호
        String hardDesire = (String) session.getAttribute("hard_desire"); // 심화 모드 <Step4> 1~5번 세부 설명 or 6번 직접 입력
        String hardAdditional = (String) session.getAttribute("hard_additional"); // 심화 모드 <Step 5> 추가 입력
        
        String hardSituationSummary = (String) session.getAttribute("hard_situation_summary"); // 심화 모드에서 입력받은 상황 맥락 요약
        
        // 수집값 확인
        System.out.println("=== SESSION DEBUG ===");

        System.out.println("depthCode = " + depthCode);
        System.out.println("simpleInput = " + simpleInput);

        System.out.println("hardSituation = " + session.getAttribute("hard_situation"));
        System.out.println("hardSituationDetail = " + session.getAttribute("hard_situation_detail"));
        System.out.println("hardAdditional = " + session.getAttribute("hard_additional"));
        System.out.println("hardSituationSummary = " + session.getAttribute("hard_situation_summary"));
        System.out.println("hardEmotion = " + session.getAttribute("hard_emotion"));
        System.out.println("hardDesire = " + session.getAttribute("hard_desire"));
        
        // null 방어용
        if (simpleInput == null) simpleInput = "";

        if (hardSituation == null) hardSituation = "";
        if (hardSituationDetail == null) hardSituationDetail = "";
        if (hardAdditional == null) hardAdditional = "";
        if (hardSituationSummary == null) hardSituationSummary = "";
        if (hardEmotion == null) hardEmotion = "";
        if (hardDesire == null) hardDesire = "";
       
        // 저장용 변수 생성
        String contextSituation = "";
        String situationSummary = "";
        String emotion = "";
        String desire = "";
        String extraNotes = null;

        if (depthCode != null && depthCode == 1) {
            // 1) 간단 모드
            contextSituation = simpleInput;
            situationSummary = simpleInput;
            emotion = "";
            desire = "";
            extraNotes = "";
        } else {
            // 2) 심화 모드
            contextSituation =
                    "상황 맥락: " + hardSituation + ", " +
                    "구체적 장면: " + hardSituationDetail + ", " +
                    "추가 정보: " + hardAdditional;

            situationSummary = hardSituationSummary;
            emotion = hardEmotion;
            desire = hardDesire;
            extraNotes = hardAdditional;
        }
        
        
        try {
            // 1) FastAPI /finalize 호출
            PythonChatClient client = new PythonChatClient(pyBase);
            JsonObject out = client.finalizeBook(
                    threadId,
                    childName, storyTargetAge, childGender,
                    storyLanguage, storyElements, selLabel
            );
            
            System.out.println("=== FINALIZE OUT ===");
            System.out.println(out.toString());
            
            String outContextSituation = out.has("context_situation") && !out.get("context_situation").isJsonNull()
                    ? out.get("context_situation").getAsString() : "";
            String outSituationSummary = out.has("situation_summary") && !out.get("situation_summary").isJsonNull()
                    ? out.get("situation_summary").getAsString() : "";
            String outEmotion = out.has("emotion") && !out.get("emotion").isJsonNull()
                    ? out.get("emotion").getAsString() : "";
            String outDesire = out.has("desire") && !out.get("desire").isJsonNull()
                    ? out.get("desire").getAsString() : "";
            String outExtraNotes = out.has("extra_notes") && !out.get("extra_notes").isJsonNull()
                    ? out.get("extra_notes").getAsString() : "";
                    
            if (!outSituationSummary.isEmpty()) situationSummary = outSituationSummary;
            if (!outEmotion.isEmpty()) emotion = outEmotion;
            if (!outDesire.isEmpty()) desire = outDesire;
            
            String storyTitle = out.has("story_title") && !out.get("story_title").isJsonNull() ? out.get("story_title").getAsString(): "";
        	
            System.out.println("=== DB SAVE DEBUG ===");
            System.out.println("contextSituation = " + contextSituation);
            System.out.println("situationSummary = " + situationSummary);
            System.out.println("emotion = " + emotion);
            System.out.println("desire = " + desire);
            System.out.println("extraNotes = " + extraNotes);
            
        	// 2) DB 저장
            Book book = new Book();
            book.setUserId(currUser.getUserId());
            book.setStoryTitle(storyTitle);
            book.setStoryLanguage(storyLanguage);
            book.setStoryElements(storyElements);
            book.setSelGoalCode(selCode);
            book.setSelGoalLabel(selLabel);
            book.setContextSituation(contextSituation);
            book.setSituationSummary(situationSummary);
            book.setEmotion(emotion);
            book.setDesire(desire);
            book.setExtraNotes(extraNotes);


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
    
    // 언어점수에 따른 아동 연령 조정 함수
	private static int getStoryTargetAge(int nowAge, int totalLangScore) {
	    if (totalLangScore > 10) {
	        return nowAge;
	    }

	    // 10점 이하인 경우: 이전 발달 단계 수준으로 조정
	    if (nowAge >= 5 && nowAge <= 6) {
	        return 4; // 만 3~4세 수준
	    } else if (nowAge >= 3 && nowAge <= 4) {
	        return 2; // 만 2세 수준
	    } else {
	        return 2; // 만 2세는 그대로
	    }
	}
}