package controller.esm.test;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.dao.EsmReplyDAO;
import model.dto.EsmResultOfType;

@WebServlet("/GetEsmResult")
public class GetEsmResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetEsmResult() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    
	 	ServletContext sc = getServletContext();
	 	Connection conn= (Connection) sc.getAttribute("DBconnection");
	 	
	 	int esmTestLogId = Integer.parseInt(request.getParameter("esmTestLogId"));
	 	
	 	//선택한 esmTestLog의 EsmResult값 가져오기
	 	ArrayList<EsmResultOfType> esmResult = EsmReplyDAO.getEsmResultByEsmTestLogId(conn, esmTestLogId);
	 	
	 	//positive 점수와 negative 점수를 분리하여 전달 (코드 리팩토링 필요)
	 	int positive=0, negative=0;
	 	for(int i=0;i<esmResult.size();i++) {
	 		System.out.println(esmResult.get(i).getEsmType()+" "+esmResult.get(i).getResult());
	 		
	 		switch (esmResult.get(i).getEsmType()) {
		 		case "positive":
		 			positive = esmResult.get(i).getResult();
		 			break;
		 		case "negative":
		 			negative = esmResult.get(i).getResult();
		 			break;
	 		}
	 	}
	 	request.setAttribute("positive", positive);
	 	request.setAttribute("negative", negative);
	 	
	 	RequestDispatcher rd = request.getRequestDispatcher("/esmResult.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
