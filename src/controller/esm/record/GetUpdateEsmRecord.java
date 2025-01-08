package controller.esm.record;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
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
 * 정서 다이어리 수정 페이지로 이동
 * 수정할 정서 다이어리의 정보 전달
 */
@WebServlet("/GetUpdateEsmRecord")
public class GetUpdateEsmRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetUpdateEsmRecord() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");

		ServletContext sc = getServletContext();
		Connection conn= (Connection) sc.getAttribute("DBconnection");
		
		int esmRecordId = Integer.parseInt(request.getParameter("esmRecordId"));
		EsmRecord esmRecord = EsmRecordDAO.getEsmRecordById(conn, esmRecordId);
		
		request.setAttribute("currEsmRecord", esmRecord);
		
		RequestDispatcher rd = request.getRequestDispatcher("/updateEsmRecord.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
