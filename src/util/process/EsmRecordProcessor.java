package util.process;

import java.sql.Date;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * @author Lee Ji Won
 * ESM 기록 날짜 리스트를 json 형식의 event 배열로 변환 
 * 	예시) "events" : [{"start":"2022-09-23"},{"start":"2022-09-24"},{"start":"2022-09-26"}]
 **/
public class EsmRecordProcessor {
	public static JSONObject EsmRecordDateListToJSON(ArrayList<Date> esmRecordDateList) {
		JSONObject eventsJsonObject = new JSONObject();
		JSONArray date_array = new JSONArray();
		JSONObject date = null;
		
		for(int i =0;i<esmRecordDateList.size();i++) {//Date 삽입
			date = new JSONObject();
			date.put("start", esmRecordDateList.get(i).toString()+"T22:22:22");
			date_array.add(date);
		}
        
		eventsJsonObject.put("events", date_array);
		
		return eventsJsonObject;
	}
}