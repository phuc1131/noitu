package com.example.noitu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameOverActivity extends AppCompatActivity {

    private TextView txtScore, txtWordCount;
    private Button btnPlayAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        txtScore = findViewById(R.id.txtScore);
        txtWordCount = findViewById(R.id.txtWordCount);
        btnPlayAgain = findViewById(R.id.btnPlayAgain);

        Intent intent = getIntent();
        txtScore.setText("Score: " + intent.getIntExtra("score", 0));
        txtWordCount.setText("Words: " + intent.getIntExtra("wordCount", 0));

        btnPlayAgain.setOnClickListener(view -> {
            startActivity(new Intent(GameOverActivity.this, MainActivity.class));
            finish();
        });
    }
}
