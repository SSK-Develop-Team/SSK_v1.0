package util.process;

import model.dto.EsmAlarm;

import java.sql.Date;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EsmProcessor {
	/**
	 * 날짜에 해당하는 주의 모든 날짜 정보를 리스트로 받아오는 함수
	 * 시작 요일 선택 : 화요일 ~ 월요일, 수요일 ~ 화요일 등 사용자 선택에 따라 요일을 받아옴
	 * @param currDate, DayOfWeek(Sunday = 1, Monday = 2, ..., Saturday = 7)
	 * @return ArrayList<Date>
	 */
	public static ArrayList<Date> getDateListOfWeek(Date currDate, int dayOfWeek){
		ArrayList<Date> dateListOfWeek = new ArrayList<Date>();

		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);
		int sdayOfTheWeek = cal.get(Calendar.DAY_OF_WEEK);//현재 일자의 요일 번호

		//1. 현재 요일이 요구한 요일보다 앞인 경우 ex) 일요일(1), 목요일(5)
		if(sdayOfTheWeek < dayOfWeek ){
			for(int i=dayOfWeek-7 ; i<dayOfWeek ; i++) {
				if(i==sdayOfTheWeek) {
					dateListOfWeek.add(currDate);
				}else {
					cal.add(Calendar.DATE, i-sdayOfTheWeek);
					dateListOfWeek.add(new Date(cal.getTimeInMillis()));
					cal.add(Calendar.DATE, -i+sdayOfTheWeek);
				}
			}

		}else{//2. 현재 요일이 요구한 요일보다 뒤인 경우 ex) 수요일(4), 월요일(2)
			for(int i=dayOfWeek ; i<=dayOfWeek+6 ; i++) {

				if(i==sdayOfTheWeek) {
					dateListOfWeek.add(currDate);
				}else {
					cal.add(Calendar.DATE,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            i-sdayOfTheWeek);
					dateListOfWeek.add(new Date(cal.getTimeInMillis()));
					cal.add(Calendar.DATE, -i+sdayOfTheWeek);
				}
			}
		}

		return dateListOfWeek;
	}

	/**
	 * 사용자의 알람 설정 정보를 실제 알람 시간 리스트로 변환하는 함수 
	 *  : 알람 시작 시간, 알람 종료 시간, 간격을 기준으로 실제 알람 시각을 산출
	 * @param esmAlarms
	 * @return
	 */
	public static ArrayList<String> convertChildEsmAlarmList(ArrayList<EsmAlarm> esmAlarms){
		ArrayList<LocalTime> childEsmAlarmList = new ArrayList<>();
		ArrayList<String> childEsmAlarmStrList = new ArrayList<>();

		//사용자에게 설정된 알람 값이 없는 경우 -> 디폴트 알람 시간 값 설정
		if(esmAlarms == null || esmAlarms.size()==0){
			/*
			childEsmAlarmList.add(LocalTime.of(9,0));
			childEsmAlarmList.add(LocalTime.of(12,0));
			childEsmAlarmList.add(LocalTime.of(15,0));
			childEsmAlarmList.add(LocalTime.of(18,0));
			childEsmAlarmList.add(LocalTime.of(21,0));
			return childEsmAlarmList;
			 */
			childEsmAlarmStrList.add("09:00:00");
			childEsmAlarmStrList.add("12:00:00");
			childEsmAlarmStrList.add("15:00:00");
			childEsmAlarmStrList.add("18:00:00");
			childEsmAlarmStrList.add("21:00:00");

			return childEsmAlarmStrList;

		}

		//알람 리스트를 순회하면서 알람 시간을 변환하여 하나의 리스트로 add
		esmAlarms.stream().forEach(e ->{
			LocalTime startTime = e.getAlarmStart().toLocalTime();
			LocalTime endTime = e.getAlarmEnd().toLocalTime();
			Duration interval = Duration.ofHours(e.getAlarmInterval());
			childEsmAlarmList.addAll(convertTimeIntervalToTimeList(startTime, endTime, interval));
		});

		//String 리스트로 변환
		childEsmAlarmList.stream().forEach(e ->{
			childEsmAlarmStrList.add(e.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
		});

		return childEsmAlarmStrList;
	}


	/**
	 * 하나의 알람 설정 값을 알람 시간 리스트로 변환
	 * @param startTime
	 * @param endTime
	 * @param interval
	 * @return
	 */
	private static ArrayList<LocalTime> convertTimeIntervalToTimeList(LocalTime startTime, LocalTime endTime, Duration interval){
		ArrayList<LocalTime> timeListOfEsmTime = new ArrayList<>();

		LocalTime current = startTime;

		while (current.isBefore(endTime)) {
			timeListOfEsmTime.add(current);
			current = current.plus(interval);
		}

		return timeListOfEsmTime;
	}
/**
 * Example Code for getting day of week
 */

	/**
	 * 날짜의 요일 번호를 받아오는 함수(Sunday = 1, Monday = 2, ..., Saturday = 7)
	 * @param currDate
	 * @return 요일 번호
	 */
	public static int getDayOfTheWeek(Date currDate) {
		Calendar currCal = Calendar.getInstance();
		currCal.setTime(currDate);
		int dayOfTheWeek = currCal.get(Calendar.DAY_OF_WEEK);
		return dayOfTheWeek;
	}
	
	/**
	 * 날짜에 해당하는 주의 첫번째 날짜 정보(일요일)를 받아오는 함수
	 * @param currDate
	 * @return Date
	 */
	public static Date getStartDateOfWeek(Date currDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);
		int dayOfTheWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		cal.add(Calendar.DATE, 1-dayOfTheWeek);
		return new Date(cal.getTimeInMillis());
	}
	
	/**
	 * 날짜에 해당하는 주의 마지막 날짜 정보(토요일)를 받아오는 함수
	 * @param currDate
	 * @return Date
	 */
	public static Date getEndDateOfWeek(Date currDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(currDate);
		int dayOfTheWeek = cal.get(Calendar.DAY_OF_WEEK);
		
		cal.add(Calendar.DATE, 7-dayOfTheWeek);
		return new Date(cal.getTimeInMillis());
	}

}
