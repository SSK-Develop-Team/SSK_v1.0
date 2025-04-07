package alarm;

import java.sql.Connection;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import alarm.firebase.FCMUtil;
import model.dao.EsmAlarmDAO;
import model.dao.EsmTestLogDAO;
import model.dao.UserTokenDAO;
import model.dto.EsmTestLog;
import util.DBConnection;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AlarmJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String alarmTime = context.getJobDetail().getJobDataMap().getString("alarmTime");
        System.out.println("=== 실행 중인 알람 시간: " + alarmTime + " ===");
        // Connection con = null;

        try (Connection con = DBConnection.getConnection()) {

            // 1. 해당 알람 시간에 매칭되는 사용자 ID 조회
            List<Integer> userIds = EsmAlarmDAO.getMatchingUsers(con, alarmTime);
            if (userIds.isEmpty()) {
                System.out.println("예약된 알람 시간과 일치하는 사용자가 없습니다.");
                return;
            }
            System.out.println("예약된 알람 시간과 일치하는 사용자 ID: " + userIds);

            // 2. 각 사용자에 대해 알람 조건 확인 및 알림 전송
            // EsmTestLogDAO testLogDAO = new EsmTestLogDAO();
            // UserTokenDAO tokenDAO = new UserTokenDAO();

            for (int userId : userIds) {
                // 2.1 최근 검사 기록 조회
                EsmTestLog recentLog = EsmTestLogDAO.getRecentEsmTestLogListByUserId(con, userId);

                // 디버깅 로그
                if (recentLog == null) {
                    System.out.println("최근 검사 기록 없음 - User ID: " + userId);
                } else {
                    System.out.println("최근 검사 기록 - User ID: " + userId +
                            ", TestDate: " + recentLog.getEsmTestDate() +
                            ", TestTime: " + recentLog.getEsmTestTime());
                }

                // 2.2 알람 조건 확인
                if (shouldSendAlarm(recentLog, alarmTime)) {
                    // 2.3 FCM 알림 전송
                    List<String> fcmTokens = UserTokenDAO.getUserTokens(con, userId);
                    if (fcmTokens.isEmpty()) {
                        System.out.println("사용자 ID:" + userId + "에 대한 FCM 토큰이 존재하지 않음");
                    } else {
                        for (String token : fcmTokens) {
                            sendFCMNotification(token, "PSLE", "검사할 시간입니다. 클릭하여 검사를 해주세요.");
                        }
                    }
                } else {
                    System.out.println("알림 조건에 부합하지 않음 - User ID: " + userId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // FCM 알림 전송
    private void sendFCMNotification(String token, String title, String body) {
        try {
            FCMUtil.sendNotification(token, title, body);
            System.out.println("FCM 알림 전송 성공: " + token);
        } catch (Exception e) {
            System.out.println("FCM 알림 전송 실패: " + token);
            e.printStackTrace();
        }
    }

    // 알람을 보내야 하는지 확인하는 메서드
    private boolean shouldSendAlarm(EsmTestLog recentLog, String alarmTime) {
        try {
            if (recentLog == null) {
                System.out.println("최근 기록 없음: 알람 전송 필요");
                return true;
            }
            // 알람 시간 파싱
            LocalTime alarmLocalTime = LocalTime.parse(alarmTime, DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalDate today = LocalDate.now(); // 오늘 날짜

            LocalDateTime alarmDateTime = LocalDateTime.of(today, alarmLocalTime); // 오늘의 알람 시간
            LocalDateTime lowerBound = alarmDateTime.minusMinutes(20); // 20분 전 시간

            // 최근 검사 기록 시간
            LocalDate recentLogDate = recentLog.getEsmTestDate().toLocalDate();
            LocalTime recentLogTime = recentLog.getEsmTestTime().toLocalTime();
            LocalDateTime recentLogDateTime = LocalDateTime.of(recentLogDate, recentLogTime);

            System.out.println("알람 시간: " + alarmLocalTime);
            System.out.println("최근 기록 시간: " + recentLogDateTime);
            System.out.println("20분 이전 시간: " + lowerBound);

            // 자정 경계 조건: 알람 시간이 자정을 포함하는 경우 어제의 기록도 허용
            // 자정 경계 조건 처리
            if (alarmLocalTime.isBefore(LocalTime.of(0, 20))) { // 알람이 00:00~00:19인 경우
                LocalDateTime yesterdayLowerBound = lowerBound.minusDays(1); // 어제의 20분 전 시간
                LocalDateTime yesterdayUpperBound = LocalDateTime.of(today.minusDays(1), LocalTime.of(23, 59, 59)); // 어제의
                                                                                                                    // 끝

                System.out.println("자정 조건 활성화: 어제 기록 허용 범위 확인");

                // 어제의 기록이 유효 범위 내에 있는지 확인
                boolean isYesterdayInRange = recentLogDateTime.isAfter(yesterdayLowerBound)
                        && recentLogDateTime.isBefore(alarmDateTime);

                // 오늘의 기록이 유효 범위 내에 있는지 확인
                boolean isTodayInRange = recentLogDateTime.isAfter(lowerBound)
                        && recentLogDateTime.isBefore(alarmDateTime);

                boolean isWithinRange = isYesterdayInRange || isTodayInRange;

                System.out.println("범위 내 기록 존재 여부: " + isWithinRange);
                return !isWithinRange; // 범위 내에 기록이 없으면 알림 전송
            }
            // 일반 조건: 오늘 날짜와 시간만 확인
            boolean isWithinRange = recentLogDateTime.isAfter(lowerBound) && recentLogDateTime.isBefore(alarmDateTime);
            System.out.println("최근 기록이 범위 내에 있음: " + isWithinRange);

            // boolean isWithinRange = recentLogLocalTime.isAfter(lowerBound)
            // && recentLogLocalTime.isBefore(alarmLocalTime);
            // System.out.println("최근 기록이 범위 내에 있음: " + isWithinRange);

            return !isWithinRange;
        } catch (Exception e) {
            System.out.println("시간 비교 중 오류 발생: " + e.getMessage());
            return false; // 예외가 발생하면 기본적으로 알람을 보내지 않음
        }
    }
}
