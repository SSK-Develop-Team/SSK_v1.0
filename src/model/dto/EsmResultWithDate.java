 package model.dto;

import java.sql.Date;
/**
 * @author leejiwon
 * 정서 반복 기록의 응답을 기반으로 평균 낸 긍정 점수(positiveAvg)와 부정 점수(negativeAvg), 날짜(date)를 나타내는 data type
 * 긍정, 부정 점수의 자료형은 기본적으로 Integer형이지만, 일별 평균 점수 계산 시 Float형이 나올 수 있으므로, Float형으로 지정함.
 */
public class EsmResultWithDate {
	private Date date;
	private float positiveAvg;
	private float negativeAvg;
	public EsmResultWithDate(Date date, float positiveAvg, float negativeAvg) {
		super();
		this.date = date;
		this.positiveAvg = positiveAvg;
		this.negativeAvg = negativeAvg;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public float getPositiveAvg() {
		return positiveAvg;
	}
	public void setPositiveAvg(float positiveAvg) {
		this.positiveAvg = positiveAvg;
	}
	public float getNegativeAvg() {
		return negativeAvg;
	}
	public void setNegativeAvg(float negativeAvg) {
		this.negativeAvg = negativeAvg;
	}
	
	@Override
	public boolean equals(Object o){
		return ( date.equals(((EsmResultWithDate)o).date) && positiveAvg == ((EsmResultWithDate)o).positiveAvg && negativeAvg == ((EsmResultWithDate)o).negativeAvg);
	}
}
