package controller.lang;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dto.LangGame;
import util.process.LangGameProcessor;

/**
 * @author Jiwon Lee
 * 게임의 다음 페이지를 로드하는 서블릿
 * 마지막 페이지일 때, 언어 받달 평가 결과를 입력하는 서블릿으로 전환함
 */
@WebServlet("/GetLangGameNextContent")
public class GetLangGameNextContent extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetLangGameNextContent() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    
	    HttpSession session = request.getSession(true);
		
		//현재 인덱스 받아오기
		int currLangGameIndex = (int)session.getAttribute("currLangGameIndex");
		@SuppressWarnings("unchecked")
		ArrayList<LangGame> currLangGameList = (ArrayList<LangGame>)session.getAttribute("currLangGameList");
		int currLangGameId = (int)session.getAttribute("langGameID");
		
		//마지막 페이지가 아닐때
		if(currLangGameIndex<currLangGameList.size()-1) {
			session.setAttribute("currLangGameIndex",currLangGameIndex+1);
			System.out.println(currLangGameId+"  "+currLangGameIndex);
			String location = LangGameProcessor.getForwardLocationByLangQuestionIdAndLangGameId(currLangGameId,currLangGameIndex+1);
			response.sendRedirect(request.getContextPath() +location);
		}
		else{//마지막 페이지일 때
			response.sendRedirect(request.getContextPath() +"/langTest.jsp");
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
