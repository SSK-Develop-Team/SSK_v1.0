package controller.sdq;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.SdqQuestionDAO;
import model.dto.SdqQuestion;

/**
 * @author Jiwon Lee
 * 정서 행동 발달 검사 문항 가져오기(부모용/아동용 선택)
 */
@WebServlet("/GetSdqTest")
public class GetSdqTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetSdqTest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	       
	    HttpSession session = request.getSession(true);
	      
	    // for DB Connection
	    ServletContext sc = getServletContext();
	    Connection conn= (Connection) sc.getAttribute("DBconnection");
	    
	    String sdqTarget = request.getParameter("target");
	    
	    ArrayList<SdqQuestion> sdqQuestionList = SdqQuestionDAO.getSdqQuestionList(conn, sdqTarget);
	    
	    session.setAttribute("sdqQuestionList", sdqQuestionList);
	    
	    RequestDispatcher rd = request.getRequestDispatcher("/sdqTest.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
