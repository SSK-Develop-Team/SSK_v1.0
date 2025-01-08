package controller.lang;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.dao.LangReplyDAO;
import model.dao.LangTestLogDAO;
import model.dto.LangQuestion;
import model.dto.LangReply;
import model.dto.LangTestLog;
import model.dto.User;

/**
 * 테스트 결과 저장
 */
@WebServlet("/DoLangTest")
public class DoLangTest extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DoLangTest() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession(true);

		ServletContext sc = getServletContext();
		Connection conn = (Connection) sc.getAttribute("DBconnection");

		User currUser = (User) session.getAttribute("currUser");
		@SuppressWarnings("unchecked")
		ArrayList<LangQuestion> langQuestionList = (ArrayList<LangQuestion>) session.getAttribute("currQuestionList");

		// 1. 테스트 로그 저장
		Date now = new Date(System.currentTimeMillis());

		LangTestLog langTestLog = new LangTestLog();
		langTestLog.setUserId(currUser.getUserId());
		langTestLog.setLangTestDate(now);
		langTestLog.setLangTestTime(new Time(now.getTime()));

		// 2. 테스트 결과 저장
		ArrayList<LangReply> langReplyList = new ArrayList<LangReply>();

		if ((request.getParameter("reply0") != null && request.getParameter("reply1") != null
				&& request.getParameter("reply2") != null && request.getParameter("reply3") != null
				&& request.getParameter("reply4") != null)||(request.getParameter("reply0") != null && request.getParameter("reply1") != null
				&& request.getParameter("reply2") != null && request.getParameter("reply3") != null && langQuestionList.get(4).getLangQuestionId() == 50)) {


			LangTestLogDAO.insertLangTestLog(conn, langTestLog);

			for (int i = 0; i < 5; i++) {
				LangReply langReply = new LangReply();
				langReply.setLangTestLogId(langTestLog.getLangTestLogId());
				langReply.setLangQuestionId(langQuestionList.get(i).getLangQuestionId());
				if(langQuestionList.get(4).getLangQuestionId() == 50 && i == 4){
					langReply.setLangReplyContent(0);
				}else{
					langReply.setLangReplyContent(Integer.parseInt(request.getParameter("reply" + i)));
				}
				if (LangReplyDAO.insertLangReply(conn, langReply)) {
					System.out.println(i + "번째 응답 삽입 성공");
					langReplyList.add(langReply);

				} else {
					System.out.println(i + "번째 응답 삽입 실패");
				}
			}
		} else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script>alert('입력되지 않은 항목이 있습니다.'); history.go(-1);</script>");
			out.flush();
			response.flushBuffer();
			out.close();
		}

		request.setAttribute("langTestLog", langTestLog);
		request.setAttribute("langReplyList", langReplyList);
		request.setAttribute("langQuestionList", langQuestionList);

		request.setAttribute("currLangTestLogId", langTestLog.getLangTestLogId());

		response.sendRedirect(getServletContext().getContextPath() + "/GetLangResultAll?isTesting=1");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
