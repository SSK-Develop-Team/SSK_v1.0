package model.dto;

import java.sql.Date;
import java.sql.Time;

public class LangTestLog {
	private int langTestLogId;
	private int userId;
	private Date langTestDate;
	private Time langTestTime;
	
	public int getLangTestLogId() {
		return langTestLogId;
	}
	public void setLangTestLogId(int langTestLogId) {
		this.langTestLogId = langTestLogId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getLangTestDate() {
		return langTestDate;
	}
	public void setLangTestDate(Date langTestDate) {
		this.langTestDate = langTestDate;
	}
	public Time getLangTestTime() {
		return langTestTime;
	}
	public void setLangTestTime(Time langTestTime) {
		this.langTestTime = langTestTime;
	}

}
