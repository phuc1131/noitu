package com.example.noitu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText edtInput;
    private TextView txtTimer, txtWordCount, txtScore, txtNextWord;
    private int score = 0, wordCount = 0;
    private String lastWord = "";
    private CountDownTimer timer;
    private long timeLeftInMillis = 30000;
    private boolean isTimerRunning = false;

    private DictionaryApi dictionaryApi;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtInput = findViewById(R.id.edtInput);
        txtTimer = findViewById(R.id.txtTimer);
        txtWordCount = findViewById(R.id.txtWordCount);
        txtScore = findViewById(R.id.txtScore);
        txtNextWord = findViewById(R.id.txtNextWord);
        Button btnEnter = findViewById(R.id.btnEnter);

        dictionaryApi = RetrofitClient.getInstance().create(DictionaryApi.class);

        btnEnter.setOnClickListener(view -> checkWord());

        edtInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkWord();
                return true;
            }
            return false;
        });
    }

    private void startTimer() {
        timer = new CountDownTimer(timeLeftInMillis, 1000) {
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {
                txtTimer.setText(millisUntilFinished / 1000 + "s");
                timeLeftInMillis = millisUntilFinished;
            }

            public void onFinish() {
                gameOver();
            }
        }.start();
        isTimerRunning = true;
    }

    private void checkWord() {
        String word = edtInput.getText().toString().trim();

        if (word.length() >= 3) {
            if (lastWord.isEmpty() || (word.startsWith(lastWord.substring(lastWord.length() - 1)) && word.length() >= lastWord.length())) {
                // Gọi API kiểm tra từ
                dictionaryApi.checkWord(word).enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            updateGame(word); // Nếu từ hợp lệ, cập nhật trò chơi
                        } else {
                            edtInput.setError("Từ không tồn tại trong từ điển!");
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Lỗi kết nối từ điển", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                edtInput.setError("Từ không hợp lệ!");
            }
        } else {
            edtInput.setError("Từ phải dài ít nhất 3 chữ cái!");
        }
    }

    private void updateGame(String word) {
        lastWord = word;
        txtNextWord.setText(lastWord);
        score += 5;
        wordCount++;
        txtScore.setText(String.valueOf(score));
        txtWordCount.setText(String.valueOf(wordCount));
        edtInput.setText("");
        edtInput.setHint("Nhập từ bắt đầu với: " + lastWord.charAt(lastWord.length() - 1));

        if (!isTimerRunning) {
            startTimer();  // Bắt đầu đếm ngược khi nhập từ đầu tiên
        } else {
            timer.cancel();
            startTimer();  // Reset lại đếm ngược mỗi khi nhập từ đúng
        }
    }

    private void gameOver() {
        Intent intent = new Intent(MainActivity.this, GameOverActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("wordCount", wordCount);
        startActivity(intent);
        finish();
    }
}
