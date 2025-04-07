package alarm.firebase;

import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

public class FCMUtil {
    public static void sendNotification(String token, String title, String body) {
        try {
            // Notification 객체 생성
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            // ApnsConfig에 sound 설정 추가
            ApnsConfig apnsConfig = ApnsConfig.builder()
                    .setAps(Aps.builder()
                            .setSound("default") // iOS 기본 알림 소리 설정
                            .build())
                    .build();
            // Message 객체 생성
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .setApnsConfig(apnsConfig)
                    .build();

            // 메시지 전송
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
