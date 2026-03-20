# Android Sentiment Analysis App

Đây là một ứng dụng Android đơn giản sử dụng **Google Gemini API** (Mô hình `gemini-2.5-flash`) để phân tích cảm xúc của một đoạn văn bản (Tích cực - POSITIVE, Tiêu cực - NEGATIVE, hoặc Bình thường - NEUTRAL).

## 🚀 Hướng dẫn cài đặt và sử dụng API Key

Để ứng dụng này có thể hoạt động, bạn cần tạo một API Key từ Google và bổ sung vào mã nguồn. Hãy làm theo các bước dưới đây:

### Bước 1: Lấy Gemini API Key
1. Truy cập vào trang [Google AI Studio](https://aistudio.google.com/app/apikey).
2. Đăng nhập bằng tài khoản Google của bạn.
3. Nhấn vào nút **Create API Key**.
4. (Tùy chọn) Chọn một project hoặc tạo project mới, sau đó ấn "Create API key in existing project".
5. Copy đoạn mã được sinh ra (Nó trông giống thế này: `AIzaSy...`).

### Bước 2: Thêm API Key vào Android Studio
Mở file mã nguồn `MainActivity.java` theo đường dẫn sau:
`app/src/main/java/com/example/lab01_hw_23520347/MainActivity.java`

Tìm đến **dòng số 40**, thay thế giá trị `"YOUR_GEMINI_API_KEY"` bằng API key bạn vừa copy ở Bước 1.

```java
// Thay YOUR_GEMINI_API_KEY bằng API Key thật của bạn
private static final String GEMINI_API_KEY = "YOUR_GEMINI_API_KEY";
```

### Bước 3: Chạy ứng dụng
- Lưu lại file, kết nối máy ảo (Emulator) hoặc điện thoại Android và nhấn nút **Run** (▶️) để khởi chạy ứng dụng.
- Bạn có thể gõ văn bản và nhấn **Submit** hoặc nhấn phím **Enter** trên bàn phím ảo để phân tích.

---

*Lưu ý: Không bao giờ đẩy (commit) API Key thật của bạn lên GitHub một cách công khai để tránh bị lạm dụng hạn mức.*