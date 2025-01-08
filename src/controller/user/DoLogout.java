package controller.user;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Lee Ji Won
 * 로그아웃
 */
@WebServlet("/DoLogout")
public class DoLogout extends HttpServlet {
   private static final long serialVersionUID = 1L;
   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      request.setCharacterEncoding("UTF-8");
      
      HttpSession session = request.getSession();
      session.removeAttribute("currUser");
      session.removeAttribute("focusUser");
      session.removeAttribute("selectedExpert");
      session.removeAttribute("selectedChild");
      response.sendRedirect("./login.jsp");
   }
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
