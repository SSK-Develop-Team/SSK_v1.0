package model.dto;

import java.sql.Date;
import java.sql.Time;

public class EsmRecord {

	private int esmRecordId;
	private String esmRecordText;
	private Date esmRecordDate;
	private Time esmRecordTime;
	private int userId;
	
	public int getEsmRecordId() {
		return esmRecordId;
	}
	public void setEsmRecordId(int esmRecordId) {
		this.esmRecordId = esmRecordId;
	}
	public String getEsmRecordText() {
		return esmRecordText;
	}
	public void setEsmRecordText(String esmRecordText) {
		this.esmRecordText = esmRecordText;
	}
	public Date getEsmRecordDate() {
		return esmRecordDate;
	}
	public void setEsmRecordDate(Date esmRecordDate) {
		this.esmRecordDate = esmRecordDate;
	}
	public Time getEsmRecordTime() {
		return esmRecordTime;
	}
	public void setEsmRecordTime(Time esmRecordTime) {
		this.esmRecordTime = esmRecordTime;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
}
