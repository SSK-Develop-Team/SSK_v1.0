package controller.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.UserDAO;
import model.dto.User;
import model.dao.EsmAlarmDAO;
import model.dto.EsmAlarm;
/**
 * 전문가 회원가입
 */
@WebServlet("/DoRegister")
public class DoRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public DoRegister() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");

		ServletContext sc = getServletContext();
		Connection conn= (Connection) sc.getAttribute("DBconnection");

		
	    String userloginid = request.getParameter("userId");
	    String userpw = request.getParameter("userPw");
		String username = request.getParameter("userName");
		String userEmail = request.getParameter("userEmail");
		String userRole = request.getParameter("userRole");
		String usergender = request.getParameter("userGender");
		String userbirth = request.getParameter("userBirth");

		Date birth=null;
		Date registrationDate = new Date(System.currentTimeMillis());
		
		if(userbirth!=null) {
			String year = userbirth.split("-")[0];
			String month = userbirth.split("-")[1];
			String day = userbirth.split("-")[2];
			
			//전문가는 따로 관리, 추후 삭제할 코드 주석 처리
			birth = Date.valueOf(Integer.parseInt(year)+"-"+Integer.parseInt(month)+"-"+Integer.parseInt(day));
		}
		
		User user = new User();
				user.setUserLoginId(userloginid);
				user.setUserPassword(userpw);
				user.setUserName(username);
				user.setUserEmail(userEmail);
				user.setUserRole(userRole);
				user.setRegistrationDate(registrationDate);
				user.setUserGender(usergender);
				user.setUserBirth(birth);
				user.setUserIcon("");
				
		int join_result = -1;
				
		join_result = UserDAO.insertUser(conn, user);

		if(userRole.equals("CHILD")) {
			//alarm
			String[] alarmStartParams = request.getParameterValues("alarmStart");
			String[] alarmEndParams = request.getParameterValues("alarmEnd");
			String[] alarmIntervalParams = request.getParameterValues("alarmInterval");
			
			
	        for (int i = 0; i < alarmStartParams.length; i++) {

	        	 // HH:MM 형식을 HH:MM:SS 형식으로 변환
	            String alarmStartFormatted = alarmStartParams[i].length() == 5 ? alarmStartParams[i] + ":00" : alarmStartParams[i];
	            String alarmEndFormatted = alarmEndParams[i].length() == 5 ? alarmEndParams[i] + ":00" : alarmEndParams[i];
	            
	            Time alarmstart = Time.valueOf(alarmStartFormatted);
	            Time alarmend = Time.valueOf(alarmEndFormatted);
	            int alarminterval = Integer.parseInt(alarmIntervalParams[i]);

	            
	            
	            EsmAlarm alarm = new EsmAlarm();
	            alarm.setAlarmStart(alarmstart);
	            alarm.setAlarmEnd(alarmend);
	            alarm.setAlarmInterval(alarminterval);
	            alarm.setUserId(join_result);


	            EsmAlarmDAO.insertUserAlarm(conn, alarm);
	        }		
		}
		
            
		
		if(join_result == -1) {
			PrintWriter out = response.getWriter();
			out.println("<script>alert('계정 정보를 확인해주세요.'); location.href='register.jsp';</script>");
			out.flush();
		}
		else {
			String location=userRole.equals("CHILD")?"GetManageChild":"GetAdminHome";

			PrintWriter out = response.getWriter();
			out.println("<script>alert('계정 생성 성공'); location.href='"+location+"';</script>");
			out.flush();
		}		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
