package controller.lang;

import util.process.LangGameProcessor;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Jiwon Lee
 * 게임의 이전 페이지를 로드하는 서블릿
 */
@WebServlet("/GetLangGamePrevContent")
public class GetLangGamePrevContent extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetLangGamePrevContent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    
	    HttpSession session = request.getSession(true);
		
		//현재 인덱스 받아오기
		int currLangGameIndex = (int)session.getAttribute("currLangGameIndex");
		int currLangGameId = (int)session.getAttribute("langGameID");
		
		session.setAttribute("currLangGameIndex",currLangGameIndex-1);

		String location = LangGameProcessor.getForwardLocationByLangQuestionIdAndLangGameId(currLangGameId,currLangGameIndex-1);
		response.sendRedirect(request.getContextPath() +location);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
