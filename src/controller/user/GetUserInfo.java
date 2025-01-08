package controller.user;

import model.dao.UserDAO;
import model.dto.User;
import model.sevice.UserListService;
import model.dao.EsmAlarmDAO;
import model.dto.EsmAlarm;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Time;
import java.util.ArrayList;
/**
 * @author Jiwon Lee
 * 계정 수정 페이지 가져오기
 * - 관리자, 전문가가 아동 계정을 수정하는 경우
 * - 관리자가 전문가 계정을 수정하는 경우
 */
@WebServlet(name = "GetUserInfo", value = "/GetUserInfo")
public class GetUserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        ServletContext sc = getServletContext();
        Connection conn= (Connection) sc.getAttribute("DBconnection");

        int childId = 0;
        childId = Integer.parseInt(request.getParameter("childId"));
        /*
        System.out.println(location);
        if(request.getParameter("latestChildId")!=null){
            userId = Integer.parseInt(request.getParameter("latestChildId"));
            location="/childInformation.jsp?role=child";
        }else if(request.getParameter("latestExpertId")!=null){
            userId = Integer.parseInt(request.getParameter("latestExpertId"));
            location="/childInformation.jsp?role=expert";
        }
        */

        User child = UserDAO.getUserById(conn,childId);
        request.setAttribute("child",child);
       
	        	//사용자의 ESM 알람 정보 불러오기
	    ArrayList<EsmAlarm> esmTime = EsmAlarmDAO.getEsmAlarmListByUser(conn, childId);
	        	
	        	//사용자의 ESM 알람 정보를 request attribute로 넘겨주기 
	    request.setAttribute("esmTime", esmTime);


		RequestDispatcher rd = request.getRequestDispatcher("/childInformation.jsp");
		rd.forward(request, response);
		
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
