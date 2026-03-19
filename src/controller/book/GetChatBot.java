package controller.book;

import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import model.dto.User;


/**
 * 
 * @author gyr
 * 챗봇 관련
 *
 */

@WebServlet("/GetChatBot")
public class GetChatBot extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GetChatBot() {
        super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(true);

		ServletContext sc = getServletContext();
		Connection con = (Connection)sc.getAttribute("DBconnection");
		
        // 로그인 유저 확인
        User currUser = (User) session.getAttribute("currUser");
        if (currUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 1) 새로 입력하기로 들어온 경우(파라미터 우선)
        String userName = request.getParameter("userName");
        String userBirth = request.getParameter("userBirth");   // yyyy-MM-dd
        String userGender = request.getParameter("userGender"); // "M" or "F" (폼 hidden 값)

        String finalName;
        String finalBirth;
        String finalGender; // "male" or "female"

        if (!isBlank(userName) && !isBlank(userBirth) && !isBlank(userGender)) {
            // 새로 입력하기 값 사용
            finalName = userName.trim();
            finalBirth = userBirth.trim();
            finalGender = userGender;
        } else {
            // 내 정보 불러오기: currUser 값 사용
            finalName = safe(currUser.getUserName());

            finalBirth = null;
            if (currUser.getUserBirth() != null) {
                finalBirth = new SimpleDateFormat("yyyy-MM-dd").format(currUser.getUserBirth());
            }

            finalGender = currUser.getUserGender();
        }

        // 동화책 생성 User의 정보 세션에 저장
        session.setAttribute("bookUserName", finalName);
        session.setAttribute("bookUserBirth", finalBirth);
        session.setAttribute("bookUserGender", finalGender); // male/female
        session.setAttribute("bookUserAge", calcAge(finalBirth));

        System.out.println(finalName + " / " + finalBirth + " / " + calcAge(finalBirth)+ " / " + finalGender );

        // 2) thread_id 생성/저장
        String threadId = (String) session.getAttribute("chatThreadId");
        if (threadId == null || isBlank(threadId)) {
            threadId = UUID.randomUUID().toString();
            session.setAttribute("chatThreadId", threadId);
        }

        // 첫 호출은 아직 안 했으니 booted=false로 표시
        session.setAttribute("chatBooted", false);

        // 초기 인사만 JSP에 넣기(즉시 렌더)
        String intro = "안녕하세요! " + finalName + " 아동에게 딱 맞는 동화를 만들어 드릴게요. 함께 시작해 볼까요?";
        request.setAttribute("initialAssistant", intro);
        
        RequestDispatcher rd = request.getRequestDispatcher("/bookChat.jsp");
        request.setAttribute("threadId", threadId);
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private static Integer calcAge(String birth) {
        if (birth == null || birth.length() < 10) return null;
        try {
            LocalDate b = LocalDate.parse(birth.substring(0, 10));
            return Period.between(b, LocalDate.now()).getYears();
        } catch (Exception e) {
            return null;
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
