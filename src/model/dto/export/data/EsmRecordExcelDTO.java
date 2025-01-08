package model.dto.export.data;

import java.sql.Date;
import java.sql.Time;

public class EsmRecordExcelDTO {
    private int userId;
    private String userName;

    private int id;
    private String dateStr;
    private String text;

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
        this.dateStr = date.toString();
        if(time!=null){
            this.dateStr+=(" "+time.toLocalTime().getHour()+"시 "+time.toLocalTime().getMinute()+"분");
        }
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
