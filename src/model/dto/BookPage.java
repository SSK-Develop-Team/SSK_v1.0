package model.dto;

public class BookPage {
    private long bookId;
    private int pageNo;
    private String pageContent;
    private String pageImagePath;

    public long getBookId() { return bookId; }
    public void setBookId(long bookId) { this.bookId = bookId; }

    public int getPageNo() { return pageNo; }
    public void setPageNo(int pageNo) { this.pageNo = pageNo; }

    public String getPageContent() { return pageContent; }
    public void setPageContent(String pageContent) { this.pageContent = pageContent; }

    public String getPageImagePath() { return pageImagePath; }
    public void setPageImagePath(String pageImagePath) { this.pageImagePath = pageImagePath; }
}