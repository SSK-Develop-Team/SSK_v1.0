package controller.expert;

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

import model.dto.SearchUserType;
import model.dto.User;
import model.dto.UserPaging;
import model.sevice.UserListService;

/**
 * 전문가 홈 - 모든 아동 리스트 조회 페이지
 */
@WebServlet("/GetExpertHome")
public class GetExpertHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetExpertHome() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    
	    ServletContext sc = getServletContext();
	 	Connection conn= (Connection) sc.getAttribute("DBconnection");
	 	
		// 아동 pagination 초기화
	 	int curPage = 1;
	 	SearchUserType searchType = null;
	 	String keyword = null;
	 	UserPaging userPaging = new UserPaging();
	 	if(request.getParameter("curPage")==null) {
	 		curPage = 1;
	 	}else {
	 		curPage = Integer.parseInt(request.getParameter("curPage"));
	 	}
	 	
	 	if(request.getParameter("searchType")!=null&&request.getParameter("keyword")!=null) { //검색어가 있는 경우
	 		searchType = SearchUserType.findByTypeName(request.getParameter("searchType"));
	 		keyword = request.getParameter("keyword");
	 		userPaging.makeLastPageNum(conn, keyword);
	 	}else {
	 		userPaging.makeLastPageNum(conn);
	 	}
	 	
		userPaging.makeBlock(curPage);//첫 페이지로 시작
		
		//페이지에 해당하는 아동 목록 불러오기 - default 정렬 : 등록일 순
		int length = UserPaging.getListRange();
		int startIndex = (curPage-1)*length;
		
		ArrayList<User> currUserList = UserListService.getUserListByFilter(conn, "CHILD", searchType, keyword, startIndex, length);
		
		if(searchType!=null&&keyword!=null) {
			request.setAttribute("searchType", SearchUserType.getTypeName(searchType));
			request.setAttribute("keyword", keyword);
		}
		
		request.setAttribute("userPaging",userPaging);
		request.setAttribute("currPageNum", curPage);
		request.setAttribute("currUserList", currUserList);
		
		RequestDispatcher rd = request.getRequestDispatcher("/expertHome.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
