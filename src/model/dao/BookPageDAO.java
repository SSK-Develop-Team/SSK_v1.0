package model.dao;

import java.sql.*;
import java.util.ArrayList;
import model.dto.BookPage;

public class BookPageDAO {

    public static void insertPages(Connection con, ArrayList<BookPage> pages) throws SQLException {
        String sql = "INSERT INTO book_page(book_id, page_no, page_content, page_image_path) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
        	
        	for (BookPage p:pages) {
	            ps.setLong(1, p.getBookId());
	            ps.setInt(2, p.getPageNo());
	            ps.setString(3, p.getPageContent());
	            ps.setString(4, p.getPageImagePath());
	            ps.addBatch();
        	}
            ps.executeBatch();
        }
    }

    public static ArrayList<BookPage> getPagesByBookId(Connection con, long bookId) throws SQLException {
        String sql = "SELECT * FROM book_page WHERE book_id=? ORDER BY page_no ASC";
        ArrayList<BookPage> out = new ArrayList<>();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setLong(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BookPage bp = new BookPage();
                    bp.setBookId(rs.getLong("book_id"));
                    bp.setPageNo(rs.getInt("page_no"));
                    bp.setPageContent(rs.getString("page_content"));
                    bp.setPageImagePath(rs.getString("page_image_path"));
                    out.add(bp);
                }
            }
        }
        return out;
    }
}