package com.adeel.funtriviaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainQuizActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton, radioButton2, radioButton3, radioButton4, selectedAnswer;
    Button submitAnswer;
    TextView textViewQuestionNumber;
    TextView textViewQuestionDescription;

    String correctAnswer;
    int questionNumber, totalNumberOfQuestions, totalScore;

    ArrayList<Integer> uniqueRandomNumbersList;

    MediaPlayer soundClip;
    JSONObject rootJSONObject;
    JSONArray jsonArrayQuestion;

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

        // Initialize the question number, total questions & total score since this is the first run
        questionNumber = 1;
        totalNumberOfQuestions = 10;
        totalScore = 0;

        // Get current question number and append it to the question number textView
        textViewQuestionNumber.setText(getString(R.string.question_number, questionNumber));

        // Read the contents of the local JSON Asset file once and save the root object for future
        try {
            rootJSONObject = new JSONObject(loadJSONFromAsset(getApplicationContext()));

            // Get the questions array from the root JSON object
             jsonArrayQuestion =  rootJSONObject.getJSONArray("questions");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Get the reference to the radioGroup and enable the initially disabled submit button
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedRadioButtonId) {
                // Get the reference of the submitAnswer button so that it may be
                // enabled once user selects any option
                submitAnswer = (Button) findViewById(R.id.buttonSubmitAnswer);
                if (!submitAnswer.isEnabled()) submitAnswer.setEnabled(true);
            }
        });

        uniqueRandomNumbersList = getUniqueRandomNumbersList(jsonArrayQuestion.length(), totalNumberOfQuestions);

        // Fetch Question along with all its options and correct answer
        fetchQuestionFromJSONAssetFile(uniqueRandomNumbersList.get(questionNumber - 1));
    }

    public ArrayList<Integer> getUniqueRandomNumbersList(int arrayLength, int totalNumberOfQuestions) {

        // Divide the arrayLength with total number of questions to get proper decrement size.
        int decrementSize = arrayLength / totalNumberOfQuestions;

        ArrayList<Integer> randomNumbersList = new ArrayList<>();
        ArrayList<Integer> previousNumbersList = new ArrayList<>();

        for (int i = arrayLength; i > 0; i -= decrementSize) {
            // Generate a random number between 0 and arrayLength - 1
            int random = new Random().nextInt(arrayLength);

            // Since the loop has just started, simply add it to previous numbers' list and let it be
            if (i == arrayLength) {
                randomNumbersList.add(random);
                previousNumbersList.add(random);
                continue;
            }
            if (previousNumbersList.contains(random)) {
                // Keep generating random numbers till a number is reached that is unique to
                // the entire list
                while (previousNumbersList.contains(random)) {
                    random = new Random().nextInt(arrayLength);
                }
            }
            randomNumbersList.add(random);
            previousNumbersList.add(random);
        }
        return randomNumbersList;
    }

    public void fetchQuestionFromJSONAssetFile (int index) {
        try {
            // Get a question JSON object by specifying its index in the JSONArray
            JSONObject questionJSONObject  = jsonArrayQuestion.getJSONObject(index);

            // Fetch the description of the question and set it to Question Description textView
            textViewQuestionDescription.setText(questionJSONObject.getString("description"));
            textViewQuestionDescription.setGravity(Gravity.CENTER); // Center the question

            // Get the answers JSON object which contains all the options and correct answer
            JSONObject answersJSONObject = questionJSONObject.getJSONObject("answers");

            // Set the text of all four radio buttons with the answers obtained from JSON File
            radioButton.setText(answersJSONObject.getString("option1"));
            radioButton2.setText(answersJSONObject.getString("option2"));
            radioButton3.setText(answersJSONObject.getString("option3"));
            radioButton4.setText(answersJSONObject.getString("option4"));

            // Get the correct answer from the answersObject and save it to correctAnswers
            correctAnswer = answersJSONObject.getString("answer");

        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }
    }
    // The method to read the local JSON Asset file and retrieve its content
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

    // Submit the answer checked by the user
    public void submitAnswer(View view) {
        // Get the reference to the radio button that was selected by the user
        selectedAnswer = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        // Disable the submit answer button
        submitAnswer.setEnabled(false);

        // Match the answer and see if it is correct
        if (selectedAnswer.getText().equals(correctAnswer)) {
            soundClip = MediaPlayer.create(this, R.raw.successsound);
            soundClip.start();
            Toast.makeText(getApplicationContext(), "Correct!", Toast.LENGTH_SHORT).show();
            totalScore += 10; // Add 10 to total score since correct option was selected
        } else {
            soundClip = MediaPlayer.create(this, R.raw.failsound);
            soundClip.start();
            Toast.makeText(getApplicationContext(), "Wrong!", Toast.LENGTH_SHORT).show();
        }

        // Wait for 2 seconds, uncheck the selected answer and then get the next question
        CountDownTimer countDownTimer = new CountDownTimer(2500, 2500) {
            @Override
            public void onTick(long l) {
                // Nothing needed to be done here
            }

            @Override
            public void onFinish() {
                selectedAnswer.setChecked(false);

                // If the current question is the final question, proceed to new activity
                if (questionNumber == totalNumberOfQuestions) {
                    Intent finishScreen = new Intent();
                    finishScreen.setClass(getApplicationContext(), FinalScoreActivity.class);
                    finishScreen.putExtra("totalScore", totalScore);
                    startActivity(finishScreen);
                    finish();
                }
                else {
                    // Increment the questionNumber
                    questionNumber++;
                    // Update the question number
                    textViewQuestionNumber.setText(getString(R.string.question_number, questionNumber));
                    // Get next question
                    fetchQuestionFromJSONAssetFile(uniqueRandomNumbersList.get(questionNumber - 1));
                }
            }
        };
        countDownTimer.start();
    }
}