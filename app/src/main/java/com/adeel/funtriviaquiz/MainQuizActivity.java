package com.adeel.funtriviaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainQuizActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton, radioButton2, radioButton3, radioButton4;
    TextView textViewQuestionNumber;
    TextView textViewQuestionDescription;

    String correctAnswer;
    int questionNumber, totalScore;

    MediaPlayer soundClip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);

        // Get references to Question Number and Question Description
        textViewQuestionNumber = (TextView) findViewById(R.id.textViewQuestionNumber);
        textViewQuestionDescription = (TextView) findViewById(R.id.textViewQuestionDescription);

        // Get references of all four radio buttons so that their text may be updated dynamically
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);

        // Initialize the question number and total score since this is the first run
        questionNumber = 1;
        totalScore = 0;

        // Get current question number and append it to the question number textView
        textViewQuestionNumber.setText(getString(R.string.question_number, questionNumber));

        // TODO: Add more questions to JSON file. At least 50 questions to keep things interesting.
        // TODO: Get randomized 10 questions from the JSON file and display to user.
        // TODO: Update the question number each time after user submits an answer.
        // TODO: Ensure randomized questions don't repeat.
        // TODO: Create an activity that displays the total score at the end of each round.

        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset(getApplicationContext()));

            JSONArray jsonArrayQuestion =  jsonObject.getJSONArray("questions");
            JSONObject questionJSONObject  = jsonArrayQuestion.getJSONObject(0);

            String test = questionJSONObject.getString("description");

            textViewQuestionDescription.setText(test);
            textViewQuestionDescription.setGravity(Gravity.CENTER);

            JSONObject answersJSONObject = questionJSONObject.getJSONObject("answers");

            // Set the text of all four radio buttons with the answers obtained from JSON File
            radioButton.setText(answersJSONObject.getString("option1"));
            radioButton2.setText(answersJSONObject.getString("option2"));
            radioButton3.setText(answersJSONObject.getString("option3"));
            radioButton4.setText(answersJSONObject.getString("option4"));

            correctAnswer = answersJSONObject.getString("answer");

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }



        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedRadioButtonId) {
                // Get the reference of the submitAnswer button so that it may be
                // enabled once user selects any option
                Button submitAnswer = (Button) findViewById(R.id.buttonSubmitAnswer);
                if (!submitAnswer.isEnabled()) submitAnswer.setEnabled(true);
            }
        });
    }

    public String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream inputStream = context.getAssets().open("quiz-ques-ans.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            json = new String(buffer, "UTF-8");
        }
        catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }

        return json;
    }


    public void submitAnswer(View view) {
        // Get the reference to the radio button that was selected by the user
        RadioButton selectedAnswer = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        if (selectedAnswer.getText().equals(correctAnswer)) {
            // Match the answer and see if it is correct
            soundClip = MediaPlayer.create(this, R.raw.successsound);
            soundClip.start();
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
            totalScore += 10;
            Toast.makeText(getApplicationContext(), String.valueOf(totalScore), Toast.LENGTH_SHORT).show();
        }
        else {
            soundClip = MediaPlayer.create(this, R.raw.failsound);
            soundClip.start();
            Toast.makeText(getApplicationContext(), "Wrong!", Toast.LENGTH_SHORT).show();
        }
    }
}