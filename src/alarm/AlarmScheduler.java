package alarm;

import java.util.Set;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

public class AlarmScheduler {
    public static void scheduleAlarms(Set<String> alarmTimes) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();

            // 기존 Job 삭제
            deleteExistingJobs(scheduler);

            for (String alarmTime : alarmTimes) {
                scheduleJob(scheduler, alarmTime);
                // System.out.println(alarmTime + " 알람 예약이 완료되었습니다.");
            }
            System.out.println("모든 알람 예약이 완료되었습니다.");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private static void deleteExistingJobs(Scheduler scheduler) {
        try {
            // 기존 Job 삭제
            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals("AlarmGroup"))) {
                scheduler.deleteJob(jobKey);
            }
        } catch (SchedulerException e) {
            System.err.println("기존 Job 삭제 중 오류 발생: " + e.getMessage());
        }
    }

    private static void scheduleJob(Scheduler scheduler, String alarmTime) throws SchedulerException {
        JobDetail job = JobBuilder.newJob(AlarmJob.class)
                .withIdentity("Job-" + alarmTime, "AlarmGroup")
                .usingJobData("alarmTime", alarmTime)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("Trigger-" + alarmTime, "AlarmGroup")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(
                        Integer.parseInt(alarmTime.substring(0, 2)), // 시
                        Integer.parseInt(alarmTime.substring(3, 5)) // 분
                ))
                .build();

        scheduler.scheduleJob(job, trigger);
    }
}
