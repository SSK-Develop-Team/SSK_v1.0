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
		//int isAlarmActive = Integer.parseInt(request.getParameter("isAlarmActive"));
		int isAlarmActive = request.getParameter("isAlarmActive") != null ? 1 : 0;
		
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
				user.setIsAlarmActive(isAlarmActive);
				
		int join_result = -1;
				
		join_result = UserDAO.insertUser(conn, user);

		if(userRole.equals("CHILD") && isAlarmActive == 1) {
			//alarm
			String[] alarmStartTimeParams = request.getParameterValues("alarmStartTime");
			String[] alarmEndTimeParams = request.getParameterValues("alarmEndTime");
			String[] alarmIntervalParams = request.getParameterValues("alarmInterval");
			String[] alarmStartDateParams = request.getParameterValues("alarmStartDate");
			String[] alarmEndDateParams = request.getParameterValues("alarmEndDate");
			
		    if (alarmStartTimeParams != null && alarmEndTimeParams != null &&
		            alarmIntervalParams != null && alarmStartDateParams != null &&
		            alarmEndDateParams != null) {

		            for (int i = 0; i < alarmStartTimeParams.length; i++) {

		                if (alarmStartTimeParams[i] == null || alarmStartTimeParams[i].trim().isEmpty() ||
		                    alarmEndTimeParams[i] == null || alarmEndTimeParams[i].trim().isEmpty() ||
		                    alarmIntervalParams[i] == null || alarmIntervalParams[i].trim().isEmpty() ||
		                    alarmStartDateParams[i] == null || alarmStartDateParams[i].trim().isEmpty() ||
		                    alarmEndDateParams[i] == null || alarmEndDateParams[i].trim().isEmpty()) {
		                    continue;
		                }

		                String alarmStartTimeFormatted = alarmStartTimeParams[i].length() == 5
		                        ? alarmStartTimeParams[i] + ":00"
		                        : alarmStartTimeParams[i];

		                String alarmEndTimeFormatted = alarmEndTimeParams[i].length() == 5
		                        ? alarmEndTimeParams[i] + ":00"
		                        : alarmEndTimeParams[i];

		                Time alarmstarttime = Time.valueOf(alarmStartTimeFormatted);
		                Time alarmendtime = Time.valueOf(alarmEndTimeFormatted);
		                int alarminterval = Integer.parseInt(alarmIntervalParams[i]);
		                Date alarmstartdate = Date.valueOf(alarmStartDateParams[i]);
		                Date alarmenddate = Date.valueOf(alarmEndDateParams[i]);

		                EsmAlarm alarm = new EsmAlarm();
		                alarm.setAlarmStartTime(alarmstarttime);
		                alarm.setAlarmEndTime(alarmendtime);
		                alarm.setAlarmInterval(alarminterval);
		                alarm.setAlarmStartDate(alarmstartdate);
		                alarm.setAlarmEndDate(alarmenddate);
		                alarm.setUserId(join_result);

		                EsmAlarmDAO.insertUserAlarm(conn, alarm);
		            }
		        }
		    }
		
        
		/*삭제*/
		System.out.println("userBirth = " + userbirth);
		System.out.println("isAlarmActive = " + isAlarmActive);

		String[] alarmIntervalParams = request.getParameterValues("alarmInterval");
		if (alarmIntervalParams == null) {
		    System.out.println("alarmIntervalParams = null");
		} else {
		    for (int i = 0; i < alarmIntervalParams.length; i++) {
		        System.out.println("alarmIntervalParams[" + i + "] = " + alarmIntervalParams[i]);
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
