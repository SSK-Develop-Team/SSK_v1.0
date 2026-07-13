package controller.book;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;

import model.dto.User;

/**
 * Servlet implementation class GetBookReadMode
 */
@WebServlet("/GetBookReadMode")
public class GetBookReadMode extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GetBookReadMode() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        HttpSession session = (HttpSession) request.getSession();
        
        ServletContext sc = getServletContext();
        Connection con = (Connection)sc.getAttribute("DBconnection");
        
        // 로그인 유저 확인
        User currUser = (User) session.getAttribute("currUser");
        if (currUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        RequestDispatcher rd = request.getRequestDispatcher("/bookReadMode.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
