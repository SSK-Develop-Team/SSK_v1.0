package controller.sdq;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.SdqReplyDAO;
import model.dao.SdqTestLogDAO;
import model.dto.SdqQuestion;
import model.dto.SdqReply;
import model.dto.SdqTestLog;
import model.dto.User;

/**
 * @author Jiwon Lee
 * SDQ 검사 결과 입력
 */
@WebServlet("/DoSdqTest")
public class DoSdqTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DoSdqTest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		   
		HttpSession session = request.getSession(true);
		  
		// for DB Connection
		ServletContext sc = getServletContext();
		Connection conn= (Connection) sc.getAttribute("DBconnection");
		User currUser = (User)session.getAttribute("currUser");
		
		//sdq 테스트 로그 기록
		Date nowDate = new Date(System.currentTimeMillis());
	 	Time nowTime = Time.valueOf(LocalTime.now());
		
		SdqTestLog sdqTestLog = new SdqTestLog();
		sdqTestLog.setUserId(currUser.getUserId());
		sdqTestLog.setSdqTestDate(nowDate);
		sdqTestLog.setSdqTestTime(nowTime);
		SdqTestLogDAO.insertSdqTestLog(conn, sdqTestLog);
		
		@SuppressWarnings("unchecked")
		ArrayList<SdqQuestion> sdqQuestionList = (ArrayList<SdqQuestion>)session.getAttribute("sdqQuestionList");
		
		//sdq 응답 기록
		ArrayList<SdqReply> sdqReplyList = new ArrayList<SdqReply>();
		for(int i=0;i<25;i++) {
			SdqReply sdqReplyElement = new SdqReply();
			sdqReplyElement.setSdqTestLogId(sdqTestLog.getSdqTestLogId());
			sdqReplyElement.setSdqQuestionId(sdqQuestionList.get(i).getSdqQuestionId());
			sdqReplyElement.setSdqReplyContent(Integer.parseInt(request.getParameter("sdqInput"+i)));
			SdqReplyDAO.insertSdqReply(conn, sdqReplyElement);
			sdqReplyList.add(sdqReplyElement);
		}
	    
	    response.sendRedirect(getServletContext().getContextPath()+"/GetSdqResultAll?sdqTestLogId=0");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
