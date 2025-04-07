package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserTokenDAO {
    public static List<String> getUserTokens(Connection con, int userId) {
        List<String> tokens = new ArrayList<>();
        String query = "SELECT fcm_token FROM user_tokens WHERE user_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    tokens.add(rs.getString("fcm_token"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tokens;
    }

    // 사용자 토큰 삽입 또는 업데이트
    public static boolean insertUserToken(Connection con, int userId, String fcmToken) {
        String sql = "INSERT INTO ssk.user_tokens (user_id, fcm_token) VALUES (?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, fcmToken);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // 성공적으로 저장되었으면 True 반환
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteUserToken(Connection conn, int userId, String fcmToken) {
        String sql = "DELETE FROM user_tokens WHERE user_id = ? AND fcm_token = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, fcmToken);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0; // 삭제 성공 여부 반환
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
