package com.adeel.funtriviaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainQuizActivity extends AppCompatActivity {

    RadioGroup radioGroup;
    RadioButton radioButton, radioButton2, radioButton3, radioButton4;
    TextView textViewQuestionNumber;
    TextView textViewQuestionDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_quiz);

        textViewQuestionNumber = (TextView) findViewById(R.id.textViewQuestionNumber);
        textViewQuestionDescription = (TextView) findViewById(R.id.textViewQuestionDescription);

        textViewQuestionNumber.setText(textViewQuestionNumber.getText().toString() + "1");

        // TODO: Read from json asset file and parse it with json object.
        // TODO: Set description and answers via json object.


        textViewQuestionDescription.setText("What is the capital of France?");

        // Get references of all four radio buttons so that their text may be updated dynamically
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton4 = (RadioButton) findViewById(R.id.radioButton4);

        // Set the text of all four radio buttons with the answers obtained from JSON File
        radioButton.setText("France");
        radioButton2.setText("London");
        radioButton3.setText("Rome");
        radioButton4.setText("Belgium");

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

    public void submitAnswer(View view) {
        // Get the reference to the radio button that was selected by the user
        RadioButton selectedAnswer = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());

        // Match the answer and see if it is correct
        Toast.makeText(getApplicationContext(), selectedAnswer.getText(), Toast.LENGTH_SHORT).show();
    }
}