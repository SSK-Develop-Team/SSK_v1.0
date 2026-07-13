package controller.book;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import model.dao.BookDAO;
import model.dto.Book;
import model.dto.User;
/**
 * Servlet implementation class GetBookVideo
 */
@WebServlet("/GetBookVideo")
public class GetBookVideo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public GetBookVideo() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        HttpSession session = request.getSession();
        User currUser = (User) session.getAttribute("currUser");

        if (currUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String rawBookId = request.getParameter("bookId");
        if (rawBookId == null || rawBookId.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/GetBookShelf");
            return;
        }

        long bookId;
        try {
            bookId = Long.parseLong(rawBookId);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/GetBookShelf");
            return;
        }

        ServletContext sc = getServletContext();
        Connection conn = (Connection) sc.getAttribute("DBconnection");

        try {
            Book book = BookDAO.getBookById(conn, bookId);

            if (book == null || book.getUserId() != currUser.getUserId()) {
                response.sendRedirect(request.getContextPath() + "/GetBookShelf");
                return;
            }

            request.setAttribute("book", book);

            RequestDispatcher rd = request.getRequestDispatcher("/bookVideo.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/GetBookShelf");
        }
    }
}
