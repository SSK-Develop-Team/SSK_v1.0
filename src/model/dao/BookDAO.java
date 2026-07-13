package model.dao;

import java.sql.*;
import java.util.ArrayList;

import model.dto.Book;

public class BookDAO {

    public static long insertBook(Connection con, Book book) throws SQLException {
        String sql = "INSERT INTO book(" +
                "user_id, story_title, story_language, story_elements, sel_goal_code, sel_goal_label," +
                "context_situation, situation_summary, emotion, desire, extra_notes" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, book.getUserId());
            ps.setString(2, book.getStoryTitle());
            ps.setString(3, book.getStoryLanguage());
            ps.setString(4, book.getStoryElements());

            if (book.getSelGoalCode() == null) ps.setNull(5, Types.TINYINT);
            else ps.setInt(5, book.getSelGoalCode());

            ps.setString(6, book.getSelGoalLabel());
            ps.setString(7, book.getContextSituation());
            ps.setString(8, book.getSituationSummary());
            ps.setString(9, book.getEmotion());
            ps.setString(10, book.getDesire());
            ps.setString(11, book.getExtraNotes());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getLong(1);
            }
        }
        throw new SQLException("Failed to get generated book_id");
    }   
    
    
    public static Book getBookById(Connection con, long bookId) throws SQLException {
        String sql = "SELECT * FROM book WHERE book_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;

                Book b = new Book();
                b.setBookId(rs.getLong("book_id"));
                b.setUserId(rs.getInt("user_id"));
                b.setStoryTitle(rs.getString("story_title"));
                b.setStoryLanguage(rs.getString("story_language"));
                b.setStoryElements(rs.getString("story_elements"));

                int code = rs.getInt("sel_goal_code");
                b.setSelGoalCode(rs.wasNull() ? null : code);

                b.setSelGoalLabel(rs.getString("sel_goal_label"));
                b.setContextSituation(rs.getString("context_situation"));
                b.setSituationSummary(rs.getString("situation_summary"));
                b.setEmotion(rs.getString("emotion"));
                b.setDesire(rs.getString("desire"));
                b.setExtraNotes(rs.getString("extra_notes"));
                b.setCreatedTime(rs.getTimestamp("created_time"));
                
                b.setTtsClip(rs.getString("tts_clip"));
                return b;
            }
        }
    }
    
    public static ArrayList<Book> getBooksByUserId(Connection con, int userId) throws SQLException {
        String sql = "SELECT * FROM book WHERE user_id=? ORDER BY created_Time DESC";
        ArrayList<Book> out = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Book b = new Book();
                    b.setBookId(rs.getLong("book_id"));
                    b.setUserId(rs.getInt("user_id"));
                    b.setStoryTitle(rs.getString("story_title"));
                    b.setStoryLanguage(rs.getString("story_language"));
                    b.setStoryElements(rs.getString("story_elements"));

                    int code = rs.getInt("sel_goal_code");
                    b.setSelGoalCode(rs.wasNull() ? null : code);

                    b.setSelGoalLabel(rs.getString("sel_goal_label"));
                    b.setContextSituation(rs.getString("context_situation"));
                    b.setSituationSummary(rs.getString("situation_summary"));
                    b.setEmotion(rs.getString("emotion"));
                    b.setDesire(rs.getString("desire"));
                    b.setExtraNotes(rs.getString("extra_notes"));
                    b.setCreatedTime(rs.getTimestamp("created_Time"));
                    
                    b.setTtsClip(rs.getString("tts_clip"));
                    out.add(b);
                }
            }
        }
        return out;
    }
    
    public static void updateTtsClip(Connection con, long bookId, String ttsClip) throws SQLException {
        String sql = "UPDATE book SET tts_clip=? WHERE book_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ttsClip);
            ps.setLong(2, bookId);
            ps.executeUpdate();
        }
    }
    
    public boolean deleteBookById(Connection con, long bookId, int userId) throws SQLException {
        String sql = "DELETE FROM book WHERE book_id=? AND user_id=?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, bookId);
            ps.setInt(2, userId);
            int affected = ps.executeUpdate();
            return affected > 0;
        }
    }
}