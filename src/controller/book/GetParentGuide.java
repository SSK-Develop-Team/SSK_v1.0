package controller.book;

import com.google.gson.JsonObject;
import model.dao.BookDAO;
import model.dao.ParentGuideDAO;
import model.dto.Book;
import model.dto.ParentGuide;
import model.dto.User;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/GetParentGuide")
public class GetParentGuide extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json; charset=UTF-8");
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(true);
        User currUser = (User) session.getAttribute("currUser");
        if (currUser == null) {
            resp.setStatus(401);
            resp.getWriter().write("{\"error\":\"not logged in\"}");
            return;
        }

        long bookId = parseLong(req.getParameter("bookId"), -1L);
        if (bookId <= 0) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"bookId required\"}");
            return;
        }

        ServletContext sc = getServletContext();
        Connection conn = (Connection) sc.getAttribute("DBconnection");

        try {
            Book book = BookDAO.getBookById(conn, bookId);
            if (book == null) {
                resp.setStatus(404);
                resp.getWriter().write("{\"error\":\"book not found\"}");
                return;
            }

            if (book.getUserId() != currUser.getUserId()) {
                resp.setStatus(403);
                resp.getWriter().write("{\"error\":\"forbidden\"}");
                return;
            }

            ParentGuide parentGuide = ParentGuideDAO.getParentGuideByBookId(conn, bookId);

            JsonObject out = new JsonObject();
            out.addProperty("bookId", bookId);
            out.addProperty("guideText", parentGuide.getGuideText() == null ? "" : parentGuide.getGuideText());
            resp.getWriter().write(out.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"server error\"}");
        }
    }

    private static long parseLong(String s, long fallback) {
        try { return Long.parseLong(s); } catch (Exception e) { return fallback; }
    }
}
