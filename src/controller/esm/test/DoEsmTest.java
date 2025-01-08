package controller.esm.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.EsmReplyDAO;
import model.dao.EsmTestLogDAO;
import model.dto.EsmReply;
import model.dto.EsmTestLog;
import model.dto.User;

/**
 * @author Jiwon Lee
 * 
 * 정서 반복 기록 결과 넣기
 */
@WebServlet("/DoEsmTest")
public class DoEsmTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public DoEsmTest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    HttpSession session = request.getSession(true);
	    
	 	ServletContext sc = getServletContext();
	 	Connection conn= (Connection) sc.getAttribute("DBconnection");
		
	 	User currUser = (User)session.getAttribute("currUser");
	 	
	 	//정서 반복 기록 로그 남기기
	 	Date nowDate = new Date(System.currentTimeMillis());
	 	Time nowTime = Time.valueOf(LocalTime.now());
	 	
	 	EsmTestLog esmTestLog = new EsmTestLog(currUser.getUserId(), nowDate, nowTime);
	 	EsmTestLogDAO.insertEsmTestLog(conn, esmTestLog);
	 	
	 	//정서 반복 기록 결과 입력하기
	 	@SuppressWarnings("unchecked")
		HashMap<Integer, Integer> esmEmotionRecordMap = (HashMap<Integer,Integer>)session.getAttribute("esmEmotionRecordMap");
	 	
	 	ArrayList<EsmReply> esmReplyList = new ArrayList<EsmReply>();
	 	for(Integer key :esmEmotionRecordMap.keySet()) {
	 		EsmReply esmReply = new EsmReply(esmTestLog.getEsmTestLogId(), key, esmEmotionRecordMap.get(key));
	 		EsmReplyDAO.insertEsmReply(conn, esmReply);
	 		esmReplyList.add(esmReply);
	 	}
	 	
		session.removeAttribute("esmEmotionRecordMap");
		
		response.sendRedirect(getServletContext().getContextPath()+"/GetEsmResult?esmTestLogId="+esmTestLog.getEsmTestLogId());
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
