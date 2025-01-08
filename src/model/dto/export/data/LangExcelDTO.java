package model.dto.export.data;

import model.dto.AgeGroup;

import java.sql.Date;
import java.util.ArrayList;

public class LangExcelDTO {
    private int userId;
    private String userName;

    private int id;
    private String dateStr;

    private String ageGroupStr;
    private ArrayList<String> replyList;

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

    public void setDateStr(Date date) {
        this.dateStr = date.toString();
    }

    public String getAgeGroupStr() {
        return ageGroupStr;
    }

    public void setAgeGroupStr(int ageGroupId) {
        this.ageGroupStr = AgeGroup.getAgeGroupStrByAgeGroupId(ageGroupId);
    }


    public ArrayList<String> getReplyList() {
        return replyList;
    }

    public void setReplyList(ArrayList<String> replyList) {
        this.replyList = replyList;
    }

}
