package controller.user;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.UserDAO;
import model.dto.User;

/**
 * 
 */
@WebServlet("/GoToChildHome")
public class GoToChildHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public GoToChildHome() {
        super();

    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    
	    HttpSession session = request.getSession(true);
	    
	    ServletContext sc = getServletContext();
	 	Connection conn= (Connection) sc.getAttribute("DBconnection");
	 	

	 	User selectedChild = new User();
	 	if(request.getParameter("childId")!=null) {
	 		selectedChild = UserDAO.getUserById(conn, Integer.parseInt(request.getParameter("childId")));
	 	}else {
	 		System.out.println("user not selected");
	 	}
	 	
	 	//언어 발달 평가 결과 조회 시 생기는 session 삭제 -> 언어 발달 평가 결과 조회 부분 수정 필요
	 	if(session.getAttribute("langLogIdListByUser")!=null) {
	 		session.removeAttribute("langLogIdListByUser");
	 	}
	 	
	 	
	 	request.setAttribute("selectedChild",selectedChild);
	 	
	 	RequestDispatcher rd = request.getRequestDispatcher("/toChildHome.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
