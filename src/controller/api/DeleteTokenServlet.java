package controller.api;

import model.dao.UserTokenDAO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.sql.Connection;

@WebServlet(name = "DeleteTokenServlet", urlPatterns = "/deleteToken")
public class DeleteTokenServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        JSONParser jsonParser = new JSONParser();
        JSONObject requestBody;

        // JSON 요청 데이터 파싱
        try {
            requestBody = (JSONObject) jsonParser.parse(getBody(request));
        } catch (ParseException e) {
            writeResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JSON format");
            return;
        }

        System.out.println("요청 데이터: " + requestBody);

        // 요청 데이터 검증
        if (!requestBody.containsKey("userId") || !requestBody.containsKey("fcmToken")) {
            writeResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
            return;
        }

        int userId;
        String fcmToken;
        try {
            userId = Integer.parseInt(requestBody.get("userId").toString());
            fcmToken = requestBody.get("fcmToken").toString();
        } catch (Exception e) {
            writeResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid request parameters");
            return;
        }

        // DB 연결 가져오기
        ServletContext sc = getServletContext();
        if (sc == null) {
            System.err.println("ServletContext가 null 입니다.");
            writeResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "ServletContext is null");
            return;
        }

        Connection conn = (Connection) sc.getAttribute("DBconnection");
        if (conn == null) {
            System.err.println("DBconnection is null. Check DBInitializer for potential issues.");
            writeResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database connection is null");
            return;
        }

        System.out.println("DB 연결 상태: " + conn);

        // DB에서 토큰 삭제
        try {
            boolean isDeleted = UserTokenDAO.deleteUserToken(conn, userId, fcmToken);
            if (isDeleted) {
                writeResponse(response, HttpServletResponse.SC_OK, "Token deleted successfully");
            } else {
                writeResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete token");
            }
        } catch (Exception e) {
            System.err.println("DB 작업 오류: " + e.getMessage());
            e.printStackTrace();
            writeResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error occurred");
        }
    }

    // 응답 작성 헬퍼 메서드
    private void writeResponse(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        JSONObject result = new JSONObject();
        result.put("message", message);
        response.getWriter().println(result.toJSONString());
    }

    // 요청 본문 가져오기
    private String getBody(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder.toString();
    }
}
