package controller.esm.test;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.EsmEmotionDAO;
import model.dto.EsmEmotion;

/**
 * @author Jiwon Lee
 * ESM - 정서 반복 기록 : 감정 문항 불러와서 뷰로 전달 
 * * * 변수 * *
 *  esmEmotionList - 사용자가 응답할 문항(김정) 리스트 : 정서 반복 기록 페이지로 뿌려줄 데이터
 *  esmEmotionTargetList - 사용자가 응답한 문항(감정) 리스트
 *  esmEmotionRecordMap - 사용자 응답 저장
 */
@WebServlet("/GetEsmTest")
public class GetEsmTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetEsmTest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    HttpSession session = request.getSession(true);
	    
	 	ServletContext sc = getServletContext();
	 	Connection conn= (Connection) sc.getAttribute("DBconnection");
	 	
	 	String esmType = request.getParameter("currEsmType");
	 	String forwardLocation = "/esmTest.jsp";
	 	
	 	if(esmType.equals("none")) {//기록 시작
	 		esmType = "positive";
	 		
	 		//다음 감정 문항 전달
		 	ArrayList<EsmEmotion> esmEmotionList = (ArrayList<EsmEmotion>)EsmEmotionDAO.getEsmEmotionListByEsmType(conn, esmType);
		 	request.setAttribute("esmEmotionList", esmEmotionList);
		 	
	 	}else if(esmType.equals("positive")){//긍정 감정 기록 종료 시
	 		ArrayList<EsmEmotion> esmEmotionTargetList = (ArrayList<EsmEmotion>)EsmEmotionDAO.getEsmEmotionListByEsmType(conn, esmType);
	 		HashMap<Integer, Integer> esmEmotionRecordMap = new HashMap<Integer, Integer>();//emotion번호, 결과
	 		
	 		for(int i=0;i<esmEmotionTargetList.size();i++) {
	 			esmEmotionRecordMap.put(esmEmotionTargetList.get(i).getEsmEmotionId(), Integer.parseInt(request.getParameter(esmEmotionTargetList.get(i).getEsmEmotion())));
	 		}
	 		
	 		session.setAttribute("esmEmotionRecordMap", esmEmotionRecordMap);
	 		esmType = "negative";
	 		
	 		//다음 감정 문항 전달
		 	ArrayList<EsmEmotion> esmEmotionList = (ArrayList<EsmEmotion>)EsmEmotionDAO.getEsmEmotionListByEsmType(conn, esmType);
		 	request.setAttribute("esmEmotionList", esmEmotionList);
	 		
	 	}else if(esmType.equals("negative")) {//부정 감정 기록 종료 시 
	 		ArrayList<EsmEmotion> esmEmotionTargetList = (ArrayList<EsmEmotion>)EsmEmotionDAO.getEsmEmotionListByEsmType(conn, esmType);
	 		
	 		@SuppressWarnings("unchecked")
			HashMap<Integer, Integer> esmEmotionRecordMap = (HashMap<Integer, Integer>)session.getAttribute("esmEmotionRecordMap");
	 		
	 		for(int i=0;i<esmEmotionTargetList.size();i++) {
	 			esmEmotionRecordMap.put(esmEmotionTargetList.get(i).getEsmEmotionId(), Integer.parseInt(request.getParameter(esmEmotionTargetList.get(i).getEsmEmotion())));
	 		}
	 		session.setAttribute("esmEmotionRecordMap", esmEmotionRecordMap);
	 		
	 		forwardLocation = "/DoEsmTest";
	 	}
	 	
	 	RequestDispatcher rd = request.getRequestDispatcher(forwardLocation);
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
