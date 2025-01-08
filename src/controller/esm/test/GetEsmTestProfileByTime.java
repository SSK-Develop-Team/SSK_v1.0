package controller.esm.test;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.Date;

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
import model.dto.EsmTestLog;
import model.dto.User;

/**
 * @author Jiwon Lee
 * 정서 반복 기록 시간별 데이터 
 */
@WebServlet("/GetEsmTestProfileByTime")
public class GetEsmTestProfileByTime extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public GetEsmTestProfileByTime() {
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
	 	
	 	
		// reply 날짜 데이터
		ArrayList<Date> esmTestDateList = (ArrayList<Date>) EsmTestLogDAO.getEsmTestLogDateListByUserIdGroupByDate(conn, focusUser.getUserId());
		if(esmTestDateList.size()==0) {
 			PrintWriter out = response.getWriter();
 			out.println("<script>alert('아직 기록이 없습니다. ');history.go(-1);</script>");
 			out.flush();
 		}else {

 		 	ArrayList<EsmTestLog> selectedDateEsmTestLogList = new ArrayList<EsmTestLog>();

 			if(request.getParameter("date")==null) {
 				selectedDateEsmTestLogList = (ArrayList<EsmTestLog>) EsmTestLogDAO.getEsmTestLogListByUserIdAndDate(conn, focusUser.getUserId(), esmTestDateList.stream().map(i->i).max(Date::compareTo).get());
 			}else{
 				String selectedDateStr = request.getParameter("date");
 				System.out.println(selectedDateStr);
 				Date selectedDate = Date.valueOf(selectedDateStr);
 	            selectedDateEsmTestLogList = (ArrayList<EsmTestLog>) EsmTestLogDAO.getEsmTestLogListByUserIdAndDate(conn, focusUser.getUserId(), selectedDate);
 			}
 			
 			int selectedIndexOfEsmTestDateList = esmTestDateList.indexOf(selectedDateEsmTestLogList.get(0).getEsmTestDate());
 			
 			// day를 기준으로 해당 데이터 가져오기
 			ArrayList<Integer> positiveList = new ArrayList<Integer>();
 			ArrayList<Integer> negativeList = new ArrayList<Integer>();
 			
 			for(int i=0;i<selectedDateEsmTestLogList.size();i++) {
 				positiveList.add(EsmReplyDAO.getEsmReplyPositiveValueByEsmTestLogId(conn, selectedDateEsmTestLogList.get(i).getEsmTestLogId()));
 				negativeList.add(EsmReplyDAO.getEsmReplyNegativeValueByEsmTestLogId(conn, selectedDateEsmTestLogList.get(i).getEsmTestLogId()));
 			}
 			
 			request.setAttribute("focusUser", focusUser);
 			request.setAttribute("esmTestDateList", esmTestDateList);//기록이 있는 일자 정보
 			request.setAttribute("selectedIndexOfEsmTestDateList", selectedIndexOfEsmTestDateList);
 			request.setAttribute("selectedEsmTestLogList", selectedDateEsmTestLogList);//선택된 일자의 기록 리스트
 			request.setAttribute("positiveList", positiveList);
 			request.setAttribute("negativeList", negativeList);

 		 	RequestDispatcher rd = request.getRequestDispatcher("/esmTestProfileByTime.jsp");
 			rd.forward(request, response);
 		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
