package model.dao;

import java.sql.*;
import model.dto.ParentGuide;

public class ParentGuideDAO {

    public static void upsert(Connection con, ParentGuide pg) throws SQLException {
        String sql = "INSERT INTO parent_guide(book_id, guide_text, created_time) VALUES(?, ?, Now()) " +
                     "ON DUPLICATE KEY UPDATE guide_text=VALUES(guide_text)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, pg.getBookId());
            ps.setString(2, pg.getGuideText());
            ps.executeUpdate();
        }
    }

    public static ParentGuide getParentGuideByBookId(Connection con, long bookId) throws SQLException {
        String sql = "SELECT * FROM parent_guide WHERE book_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                ParentGuide g = new ParentGuide();
                g.setBookId(rs.getLong("book_id"));
                g.setGuideText(rs.getString("guide_text"));
                g.setCreatedTime(rs.getTimestamp("created_time"));
                return g;
            }
        }
    }
}