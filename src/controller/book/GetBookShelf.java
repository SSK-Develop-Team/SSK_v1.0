package controller.book;

import model.dao.BookDAO;
import model.dao.ParentGuideDAO;
import model.dto.Book;
import model.dto.ParentGuide;
import model.dto.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

@WebServlet("/GetBookShelf")
public class GetBookShelf extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(true);
        User currUser = (User) session.getAttribute("currUser");
        if (currUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        ServletContext sc = getServletContext();
        Connection conn = (Connection) sc.getAttribute("DBconnection");

        try {
            ArrayList<Book> books = BookDAO.getBooksByUserId(conn, currUser.getUserId());
            if (books == null || books.size() == 0) books = new ArrayList<>();
            
            // (옵션) 리스트에서 모달에 부모가이드까지 같이 띄우고 싶으면:
            // 책 수가 많아지면 N+1이 되므로, 추후 JOIN으로 개선 권장
            // 여기서는 UI 편의를 위해 가볍게 붙일 수도 있음
            // request.setAttribute("guides", ...);

            request.setAttribute("books", books);

            RequestDispatcher rd = request.getRequestDispatcher("/bookShelf.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("books", new ArrayList<Book>());
            request.setAttribute("errorMsg", "책 목록을 불러오지 못했어요.");
            request.getRequestDispatcher("/bookShelf.jsp").forward(request, response);
           }
    }
}