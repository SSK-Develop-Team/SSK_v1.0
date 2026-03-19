package integration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PythonChatClient {
    private final String baseUrl; // e.g. http://127.0.0.1:8000

    public PythonChatClient(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    // -------------------------
    // /chat
    // -------------------------
    public JsonObject chatBoot(String threadId, String message, String childName, Integer childAge, String childGender)
            throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("thread_id", threadId);
        body.addProperty("message", message);
        body.addProperty("booted", false);
        body.addProperty("child_name", childName == null ? "" : childName);
        if (childAge != null) body.addProperty("child_age", childAge);
        body.addProperty("child_gender", childGender == null ? "" : childGender);
        return postJson("/chat", body);
    }

    public JsonObject chatTurn(String threadId, String message) throws IOException {
        JsonObject body = new JsonObject();
        body.addProperty("thread_id", threadId);
        body.addProperty("message", message);
        body.addProperty("booted", true);
        return postJson("/chat", body);
    }

    // -------------------------
    // /finalize
    // -------------------------
    public JsonObject finalizeBook(String threadId,
                                   String childName, Integer childAge, String childGender,
                                   String storyLanguage, String storyElements, String storyMainTheme)
            throws IOException {

        JsonObject body = new JsonObject();
        body.addProperty("thread_id", threadId);
        body.addProperty("child_name", childName == null ? "" : childName);
        body.addProperty("child_age", childAge == null ? 0 : childAge);
        body.addProperty("child_gender", childGender == null ? "" : childGender);
        body.addProperty("story_language", storyLanguage == null ? "" : storyLanguage);
        body.addProperty("story_elements", storyElements == null ? "" : storyElements);
        body.addProperty("story_main_theme", storyMainTheme == null ? "" : storyMainTheme);

        return postJson("/finalize", body);
    }

    // -------------------------
    // internal
    // -------------------------
    public JsonObject postJson(String path, JsonObject body) throws IOException {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(baseUrl + path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(1_800_000); // finalize 로딩 시간(동화 생성, 이미지 생성, 부모가이드 생성)
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            byte[] payload = body.toString().getBytes(StandardCharsets.UTF_8);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload);
            }

            int code = conn.getResponseCode();
            InputStream is = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
            String responseText;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
                responseText = sb.toString();
            }

            if (code < 200 || code >= 300) {
                throw new IOException("FastAPI error: " + code + " " + responseText);
            }
            return JsonParser.parseString(responseText).getAsJsonObject();

        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}