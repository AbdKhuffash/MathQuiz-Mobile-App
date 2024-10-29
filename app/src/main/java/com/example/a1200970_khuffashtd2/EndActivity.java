package com.example.a1200970_khuffashtd2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        ListView topScoresList = findViewById(R.id.topScoresList);
        TextView  totalPlayersTV = findViewById(R.id.totalPlayersLabel);
        TextView averageScoreTv = findViewById(R.id.averageScoreTextView);
        TextView highestScoreTV = findViewById(R.id.highestScoreTextView);
        EditText usernameET = findViewById(R.id.unsernameEditText);
        Button findScore=findViewById(R.id.getPlayerScoreButton);
        Button goBack=findViewById(R.id.goBackButton);

        DataBaseHelper db = new DataBaseHelper(EndActivity.this, "ToDo2", null, 1);

        List<String> topScores = db.getTop5Scores();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, topScores);
        topScoresList.setAdapter(adapter);

        totalPlayersTV.setText("Total Player Count: "+String.valueOf(db.getPlayersCount()));

        findScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(getCurrentFocus());
                String username=usernameET.getText().toString().trim().toUpperCase();
                Log.d("DataBaseHelper", " ====================ea " + username);
                usernameET.setText("The Score for  "+ username+ " is: "+ db.getScoreForPlayer(username));
            }
        });

        averageScoreTv.setText("The AVG Score: "+String.valueOf(db.getAverageScore()));
        highestScoreTV.setText("Highes Score Player Information: "+db.getHighestScore());

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EndActivity.this, MainActivity.class);
                EndActivity.this.startActivity(intent);
                usernameET.setText("");

            }
        });




    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}