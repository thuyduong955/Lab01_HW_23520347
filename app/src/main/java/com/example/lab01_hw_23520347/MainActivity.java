package com.example.lab01_hw_23520347;

import android.graphics.Color;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private MaterialButton submitButton;
    private TextView emojiText;
    private TextView titleText;
    private ProgressBar progressBar;
    private View mainLayout;

    // Nhớ thay bằng API Key thật ở Google AI Studio khi chạy trên máy!
    private static final String GEMINI_API_KEY = "YOUR_GEMINI_API_KEY";
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + GEMINI_API_KEY;

    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        mainLayout = findViewById(R.id.main);
        inputText = findViewById(R.id.inputText);
        submitButton = findViewById(R.id.submitButton);
        emojiText = findViewById(R.id.emojiText);
        titleText = findViewById(R.id.titleText);
        progressBar = findViewById(R.id.progressBar);

        ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setNeutral();

        submitButton.setOnClickListener(v -> {
            String text = inputText.getText().toString().trim();
            if (text.isEmpty()) {
                Toast.makeText(this, "Please enter some text", Toast.LENGTH_SHORT).show();
                return;
            }
            analyzeSentiment(text);
        });

        // Tự động submit khi ấn phím Enter trên bàn phím ảo
        inputText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO ||
                    (event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                submitButton.performClick();
                return true;
            }
            return false;
        });
    }

    private void analyzeSentiment(String prompt) {
        progressBar.setVisibility(View.VISIBLE);
        emojiText.setVisibility(View.GONE);
        submitButton.setEnabled(false);

        // Tạo câu lệnh (prompt) ngắn gọn để ép hệ thống chỉ trả về 1 từ
        String instruction = "Phân tích cảm xúc của câu sau và chỉ trả về ĐÚNG MỘT TỪ trong 3 từ này: POSITIVE, NEGATIVE, hoặc NEUTRAL.\nCâu cần phân tích: " + prompt;

        try {
            // Xây dựng JSON Body theo chuẩn của Gemini API
            JSONObject jsonBody = new JSONObject();
            JSONArray contentsArray = new JSONArray();
            JSONObject contentsObj = new JSONObject();
            JSONArray partsArray = new JSONArray();
            JSONObject textObj = new JSONObject();

            textObj.put("text", instruction);
            partsArray.put(textObj);
            contentsObj.put("parts", partsArray);
            contentsArray.put(contentsObj);
            jsonBody.put("contents", contentsArray);

            RequestBody body = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.parse("application/json; charset=utf-8")
            );

            // Tạo Request gửi tới Gemini
            Request request = new Request.Builder()
                    .url(GEMINI_API_URL)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        emojiText.setVisibility(View.VISIBLE);
                        submitButton.setEnabled(true);
                        Toast.makeText(MainActivity.this, "Network Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    String responseBody = response.body() != null ? response.body().string() : "";

                    if (response.isSuccessful() && !responseBody.isEmpty()) {
                        try {
                            // API Gemini trả về JSON có cấu trúc candidates -> content -> parts -> text
                            JSONObject jsonResponse = new JSONObject(responseBody);
                            String sentiment = jsonResponse
                                    .getJSONArray("candidates")
                                    .getJSONObject(0)
                                    .getJSONObject("content")
                                    .getJSONArray("parts")
                                    .getJSONObject(0)
                                    .getString("text").trim().toUpperCase();

                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                emojiText.setVisibility(View.VISIBLE);
                                submitButton.setEnabled(true);
                                updateUI(sentiment);
                            });
                        } catch (Exception e) {
                            runOnUiThread(() -> {
                                progressBar.setVisibility(View.GONE);
                                emojiText.setVisibility(View.VISIBLE);
                                submitButton.setEnabled(true);
                                Toast.makeText(MainActivity.this, "Lỗi phân tích JSON", Toast.LENGTH_SHORT).show();
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            progressBar.setVisibility(View.GONE);
                            emojiText.setVisibility(View.VISIBLE);
                            submitButton.setEnabled(true);
                            Toast.makeText(MainActivity.this, "API Error " + response.code(), Toast.LENGTH_LONG).show();
                        });
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
            emojiText.setVisibility(View.VISIBLE);
            submitButton.setEnabled(true);
        }
    }

    private void updateUI(String sentiment) {
        if (sentiment.contains("POSITIVE")) {
            mainLayout.setBackgroundColor(Color.parseColor("#DCFFB7"));
            emojiText.setText("😃");
            setLightModeColors();
        } else if (sentiment.contains("NEGATIVE")) {
            mainLayout.setBackgroundColor(Color.parseColor("#FF6868"));
            emojiText.setText("☹️");
            setDarkModeColors();
        } else {
            setNeutral();
        }
    }

    private void setNeutral() {
        mainLayout.setBackgroundColor(Color.parseColor("#FFEAA7"));
        emojiText.setText("😐");
        setLightModeColors();
    }

    private void setLightModeColors() {
        // Nền sáng (vàng, xanh) -> Chữ Title đen, Nút gạch viền đen/chữ đen
        titleText.setTextColor(Color.BLACK);
        submitButton.setTextColor(Color.BLACK);
        submitButton.setStrokeColor(ColorStateList.valueOf(Color.BLACK));
        inputText.setTextColor(Color.BLACK);
        inputText.setHintTextColor(Color.DKGRAY);
    }

    private void setDarkModeColors() {
        // Nền tối (đỏ) -> Chữ Title trắng, Nút gạch viền trắng/chữ trắng
        titleText.setTextColor(Color.WHITE);
        submitButton.setTextColor(Color.WHITE);
        submitButton.setStrokeColor(ColorStateList.valueOf(Color.WHITE));
        inputText.setTextColor(Color.BLACK); // Edit text nền trắng nên chữ vẫn cần màu đen
        inputText.setHintTextColor(Color.DKGRAY);
    }
}
