package controller.lang;

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

import model.dao.LangReplyDAO;
import model.dto.User;

/**
 * 
 *
 */

@WebServlet("/GetLangLog")
public class GetLangLog extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetLangLog() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    
	    HttpSession session = request.getSession(true);
		
		ServletContext sc = getServletContext();
		Connection conn= (Connection)sc.getAttribute("DBconnection");
		

		User currUser = (User)session.getAttribute("currUser");
		
		int selectAgeGroup = Integer.parseInt(request.getParameter("ageGroupId"));
		int focusUserId = Integer.parseInt(request.getParameter("focusUserId"));
		
		
		ArrayList<Integer> tmpLogIdList = LangReplyDAO.getLangTestLogIdByAgeGroup(conn, selectAgeGroup, focusUserId);
		ArrayList<Integer> langLogIdListByUser = new ArrayList<Integer>();
		
		for(int i=0; i<tmpLogIdList.size(); i++) {
			if(! langLogIdListByUser.contains(tmpLogIdList.get(i))) {
				langLogIdListByUser.add(tmpLogIdList.get(i));
			}
		}
		
		session.setAttribute("langLogIdListByUser", langLogIdListByUser);
		
		
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
