package controller.book;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import com.google.gson.JsonObject;

import integration.PythonChatClient;
import model.dto.User;
import util.JsonUtil;

@WebServlet("/ChatApi")
public class ChatApiServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
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

        JsonObject body = JsonUtil.gson().fromJson(req.getReader(), JsonObject.class);
        String msg = (body != null && body.has("message")) ? body.get("message").getAsString() : null;
        if (msg == null || msg.trim().isEmpty()) {
            resp.setStatus(400);
            resp.getWriter().write("{\"error\":\"message required\"}");
            return;
        }
        msg = msg.trim();

        String clientThreadId =
        	    (body != null && body.has("thread_id") && !body.get("thread_id").isJsonNull())
        	        ? body.get("thread_id").getAsString()
        	        : null;

    	String threadId = (clientThreadId != null && !clientThreadId.trim().isEmpty())
    	        ? clientThreadId.trim()
    	        : (String) session.getAttribute("chatThreadId");

    	if (threadId == null || isBlank(threadId)) {
    	    threadId = UUID.randomUUID().toString();
    	}

    	session.setAttribute("chatThreadId", threadId);

        String baseUrl = getServletContext().getInitParameter("PY_BASE_URL");
        if (baseUrl == null || isBlank(baseUrl)) baseUrl = "http://127.0.0.1:8000";
        
        Boolean booted = (Boolean) session.getAttribute("chatBooted");
        if (booted == null) booted = false; 
        String prevThreadId = (String) session.getAttribute("chatThreadId_prev");
        
        if (prevThreadId == null || !prevThreadId.equals(threadId)) {
            booted = false;
            session.setAttribute("chatBooted", false);
            session.setAttribute("chatThreadId_prev", threadId);

            // 수집값도 초기화(선택)
            session.removeAttribute("story_title");
            session.removeAttribute("story_language");
            session.removeAttribute("story_elements");
            session.removeAttribute("story_theme_choice");
            session.removeAttribute("story_main_theme");
            session.removeAttribute("step");
        }
        
        try {
            PythonChatClient client = new PythonChatClient(baseUrl);
            JsonObject out;
            
            if (!booted) {
                String name = (String) session.getAttribute("bookUserName");
                Integer age = (Integer) session.getAttribute("bookUserAge");
                String gender = (String) session.getAttribute("bookUserGender");

                // 사용자의 첫 입력을 boot 메시지로 사용
                out = client.chatBoot(threadId, msg, name, age, gender);
                session.setAttribute("chatBooted", true);
            } else {
                out = client.chatTurn(threadId, msg);
            }

            // FastAPI /chat 응답 반영
            int step = out.has("step") ? out.get("step").getAsInt() : 1;
            boolean doneCollect = out.has("done_collect") && out.get("done_collect").getAsBoolean();

            session.setAttribute("step", step);
            if (out.has("story_title") && !out.get("story_title").isJsonNull())
                session.setAttribute("story_title", out.get("story_title").getAsString());
            if (out.has("story_language") && !out.get("story_language").isJsonNull())
                session.setAttribute("story_language", out.get("story_language").getAsString());
            if (out.has("story_elements") && !out.get("story_elements").isJsonNull())
                session.setAttribute("story_elements", out.get("story_elements").getAsString());
            if (out.has("story_theme_choice") && !out.get("story_theme_choice").isJsonNull()) 
                session.setAttribute("story_theme_choice", out.get("story_theme_choice").getAsInt());
            if (out.has("story_main_theme") && !out.get("story_main_theme").isJsonNull())
                session.setAttribute("story_main_theme", out.get("story_main_theme").getAsString());

            // 프론트로 그대로 전달
            JsonObject res = new JsonObject();
            res.addProperty("reply", out.has("reply") ? out.get("reply").getAsString() : "");
            res.addProperty("step", step);
            res.addProperty("done_collect", doneCollect);

            resp.getWriter().write(res.toString());

        } catch (Exception e) {
            resp.setStatus(500);
            resp.getWriter().write("{\"error\":\"" + escape(e.getMessage()) + "\"}");
        }
        
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
    
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }    
}