package controller.esm.test;

import java.io.IOException;
import java.io.PrintWriter;
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

import model.dao.EsmReplyDAO;
import model.dao.EsmTestLogDAO;
import model.dao.UserDAO;
import model.dto.EsmDateWeekType;
import model.dto.EsmResultWithDate;
import model.dto.User;
import util.process.EsmProcessor;

/**
 * @author Jiwon Lee
 * 정서 반복 기록 일별 데이터 
 */
@WebServlet("/GetEsmTestProfileByDay")
public class GetEsmTestProfileByDay extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetEsmTestProfileByDay() {
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
	 	
 		ArrayList<Date> esmTestDateList = (ArrayList<Date>) EsmTestLogDAO.getEsmTestLogDateListByUserIdGroupByDate(conn, focusUser.getUserId());// 기록(테스트)한 모든 날짜
 		if(esmTestDateList.size()==0) {
 			PrintWriter out = response.getWriter();
 			out.println("<script>alert('아직 기록이 없습니다. ');history.go(-1);</script>");
 			out.flush();
 		}else {
 			/**
 	 		 * selected week -> graph data
 	 		 * */
 	 		
 	 		Date sdate = null;
 	 		
 	 		/*선택한 일자의 Date 구하기*/
 	 		if(request.getParameter("date")==null) {
 	 			sdate = esmTestDateList.stream().map(i->i).max(Date::compareTo).get();//테스트를 수행한 가장 최근 일자 구하기
 			}else{
 				String sdateStr = request.getParameter("date");
 				sdate = Date.valueOf(sdateStr);
			  }
			  
			  
			/*선택한 DayOfWeek Option 받아오기*/
			int startDayOfWeek;
			if(request.getParameter("startDayOfWeek")==null){
				startDayOfWeek = 1;//SUNDAY를 시작 요일로 세팅
			}else{
				startDayOfWeek = Integer.parseInt(request.getParameter("startDayOfWeek"));
			}
 	 		
 		 	/*선택한 일자에 해당하는 주의 날짜 리스트(그래프 X축)*/
 	 		ArrayList<Date> sDateListOfWeek = (ArrayList<Date>) EsmProcessor.getDateListOfWeek(sdate, startDayOfWeek);
 	 		EsmDateWeekType selectedDateWeek = new EsmDateWeekType(sdate,sDateListOfWeek.get(0), sDateListOfWeek.get(6));
 	 		
 	 		/*해당 주의 모든 기록 - 일별 평균 응답*/
 	 		ArrayList<EsmResultWithDate> esmReplyOfDayList = EsmReplyDAO.getEsmReplyListByWeek(conn, focusUser.getUserId(), sDateListOfWeek.get(0), sDateListOfWeek.get(6));

 	 		/**
 	 		 * all week -> drop down data
 	 		 * */
 		 	//모든 테스트 로그 데이터 -> 주(week) 데이터 추출 : 중복제거 (드롭다운 표시)
 	 		ArrayList<EsmDateWeekType> dateWeekList = new ArrayList<EsmDateWeekType>();
 	 		
 	 		ArrayList<Date> tmpDateListOfWeek = new ArrayList<Date>();
 	 		for(Date date : esmTestDateList) {
 	 			if(!tmpDateListOfWeek.contains(date)) {
 	 				tmpDateListOfWeek = (ArrayList<Date>) EsmProcessor.getDateListOfWeek(date, startDayOfWeek);
 	 				Date startDate = tmpDateListOfWeek.get(0);
 	 				Date endDate = tmpDateListOfWeek.get(6);
 	 				dateWeekList.add(new EsmDateWeekType(date,startDate,endDate));
 	 			}
 	 		}


 	 		request.setAttribute("focusUser", focusUser);
 	 		request.setAttribute("esmTestDateList", esmTestDateList);//전체 기록
 	 		request.setAttribute("selectedDateWeek", selectedDateWeek);
 	 		request.setAttribute("dateWeekList", dateWeekList);//drop down에 표시할 기간 데이터
 	 		request.setAttribute("sDateListOfWeek",sDateListOfWeek);//선택한 주의 날짜 리스트(그래프 X축)
 	 		request.setAttribute("esmReplyOfDayList",esmReplyOfDayList);//선택한 주의 일별 기록
			request.setAttribute("startDayOfWeek", startDayOfWeek);//일별 그래프의 시작요일을 지정하는 변수
 	 		
 		 	RequestDispatcher rd = request.getRequestDispatcher("/esmTestProfileByDay.jsp");
 			rd.forward(request, response);
 		}
 		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
