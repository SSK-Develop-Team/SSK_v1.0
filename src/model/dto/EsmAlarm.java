package model.dto;


import java.sql.Time;
import java.sql.Date;

public class EsmAlarm {
	
	private int alarmId;
	private int userId;
	private Time alarmStartTime;
	private Time alarmEndTime;
	private Date alarmStartDate;
	private Date alarmEndDate;
	private int alarmInterval;
	
	public EsmAlarm() {

	}
	
	public EsmAlarm(Time alarmStartTime, Time alarmEndTime, 
					Date alarmStartDate, Date alarmEndDate, int alarmInterval) {
		super();
		this.alarmStartTime=alarmStartTime;
		this.alarmEndTime=alarmEndTime;
		this.alarmStartDate=alarmStartDate;
		this.alarmEndDate=alarmEndDate;
		this.alarmInterval=alarmInterval;
	}
	
	public Time getAlarmStartTime() {
		return alarmStartTime;
	}
	public void setAlarmStartTime(Time alarmStartTime) {
		this.alarmStartTime = alarmStartTime;
	}
	
	public Time getAlarmEndTime() {
		return alarmEndTime;
	}
	public void setAlarmEndTime(Time alarmEndTime) {
		this.alarmEndTime = alarmEndTime;
	}
	public Date getAlarmStartDate() {
		return alarmStartDate;
	}
	public void setAlarmStartDate(Date alarmStartDate) {
		this.alarmStartDate = alarmStartDate;
	}
	
	public Date getAlarmEndDate() {
		return alarmEndDate;
	}
	public void setAlarmEndDate(Date alarmEndDate) {
		this.alarmEndDate = alarmEndDate;
	}
	public int getAlarmInterval() {
		return alarmInterval;
	}
	public void setAlarmInterval(int alarmInterval) {
		this.alarmInterval = alarmInterval;
	}

	public int getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(int alarmId) {
		this.alarmId = alarmId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}