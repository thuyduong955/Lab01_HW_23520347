# Android Sentiment Analysis App

This is a simple Android application using the **Google Gemini API** (model `gemini-2.5-flash`) to analyze the sentiment of a piece of text (Positive, Negative, or Neutral).

## 🚀 Instructions for installing and using API Keys

For security reasons, personal API keys are not uploaded to the shared source code (GitHub). To run the application after cloning it, please set your own API key by:

### Step 1: Lấy Gemini API Key
1. Access website [Google AI Studio](https://aistudio.google.com/app/apikey).
2. Sign in with your Google account.
3. Click the button **Create API Key**.
4. Choose an existing Project (or create a new Project) and then copy the token code (starting with `AIzaSy...`).

### Step 2: Integrate the Key into the project.
Open the source code file `MainActivity.java` follow this:
`app/src/main/java/com/example/lab01_hw_23520347/MainActivity.java`

Find **line 40** and replace the value `"YOUR_GEMINI_API_KEY"` with the API key you copied in Step 1.

```java
// Thay YOUR_GEMINI_API_KEY bằng API Key thật của bạn
private static final String GEMINI_API_KEY = "YOUR_GEMINI_API_KEY";
```

### Bước 3: Chạy ứng dụng
- Save the file, connect to the emulator or your Android phone, and press the **Run** (▶️) button to launch the application.
- You can type text and press **Submit** or press the **Enter** key on the virtual keyboard for analysis.

---
### Fast prompt you can use instead of step 2
Open the `MainActivity.java` file and change the string `YOUR_GEMINI_API_KEY` to this API Key: `[PASTE THE API COPIED IN STEP 1 HERE]`