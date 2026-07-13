package model.dto;

import java.sql.Timestamp;

public class Book {
    private long bookId;
    private int userId;

    private String storyTitle;
    private String storyLanguage;
    private String storyElements;
    private Integer selGoalCode;
    private String selGoalLabel;
    private String situationSummary;
    private String emotion;
    private String desire;
    private String ttsClip;

    private String contextSituation ;
    private String extraNotes;

    private Timestamp createdTime;

    public long getBookId() { return bookId; }
    public void setBookId(long bookId) { this.bookId = bookId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getStoryTitle() { return storyTitle; }
    public void setStoryTitle(String storyTitle) { this.storyTitle = storyTitle; }
    
    public String getStoryLanguage() { return storyLanguage; }
    public void setStoryLanguage(String storyLanguage) { this.storyLanguage = storyLanguage; }

    public String getStoryElements() { return storyElements; }
    public void setStoryElements(String storyElements) { this.storyElements = storyElements; }

    public Integer getSelGoalCode() { return selGoalCode; }
    public void setSelGoalCode(Integer selGoalCode) { this.selGoalCode = selGoalCode; }

    public String getSelGoalLabel() { return selGoalLabel; }
    public void setSelGoalLabel(String selGoalLabel) { this.selGoalLabel = selGoalLabel; }

    public String getContextSituation() { return contextSituation; }
    public void setContextSituation(String contextSituation) { this.contextSituation = contextSituation; }

    public String getExtraNotes() { return extraNotes; }
    public void setExtraNotes(String extraNotes) { this.extraNotes = extraNotes; }

    public Timestamp getCreatedTime() { return createdTime; }
    public void setCreatedTime(Timestamp createdTime) { this.createdTime = createdTime; }
    
    public String getSituationSummary() { return situationSummary; }
    public void setSituationSummary(String situationSummary) { this.situationSummary = situationSummary; }
    
    public String getEmotion() { return emotion; }
    public void setEmotion(String emotion) { this.emotion = emotion; }
    
    public String getDesire() { return desire; }
    public void setDesire(String desire) { this.desire = desire; }
    
    public String getTtsClip() { return ttsClip; }
    public void setTtsClip(String ttsClip) { this.ttsClip = ttsClip; }
}