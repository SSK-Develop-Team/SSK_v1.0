package controller.book;

import com.google.gson.Gson;
import model.dao.BookDAO;
import model.dao.BookPageDAO;
import model.dto.Book;
import model.dto.BookPage;
import model.dto.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

@WebServlet("/GetBookView")

public class GetBookView extends HttpServlet{
    private static final long serialVersionUID = 1L;
    private static final Gson GSON = new Gson();

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

        long bookId = parseLong(request.getParameter("bookId"), -1L);
        if (bookId <= 0) {
            response.sendError(400, "bookId required");
            return;
        }

        ServletContext sc = getServletContext();
        Connection conn = (Connection) sc.getAttribute("DBconnection");

        try {
            Book book = BookDAO.getBookById(conn, bookId);
            if (book == null) {
                response.sendError(404, "book not found");
                return;
            }
            
            if (book.getUserId() != currUser.getUserId()) {
            	  response.sendError(403, "forbidden");
            	  return;
            	}
            
            ArrayList<BookPage> pages = BookPageDAO.getPagesByBookId(conn, bookId);
            pages.sort(Comparator.comparingInt(BookPage::getPageNo));

            List<Map<String, Object>> pagePayload = new ArrayList<>();
            for (BookPage p : pages) {
                Map<String, Object> m = new HashMap<>();
                m.put("pageNo", p.getPageNo());
                m.put("pageText", p.getPageContent());
                m.put("imageUrl", p.getPageImagePath());
                pagePayload.add(m);
            }

            request.setAttribute("book", book);
            request.setAttribute("pagesJson", GSON.toJson(pagePayload));
            request.setAttribute("totalPages", pages.size());

            RequestDispatcher rd = request.getRequestDispatcher("/bookView.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "server error");
        }
    }

    private static long parseLong(String s, long fallback) {
        try { return Long.parseLong(s); } catch (Exception e) { return fallback; }
    }
}
