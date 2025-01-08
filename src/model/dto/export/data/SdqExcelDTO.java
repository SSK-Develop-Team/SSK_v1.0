package model.dto.export.data;

import model.dto.SdqResultOfType;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

/**
 * xlsx 추출용 DTO - sdq data
 */
public class SdqExcelDTO {
    private int userId;
    private String userName;

    private int id;//검사 식별 번호
    private String target;//검사 종류 : 부모용 or 아동용
    private String dateStr;//년월일시분
    private ArrayList<Integer> replyList;//문항별 응답(문항 개수 : 10)
    private ArrayList<SdqResultOfType> scoreList;//sdq type 별 점수(type 개수 : 5)
    /*
     * index 
     * 0 - 사회지향행동
     * 1 - 과잉행동
     * 2 - 정서증상
     * 3 - 품행문제
     * 4 - 또래문제
     */

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

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    public ArrayList<SdqResultOfType> getScoreList() {
        return scoreList;
    }

    public void setScoreList(ArrayList<SdqResultOfType> scoreList) {
        this.scoreList = scoreList;
    }
    

}
