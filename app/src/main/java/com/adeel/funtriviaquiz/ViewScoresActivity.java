package com.adeel.funtriviaquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
    ArrayAdapter<Integer> arrayAdapter;
    Map<String, ?> keys;

    ArrayList<Integer> listItems = new ArrayList<>();

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
    }

    public void getPreviousScores() {
        if (previousScores.contains("1")) {

            // If this is true, at least one score entry must exist in the file

            // Since using entrySet() of Map.Entry was not giving the keys in order when iterated,
            // the following can be used to manually iterate over all the values in order
            for (int i = 1; i <= keys.size(); i++) {
                // First, we cast index i to String as get() accepts String values
                // Then we cast the resultant value from any type (?) to Integer and add to arrayAdapter
                arrayAdapter.add((Integer) keys.get(String.valueOf(i)));
            }
        }
        else {
            // Either the previousScores file doesn't exist
            // Or they have been cleared by user, so just show 0
            arrayAdapter.add(0);
        }
    }

    public void clearPreviousScores(View view)
    {
        // TODO: Show user a confirmation dialog box before clearing the previous scores

        // Show user Toast notification that scores have been cleared
        Toast.makeText(getApplicationContext(), "Previous Scores have been cleared", Toast.LENGTH_LONG).show();

        // Clears the whole file
        editor = previousScores.edit();
        editor.clear();
        editor.commit();

        // Clear the arrayAdapter so that the getPreviousScores function doesn't append to it
        arrayAdapter.clear();
        getPreviousScores();
    }
}