package controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.UserDAO;
import model.dto.User;

@WebServlet("/doLogin")
public class DoLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
    public DoLogin() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession(true);
		String userid = request.getParameter("userid");
		String userpw = request.getParameter("userpw");
		
		//for DB connection
		ServletContext sc_tmp = getServletContext();
		Connection conn_tmp = (Connection)sc_tmp.getAttribute("DBconnection");
		
		try {//Connection timeout 오류 해결용 코드
			UserDAO.throwConnection(conn_tmp);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		// for DB Connection
		ServletContext sc = getServletContext();
		Connection conn= (Connection) sc.getAttribute("DBconnection");
		
		User currUser = new User();
		currUser = UserDAO.findUser(conn, userid, userpw);
		if(currUser == null) { //login failed
			System.out.println("User not found");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('회원 정보를 확인해주세요.'); location.href='login.jsp';</script>");
			out.flush();
		}
		else { //login success
			System.out.println(currUser.getUserRole());
			String redirectLocation = "";
			switch(currUser.getUserRole()) {
			case "CHILD":
				redirectLocation = "/childHome.jsp"; // 아동 페이지
				break;
			case "EXPERT":
				redirectLocation = "/GetExpertHome"; // 전문가 페이지
				break;
			case "ADMIN":
				redirectLocation = "/GetAdminHome"; // 슈퍼 전문가 페이지
				break;
			}
			session.setAttribute("currUser", currUser);
			response.sendRedirect(request.getContextPath() +redirectLocation);
		}


	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}

