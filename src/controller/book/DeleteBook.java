package controller.book;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.JsonObject;

import model.dao.BookDAO;
import model.dto.User;

@WebServlet("/bookDelete")
public class DeleteBook extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(true);
        User currUser = (User) session.getAttribute("currUser");
        if (currUser == null) {
            response.setStatus(401);
            response.getWriter().write("{\"ok\":false,\"error\":\"not logged in\"}");
            return;
        }

        long bookId = parseLong(request.getParameter("bookId"), -1L);
        if (bookId <= 0) {
            response.setStatus(400);
            response.getWriter().write("{\"ok\":false,\"error\":\"bookId required\"}");
            return;
        }

        ServletContext sc = getServletContext();
        Connection conn = (Connection) sc.getAttribute("DBconnection");

        JsonObject res = new JsonObject();

        try {
            boolean ok = new BookDAO().deleteBookById(conn, bookId, currUser.getUserId());
            if (!ok) {
                // 존재하지 않거나 / 내 책이 아니거나
                response.setStatus(403);
                res.addProperty("ok", false);
                res.addProperty("error", "forbidden or not found");
                response.getWriter().write(res.toString());
                return;
            }

            // book 삭제되면 book_page, parent_guide는 FK ON DELETE CASCADE로 자동 삭제됨
            res.addProperty("ok", true);
            response.getWriter().write(res.toString());

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            res.addProperty("ok", false);
            res.addProperty("error", "delete failed: " + safe(e.getMessage()));
            response.getWriter().write(res.toString());
        }
    }

    private static long parseLong(String s, long fallback) {
        try { return Long.parseLong(s); } catch (Exception e) { return fallback; }
    }

    private static String safe(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}