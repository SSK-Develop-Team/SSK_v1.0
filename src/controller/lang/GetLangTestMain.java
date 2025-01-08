package controller.lang;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import model.dto.AgeGroup;
import model.dto.User;
import model.sevice.AgeGroupService;
import util.process.UserInfoProcessor;
import model.dao.AgeGroupDAO;

import java.sql.Date;
import java.util.ArrayList;

/**
 * - 해당 아동의 AgeGroup, 전체 AgeGroup 구하기
 */


@WebServlet("/GetLangTestMain")
public class GetLangTestMain extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public GetLangTestMain() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession(true);

		ServletContext sc = getServletContext();
		Connection con = (Connection)sc.getAttribute("DBconnection");
		User currUser = (User)session.getAttribute("currUser");
		
		Date userBirth = currUser.getUserBirth();
		int nowAge = UserInfoProcessor.getUserBirthToCurrAge(userBirth);
		System.out.println("nowAge" + nowAge);
		
		AgeGroup currAgeGroup = AgeGroupDAO.getCurrAgeGroup(con, nowAge);
		System.out.println("currAgeGroup : " + currAgeGroup.getAgeGroupId());
		ArrayList<AgeGroup> selectableAgeGroupList = AgeGroupService.getSelectableAgeGroupList(con, currAgeGroup);
		System.out.println("selectableAgeGroupList : " + selectableAgeGroupList.size());

		session.setAttribute("currAgeGroup", currAgeGroup);
		session.setAttribute("selectableAgeGroupList", selectableAgeGroupList);

		RequestDispatcher rd = request.getRequestDispatcher("/langTestMain.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}