package alarm.firebase;

import java.io.FileInputStream;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;


public class FirebaseInitializer {
    private static boolean initialized = false;

    public static void initialize() {
        if (initialized) {
            return;
        }
        try {
            // 서비스 계정 키 파일 경로
            String serviceAccountKeyPath = "/Users/Admin/Desktop/honglabweb/SSK-run/SSK/ssk/src/alarm/firebase/service-account.json";

            FileInputStream serviceAccount = new FileInputStream(serviceAccountKeyPath);

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options);
            initialized = true; // 초기화 플래그 설정
            System.out.println("Firebase가 성공적으로 초기화되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Firebase 초기화 중 오류 발생: " + e.getMessage());
        }
    }
}
