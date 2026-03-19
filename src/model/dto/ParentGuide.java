package model.dto;

import java.sql.Timestamp;

public class ParentGuide {
    private long bookId;
    private String guideText;
    private Timestamp createdTime;

    public long getBookId() { return bookId; }
    public void setBookId(long bookId) { this.bookId = bookId; }

    public String getGuideText() { return guideText; }
    public void setGuideText(String guideText) { this.guideText = guideText; }

    public Timestamp getCreatedTime() { return createdTime; }
    public void setCreatedTime(Timestamp createdTime) { this.createdTime = createdTime; }
}