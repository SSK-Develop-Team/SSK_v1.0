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
}
