package com.example.lab01_hw_23520347;

import androidx.annotation.NonNull;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.google.gson.JsonObject;
import java.io.IOException;

public class SentimentAnalyzer {

    // Đây là URL API của Hugging Face
    private static final String API_URL = "https://api-inference.huggingface.co/models/lxyuan/distilbert-base-multilingual-cased-sentiments-student";
    private static final String API_TOKEN = "Bearer hf_xxxxxxxxxxxxxxxxxxxxxxxxxxx";

    public static void analyzeSentiment(String textToAnalyze) {
        OkHttpClient client = new OkHttpClient();

        // Xây dựng chuỗi JSON theo yêu cầu của Hugging Face
        JsonObject jsonBody = new JsonObject();
        jsonBody.addProperty("inputs", textToAnalyze);

        RequestBody body = RequestBody.create(
                jsonBody.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        // Tạo Request gửi đi
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", API_TOKEN)
                .post(body)
                .build();

        // Gửi Request ngầm ở một Thread (luồng) phụ để không làm đơ ứng dụng
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    System.out.println("Kết quả phân tích: " + responseData);
                    // Dữ liệu in ra ở cửa sổ Logcat của Android Studio (chọn tab Logcat bên dưới để xem)
                } else {
                    System.out.println("Lỗi gọi API: " + response.code() + " - " + response.message());
                }
            }
        });
    }
}