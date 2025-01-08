package model.dto;


import java.sql.Time;

public class EsmAlarm {
	
	private int alarmId;
	private int userId;
	private Time alarmStart;
	private Time alarmEnd;
	private int alarmInterval;
	
	public EsmAlarm() {

	}
	
	public EsmAlarm(Time alarmStart, Time alarmEnd, int alarmInterval) {
		super();
		this.alarmStart=alarmStart;
		this.alarmEnd=alarmEnd;
		this.alarmInterval=alarmInterval;
	}
	
	public Time getAlarmStart() {
		return alarmStart;
	}
	public void setAlarmStart(Time alarmStart) {
		this.alarmStart = alarmStart;
	}
	
	public Time getAlarmEnd() {
		return alarmEnd;
	}
	public void setAlarmEnd(Time alarmEnd) {
		this.alarmEnd = alarmEnd;
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