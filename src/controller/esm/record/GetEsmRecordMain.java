package controller.esm.record;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
import model.dto.User;
import util.process.EsmRecordProcessor;

/**
 * @author Jiwon Lee
 * 사용자의 모든 정서 다이어리 기록 데이터를 가져온 후, EsmRecordMain.jsp로 forward
 */
@WebServlet("/GetEsmRecordMain")
public class GetEsmRecordMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetEsmRecordMain() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    HttpSession session = request.getSession(true);
	    
	    ServletContext sc = getServletContext();
	 	Connection conn= (Connection) sc.getAttribute("DBconnection");
	 	
	 	User currUser = (User)session.getAttribute("currUser");
	 	
	 	
	 	
		/*사용자의 EsmRecord 목록 가져오기 -> session events JSON 객체로 저장*/ 
		ArrayList<Date> esmRecordDateList = (ArrayList<Date>)EsmRecordDAO.getEsmRecordDateList(conn, currUser.getUserId());
		JSONObject eventsJsonObject = EsmRecordProcessor.EsmRecordDateListToJSON(esmRecordDateList);
		request.setAttribute("eventsJsonObject",eventsJsonObject);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date(System.currentTimeMillis());

		//현재 날짜로 세팅
		request.setAttribute("currDateStr", sdf.format(now));
		/*달력에 View*/
		RequestDispatcher rd = request.getRequestDispatcher("/esmRecordMain.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
