package model.dto;

import java.sql.Date;
import java.sql.Time;

public class SdqTestLog {
	
	private int sdqTestLogId;
	private int userId;
	private Date sdqTestDate;
	private Time sdqTestTime;
	
	public int getSdqTestLogId() {
		return sdqTestLogId;
	}
	public void setSdqTestLogId(int sdqTestLogId) {
		this.sdqTestLogId = sdqTestLogId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Date getSdqTestDate() {
		return sdqTestDate;
	}
	public void setSdqTestDate(Date sdqTestDate) {
		this.sdqTestDate = sdqTestDate;
	}
	public Time getSdqTestTime() {
		return sdqTestTime;
	}
	public void setSdqTestTime(Time timeStr) {
		this.sdqTestTime = timeStr;
	}
	
	@Override
	public boolean equals(Object o) {
		return ((sdqTestLogId==((SdqTestLog)o).sdqTestLogId)&&(userId==((SdqTestLog)o).userId)&&(sdqTestDate.equals(((SdqTestLog)o).sdqTestDate))&&(sdqTestTime.equals(((SdqTestLog)o).sdqTestTime)));
	}
}

