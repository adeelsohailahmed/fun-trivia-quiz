package com.adeel.funtriviaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class FinalScoreActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_score);

        // Get the final score from the previous activity
        Intent caller = getIntent();
        int totalScore = caller.getIntExtra("totalScore", 0);

        // Get the reference to the final score textView and display to the user
        TextView textViewFinalScore = (TextView) findViewById(R.id.textViewFinalScore);
        textViewFinalScore.setText(getString(R.string.final_score, totalScore));

        // Also save this final score in previousScores file
        SharedPreferences previousScores = getSharedPreferences("previousScores", MODE_PRIVATE);
        SharedPreferences.Editor editor = previousScores.edit();

        // If previousScores has at least 1 previous score, then:
        if (previousScores.contains("1")) {
            Map<String, ?> keys = previousScores.getAll();
            int nextItemNumber = keys.size() + 1;

            editor.putInt(String.valueOf(nextItemNumber), totalScore);
        }
        // Otherwise, just put data manually.
        else {
            editor.putInt("1", totalScore);
        }
        editor.commit();
    }

    // Play the quiz again
    public void playAgain(View view) {
        Intent playAgain = new Intent();
        playAgain.setClass(this, MainQuizActivity.class);
        startActivity(playAgain);
        finish();
    }

    // View your previous scores
    public void viewPrevScores (View view) {
        Intent viewPrevScores = new Intent();
        viewPrevScores.setClass(this, ViewScoresActivity.class);
        startActivity(viewPrevScores);
        finish();
    }


    // Go back to the main activity if user has pressed the back button
    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent();
        mainActivity.setClass(this, MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}