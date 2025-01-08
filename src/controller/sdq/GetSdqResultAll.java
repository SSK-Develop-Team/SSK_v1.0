package controller.sdq;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.SdqReplyDAO;
import model.dao.SdqResultAnalysisDAO;
import model.dao.SdqTestLogDAO;
import model.dao.UserDAO;
import model.dto.SdqResultAnalysis;
import model.dto.SdqResultOfType;
import model.dto.SdqTestLog;
import model.dto.User;

/**
 * @author Jiwon Lee
 * Sdq 모든 검사 결과 가져오기
 * Sdq 드롭다운에서 선택한 검사 결과 가져오기(default : 가장 최근 결과를 뷰로 전달)
 */
@WebServlet("/GetSdqResultAll")
public class GetSdqResultAll extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetSdqResultAll() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		   
		HttpSession session = request.getSession(true);
		  
		// for DB Connection
		ServletContext sc = getServletContext();
		Connection conn= (Connection) sc.getAttribute("DBconnection");
		
		User focusUser = new User();
	 	
	 	if(request.getParameter("childId")==null) {//아동이 로그인한 상태 : 자신의 아이디를 parameter로 보내지 않았을 때
	 		focusUser = (User)session.getAttribute("currUser");
		}else{
			int childId = Integer.parseInt(request.getParameter("childId"));
		 	focusUser  = UserDAO.getUserById(conn, childId);
		}
		
		//모든 SdqTestLog
		ArrayList<SdqTestLog> sdqTestLogList = SdqTestLogDAO.getSdqTestLogAllByUserId(conn, focusUser.getUserId());
		if(sdqTestLogList.size()==0) {
 			PrintWriter out = response.getWriter();
 			out.println("<script>alert('검사 기록이 없습니다.');history.go(-1);</script>");
 			out.flush();
 		}else {
 			SdqTestLog selectedSdqTestLog = null;
 			
 			//선택한 테스트 로그 정보 가져오기
 			if(request.getParameter("sdqTestLogId")==null||request.getParameter("sdqTestLogId").equals("0")) {//사용자가 가장 최근에 수행한 검사 기록 가져오기
 				Comparator<SdqTestLog> comparatorById = Comparator.comparingInt(SdqTestLog::getSdqTestLogId);
 				selectedSdqTestLog = sdqTestLogList.stream().max(comparatorById).orElseThrow(NoSuchElementException::new);
 			}else {
 				selectedSdqTestLog = SdqTestLogDAO.getSdqTestLogById(conn, Integer.parseInt(request.getParameter("sdqTestLogId")));
 			}
 			
 			//선택한 테스트 로그에 대한 응답값을 기준으로 결과 값 가져오기
 			ArrayList<SdqResultOfType> sdqResult = (ArrayList<SdqResultOfType>)SdqReplyDAO.getSdqResultOfTypesBySdqTestLogId(conn, selectedSdqTestLog.getSdqTestLogId());
 			
 			//결과 분석
 			ArrayList<SdqResultAnalysis> sdqResultAnalysisList = new ArrayList<SdqResultAnalysis>();

 			for(int i=0;i<sdqResult.size();i++) {
 				sdqResultAnalysisList.add(SdqResultAnalysisDAO.findSdqResultAnalysisByTypeAndValue(conn, sdqResult.get(i).getSdqType(),sdqResult.get(i).getResult()));
 			}
 			
 			request.setAttribute("focusUser", focusUser);
 			request.setAttribute("sdqTestLogList", sdqTestLogList);
 			request.setAttribute("selectedSdqTestLog", selectedSdqTestLog);
 			request.setAttribute("sdqResult", sdqResult);
 			request.setAttribute("sdqResultAnalysisList",sdqResultAnalysisList);
 			
 			RequestDispatcher rd = request.getRequestDispatcher("/sdqResultAll.jsp");
 			rd.forward(request, response);
 		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
