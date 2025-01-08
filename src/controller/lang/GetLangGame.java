package controller.lang;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import model.dto.LangGame;

import model.dao.LangGameDAO;
import util.process.LangGameProcessor;

/**
 * 선택한 문항마다 게임(직접 평가)으로 연결
 */


@WebServlet("/GetLangGame")
public class GetLangGame extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetLangGame() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(true);

		ServletContext sc = getServletContext();
		Connection con = (Connection)sc.getAttribute("DBconnection");
		
		request.setCharacterEncoding("utf-8");

		int gameID = Integer.parseInt(request.getParameter("langGameID"));

		ArrayList<Integer> langProgList = new ArrayList<Integer>();
		langProgList.add(Integer.parseInt(request.getParameter("langTestProgress1")));
		langProgList.add(Integer.parseInt(request.getParameter("langTestProgress2")));
		langProgList.add(Integer.parseInt(request.getParameter("langTestProgress3")));
		langProgList.add(Integer.parseInt(request.getParameter("langTestProgress4")));
		langProgList.add(Integer.parseInt(request.getParameter("langTestProgress5")));
		
		session.setAttribute("langProgList", langProgList);
		session.setAttribute("langGameID", gameID);
		
		ArrayList<LangGame> currLangGameList = LangGameDAO.getLangGameListByLangQuestionId(con, gameID);
		session.setAttribute("currLangGameList", currLangGameList);
		session.setAttribute("currLangGameIndex", 0);
		
		String location = LangGameProcessor.getForwardLocationByLangQuestionIdAndLangGameId(gameID, 0);

		RequestDispatcher rd = request.getRequestDispatcher(location);
		rd.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
