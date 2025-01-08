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

import model.dao.LangQuestionDAO;
import model.dto.AgeGroup;
import model.dto.LangQuestion;

/**
 * 해당 아동의 연령에 맞는 질문 가져오기
 */


@WebServlet("/GetLangTest")
public class GetLangTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetLangTest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(true);

		ServletContext sc = getServletContext();
		Connection con = (Connection)sc.getAttribute("DBconnection");

		session.removeAttribute("langProgList");//이전에 저장된 응답 세션 삭제 

		System.out.println("ageGroup"+request.getParameter("ageGroup"));

		AgeGroup currAgeGroup = (AgeGroup) session.getAttribute("currAgeGroup");
		int selectAge = currAgeGroup.getAgeGroupId();
		
		if(selectAge == 14){//13단계에 포함되지 않는 아동인 경우
			selectAge = 13;
		}
		
		if(request.getParameter("ageGroup")!=null){//다른 연령을 선택한 경우
			selectAge = Integer.parseInt(request.getParameter("ageGroup"));
		}
		
		ArrayList<LangQuestion> currQuestionList = LangQuestionDAO.getLangQuestionListByAgeGroupId(con, selectAge);
		
		session.setAttribute("currQuestionList", currQuestionList);
		
		RequestDispatcher rd = request.getRequestDispatcher("/langTest.jsp");
		rd.forward(request, response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}