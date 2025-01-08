package controller.esm.record;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import model.dao.EsmRecordDAO;
import model.dto.User;

/**
 * @author Lee Ji Won
 * 사용자가 ESM Record 작성
 */

@WebServlet("/CreateEsmRecord")
public class CreateEsmRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    public CreateEsmRecord() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*기록 작성*/
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    HttpSession session = request.getSession(true);
	    
	 	ServletContext sc = getServletContext();
	 	Connection conn= (Connection) sc.getAttribute("DBconnection");
	 	
	 	User currUser = (User)session.getAttribute("currUser");

	 	String esmRecordText = request.getParameter("newRecordText");
	 	Date esmRecordDate = Date.valueOf(request.getParameter("newRecordDateStr"));
	 	Date nowDate = new Date(System.currentTimeMillis());
	 	Time esmRecordTime = null;
	 	
	 	if(esmRecordDate.toString().equals(nowDate.toString())) esmRecordTime = Time.valueOf(LocalTime.now());
	 	boolean insertEsmRecord = EsmRecordDAO.insertEsmRecord(conn, esmRecordText, esmRecordDate, esmRecordTime, currUser.getUserId());
	 	
	 	if(insertEsmRecord==true)System.out.println("EsmRecord 생성");
	 	else System.out.println("EsmRecord 생성 실패");
	 	
	 	response.sendRedirect(getServletContext().getContextPath()+"/GetEsmRecordMain");
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
