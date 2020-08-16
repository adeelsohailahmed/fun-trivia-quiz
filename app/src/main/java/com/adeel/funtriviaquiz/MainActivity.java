package com.adeel.funtriviaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void exit (View view) {
        finish(); // Close the app.
    }

    public void viewPreviousScores(View view) {
        Intent viewPreviousScores = new Intent();
        viewPreviousScores.setClass(this, ViewScoresActivity.class);
        startActivity(viewPreviousScores);
    }

    public void startQuiz(View view) {
        Intent startQuiz = new Intent();
        startQuiz.setClass(this, MainQuizActivity.class);
        startActivity(startQuiz);
        finish();
    }
}