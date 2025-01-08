package model.dto;

import java.sql.Date;
/**
 * @author leejiwon
 * 정서 반복 기록
 * 주간 첫 기록 일자(date)와 해당 주를 나타내는 문자열(weekStr)을 나타낸 자료형
 * esmResult.jsp 페이지에서 드롭다운에 나타내기 위해 이용하는 data type
 * weekStr값만 일치하면 같은 값으로 취급
 */
public class EsmDateWeekType {
	private Date date;
	private String weekStr;
	
	public EsmDateWeekType(Date date, Date startDate, Date endDate) {
		super();
		this.date = date;
		this.weekStr = startDate.toString()+" ~ "+endDate.toString();
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getWeekStr() {
		return weekStr;
	}

	public void setWeekStr(Date startDate, Date endDate) {
		this.weekStr = startDate.toString()+" ~ "+endDate.toString();
	}
	
	@Override
	public boolean equals(Object o){
		return (weekStr.equals(((EsmDateWeekType)o).weekStr));
	}
}
