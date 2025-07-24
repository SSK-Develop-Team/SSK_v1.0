package alarm;

import java.sql.Connection;
import java.util.Set;
import java.util.List;

import alarm.firebase.FirebaseInitializer;
import model.dao.EsmAlarmDAO;
import model.dto.EsmAlarm;

public class AlarmSchedulerService {
    public static void run(Connection conn) {
        FirebaseInitializer.initialize();
        try {
            // 1. DAO를 통해 알람 데이터 가져오기
            List<EsmAlarm> alarms = EsmAlarmDAO.getAllAlarms(conn);
            
            if ((alarms == null) || (alarms.size() == 0)){
                System.out.println("설정된 알람이 없습니다.");

            }else {
                // 2. 중복 없는 알람 시간 리스트 생성
                Set<String> alarmTimes = AlarmUtils.generateAlarmTimes(alarms);
                System.out.println("중복 없는 알람 시간 리스트 생성함");
                // 3. 작업 예약
                AlarmScheduler.scheduleAlarms(alarmTimes);

                System.out.println("알람 작업이 성공적으로 예약되었습니다.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("알람 스케줄링 중 오류 발생: " + e.getMessage());
        }
    }
}
