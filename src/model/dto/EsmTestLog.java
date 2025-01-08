package model.dto;

import java.sql.Date;
import java.sql.Time;

public class EsmTestLog {
	
	private int esmTestLogId;
	private int userId;
	private Date esmTestDate;
	private Time esmTestTime;
	
	public EsmTestLog() {
	}
	
	public EsmTestLog(int userId, Date esmTestDate, Time esmTestTime) {
		super();
		this.userId = userId;
		this.esmTestDate = esmTestDate;
		this.esmTestTime = esmTestTime;
	}

	public int getEsmTestLogId() {
		return esmTestLogId;
	}
	public void setEsmTestLogId(int esmTestLogId) {
		this.esmTestLogId = esmTestLogId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getEsmTestDate() {
		return esmTestDate;
	}
	public void setEsmTestDate(Date esmTestDate) {
		this.esmTestDate = esmTestDate;
	}
	public Time getEsmTestTime() {
		return esmTestTime;
	}
	public void setEsmTestTime(Time esmTestTime) {
		this.esmTestTime = esmTestTime;
	}
	
}
