package model.dto.export.data;

import model.dto.EsmResultOfType;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

public class EsmExcelDTO {
    private int userId;
    private String userName;

    private int id;
    private String dateStr;
    private ArrayList<Integer> replyList;
    private ArrayList<EsmResultOfType> scoreList;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(Date date, Time time) {
        this.dateStr = date.toString()+" "+time.toLocalTime().getHour()+"시 "+time.toLocalTime().getMinute()+"분";
    }

    public ArrayList<Integer> getReplyList() {
        return replyList;
    }

    public void setReplyList(ArrayList<Integer> replyList) {
        this.replyList = replyList;
    }

    public ArrayList<EsmResultOfType> getScoreList() {
        return scoreList;
    }

    public void setScoreList(ArrayList<EsmResultOfType> scoreList) {
        this.scoreList = scoreList;
    }
}
