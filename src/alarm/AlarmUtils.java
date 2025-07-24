package alarm;

import java.sql.Time;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.dto.EsmAlarm;

public class AlarmUtils {
    public static Set<String> generateAlarmTimes(List<EsmAlarm> alarms) {
        Set<String> alarmTimes = new HashSet<>(); // 중복 제거를 위해 Set 사용

        for (EsmAlarm alarm : alarms) { // 알람 데이터 순회
            Time currentTime = alarm.getAlarmStartTime(); // 시작 시간
            Time endTime = alarm.getAlarmEndTime(); // 종료 시간
            int interval = alarm.getAlarmInterval(); // 간격 (시간 단위)

            if (interval <= 0) {
                System.out.println("Invalid interval: " + interval + ". Skipping alarm.");
                continue;
            }

            while (currentTime.before(endTime) || currentTime.equals(endTime)) {
                alarmTimes.add(currentTime.toString()); // 알람 시간 추가
                currentTime = incrementTime(currentTime, interval); // interval 만큼 시간 증가
                // 안전 장치: 최대 10000개의 시간 생성 후 중단
                if (alarmTimes.size() > 10000) {
                    System.out.println("Too many alarm times generated. Stopping early.");
                    break;
                }
            }
        }
        System.out.println("Generated alarm times: " + alarmTimes);
        return alarmTimes; // 생성된 알람 시간 반환
    }

    // 시간 증가 메서드
    private static Time incrementTime(Time time, int interval) {
        long millis = time.getTime() + interval * 60 * 60 * 1000; // interval 시간(밀리초로 계산) 추가
        return new Time(millis); // 새로운 시간 반환
    }
}
