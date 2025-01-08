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

import model.dao.UserDAO;
import model.dao.EsmAlarmDAO;

/**
 * @author Lee Ji Won
 * 사용자 삭제
 * - 전문가가 아동을 삭제하는 경우
 */
@WebServlet("/DeleteUser")
public class DeleteUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DeleteUser() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    
	    ServletContext sc = getServletContext();
	 	Connection conn= (Connection) sc.getAttribute("DBconnection");

		String[] userIdStrList = null;
		String location = null;
		if(request.getParameterValues("childId")!=null){
			userIdStrList = request.getParameterValues("childId");
			location = "/GetManageChild";
		}else if(request.getParameterValues("expertId")!=null){
			userIdStrList = request.getParameterValues("expertId");
			location = "/GetAdminHome";
		}

		for(String c : userIdStrList){
			int deleteUserId = Integer.parseInt(c);
			UserDAO.deleteUser(conn, deleteUserId);
			EsmAlarmDAO.deleteUserAlarm(conn, deleteUserId);
		}

	 	RequestDispatcher rd = request.getRequestDispatcher(location);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
