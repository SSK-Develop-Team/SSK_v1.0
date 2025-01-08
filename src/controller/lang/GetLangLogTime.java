package controller.lang;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dto.User;

/**
 * 
 *
 */

@WebServlet("/GetLangLogTime")
public class GetLangLogTime extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetLangLogTime() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    
	    HttpSession session = request.getSession(true);
		
		

		User currUser = (User)session.getAttribute("currUser");
		int focusUserId = Integer.parseInt(request.getParameter("focusUserId"));

		
		int selectNum = Integer.parseInt(request.getParameter("selectNum"));
		request.setAttribute("selectIndex", selectNum);
		
		
		String forwardLocation = "/GetLangResultAll";
		
		if(currUser.getUserId() != focusUserId) {
			forwardLocation = forwardLocation + "?childId="+focusUserId;
		}
		
		
		RequestDispatcher rd = request.getRequestDispatcher(forwardLocation);
		rd.forward(request, response);
		

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
