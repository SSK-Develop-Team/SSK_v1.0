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
 * 정서 다이어리 삭제
 */
@WebServlet("/DeleteEsmRecord")
public class DeleteEsmRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DeleteEsmRecord() {
        super();
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");

		ServletContext sc = getServletContext();
		Connection conn= (Connection) sc.getAttribute("DBconnection");
		
		int esmRecordId = Integer.parseInt(request.getParameter("esmRecordId"));
		EsmRecord esmRecord = EsmRecordDAO.getEsmRecordById(conn, esmRecordId);
		String alertMsg = (EsmRecordDAO.deleteEsmRecord(conn, esmRecordId)?"삭제가 완료되었습니다.":"삭제에 실패했습니다.");
		
		PrintWriter out = response.getWriter();
		out.println("<script>alert('"+alertMsg+"'); location.href='GetDayEsmRecord?selectedDateStr="+esmRecord.getEsmRecordDate().toString()+"';</script>");
		out.flush();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
