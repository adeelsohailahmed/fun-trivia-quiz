package com.adeel.funtriviaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class ViewScoresActivity extends AppCompatActivity {

    SharedPreferences previousScores;
    SharedPreferences.Editor editor;
    ArrayAdapter<String> arrayAdapter;
    Map<String, ?> keys;

    ArrayList<String> listItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_scores);

        previousScores = getSharedPreferences("previousScores", Context.MODE_PRIVATE);

        ListView listViewScores = (ListView) findViewById(R.id.listViewScores);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listItems);
        listViewScores.setAdapter(arrayAdapter);

        // Get all entries from the previousScores file into the Map keys
        keys = previousScores.getAll();

        // Get the previous scores, if any
        getPreviousScores();

        // To create dummy data
//        editor = previousScores.edit();
//        editor.putInt("1", 42);
//        editor.putInt("2", 32);
//        editor.putInt("3", 22);
//        editor.putInt("4", 12);
//        editor.putInt("5", 02);
//        editor.commit();
    }

    public void getPreviousScores() {
        if (previousScores.contains("1")) {

            // If this is true, at least one score entry must exist in the file

            // Since using entrySet() of Map.Entry was not giving the keys in order when iterated,
            // the following can be used to manually iterate over all the values in order
            for (int i = 1; i <= keys.size(); i++) {
                // First, we cast index i to String as get() accepts String values
                // Then we cast the resultant value from any type (?) to Integer and add to arrayAdapter
                arrayAdapter.add(i + ".    " + keys.get(String.valueOf(i)));
            }
        }
        else {
            // Either the previousScores file doesn't exist
            // Or they have been cleared by user, so just show 0
            arrayAdapter.add("1.    0");
        }
    }

    public void clearPreviousScores(View view)
    {
        // Before allowing user to clear the previous scores, display a confirmation dialog box

        // First, register the OnClickListener. This will be attached to the AlertDialog.Builder obj
        DialogInterface.OnClickListener dOnClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int buttonClicked) {

                // User pressed 'Yes', thus clear all the previous scores
                if (buttonClicked == DialogInterface.BUTTON_POSITIVE) {
                    // Clears the whole file
                    editor = previousScores.edit();
                    editor.clear();
                    editor.commit();

                    // Clear the arrayAdapter so that the getPreviousScores function doesn't append to it
                    arrayAdapter.clear();
                    getPreviousScores();

                    // Show user Toast notification that scores have been cleared
                    Toast.makeText(getApplicationContext(), "Previous Scores have been cleared", Toast.LENGTH_SHORT).show();
                }

                // No need to do anything else since previous scores won't be cleared
                // in any other case.
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ViewScoresActivity.this);

        // The same OnClickListener object will be attached to both responses since it is handling
        // the both cases.
        builder.setMessage("Are you sure you want to clear your previous scores?")
                .setPositiveButton("Yes", dOnClickListener)
                .setNegativeButton("No", dOnClickListener)
                .show();
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