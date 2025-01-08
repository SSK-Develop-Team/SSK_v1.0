package controller.esm.record;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import model.dao.EsmRecordDAO;
import model.dao.UserDAO;
import model.dto.EsmRecord;
import model.dto.User;
import util.process.EsmRecordProcessor;

/**
 * @author Lee Ji Won
 * 사용자가 날짜 클릭 시 해당 날짜의 ESM Record 목록 전달
 */
@WebServlet("/GetDayEsmRecord")
public class GetDayEsmRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public GetDayEsmRecord() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    HttpSession session = request.getSession(true);
	    
	 	ServletContext sc = getServletContext();
	 	Connection conn= (Connection) sc.getAttribute("DBconnection");
	 	
	 	User focusUser = new User();
	 	
	 	if(request.getParameter("childId")==null) {//아동이 로그인한 상태 : 자신의 아이디를 parameter로 보내지 않았을 때
	 		focusUser = (User)session.getAttribute("currUser");
		}else{
			int childId = Integer.parseInt(request.getParameter("childId"));
		 	focusUser  = UserDAO.getUserById(conn, childId);
		}
	 	
	 	String selectedDateStr = null;
	 	Date selectedDateValue = null;
	 	if(request.getParameter("selectedDateStr")==null) {
	 		selectedDateValue = new Date(System.currentTimeMillis());
	 		selectedDateStr = selectedDateValue.toString();
	 	}else {
	 		selectedDateStr = request.getParameter("selectedDateStr");
	 		selectedDateValue = Date.valueOf(selectedDateStr);
	 	}
	 	
	 	
	 	
	 	/*사용자의 EsmRecord 목록 가져오기 -> session events JSON 객체로 저장*/ 
		ArrayList<Date> esmRecordDateList = (ArrayList<Date>)EsmRecordDAO.getEsmRecordDateList(conn, focusUser.getUserId());
		JSONObject eventsJsonObject = EsmRecordProcessor.EsmRecordDateListToJSON(esmRecordDateList);
		request.setAttribute("eventsJsonObject",eventsJsonObject);
	 	
	 	//해당 날짜의 기록 조회
	 	ArrayList<EsmRecord> currEsmRecordList = (ArrayList<EsmRecord>)EsmRecordDAO.getEsmRecordListByDate(conn, focusUser.getUserId(), selectedDateValue);
		
	 	request.setAttribute("focusUser",focusUser);
	 	request.setAttribute("currEsmRecordList", currEsmRecordList);
	 	request.setAttribute("currDateStr", selectedDateStr);
	 	
	 	RequestDispatcher rd = request.getRequestDispatcher("/viewEsmRecord.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}