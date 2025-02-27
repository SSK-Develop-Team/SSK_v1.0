package controller.lang;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Yurim Kang
 * 게임에서 평가 페이지로 이동하는 서블릿
 */
@WebServlet("/GetLangGameBackToTest")
public class GetLangGameBackToTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public GetLangGameBackToTest() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
	    request.setCharacterEncoding("UTF-8");
	    	    
	    response.sendRedirect(request.getContextPath() +"/langTest.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
