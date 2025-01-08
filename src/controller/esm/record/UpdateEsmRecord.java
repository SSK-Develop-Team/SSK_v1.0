package controller.esm.record;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.EsmRecordDAO;
import model.dto.EsmRecord;

/**
 * @author Jiwon Lee
 * UpdateEsmRecord
 */
@WebServlet("/UpdateEsmRecord")
public class UpdateEsmRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public UpdateEsmRecord() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    
	 	ServletContext sc = getServletContext();
	 	Connection conn= (Connection) sc.getAttribute("DBconnection");
	 	
	 	int esmRecordId = Integer.parseInt(request.getParameter("esmRecordId"));
	 	String esmRecordText = request.getParameter("esmRecordText");
	 	EsmRecord esmRecord = EsmRecordDAO.getEsmRecordById(conn, esmRecordId);
	 	
	 	String alertMsg = (EsmRecordDAO.updateEsmRecord(conn, esmRecordId, esmRecordText)?"수정이 완료되었습니다.":"수정에 실패했습니다.");
	 	
	 	PrintWriter out = response.getWriter();
		out.println("<script>alert('"+alertMsg+"'); location.href='GetDayEsmRecord?selectedDateStr="+esmRecord.getEsmRecordDate().toString()+"';</script>");
		out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
