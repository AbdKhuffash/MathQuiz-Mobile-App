package com.example.a1200970_khuffashtd2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);



        Boolean[] questionResults = (Boolean[]) getIntent().getSerializableExtra("questionResults");
        Question[] selectedQuestions = (Question[]) getIntent().getSerializableExtra("selectedQuestions");
        User user = (User) getIntent().getSerializableExtra("user");

        TextView scoreTV=findViewById(R.id.scoreTV);
        scoreTV.setText("Score: "+String.valueOf(user.getScore()));
        Button nextBTn=findViewById(R.id.next);
        TextView question1=findViewById(R.id.question1);
        TextView question2=findViewById(R.id.question2);
        TextView question3=findViewById(R.id.question3);
        TextView question4=findViewById(R.id.question4);
        TextView question5=findViewById(R.id.question5);

        question1.setText(selectedQuestions[0].getQ());
        if (questionResults[0] != null && questionResults[0]) {
            question1.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            question1.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }

        question2.setText(selectedQuestions[1].getQ());
        if (questionResults[1] != null && questionResults[1]) {
            question2.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            question2.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }

        question3.setText(selectedQuestions[2].getQ());
        if (questionResults[2] != null && questionResults[2]) {
            question3.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            question3.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }

        question4.setText(selectedQuestions[3].getQ());
        if (questionResults[3] != null && questionResults[3]) {
            question4.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            question4.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }
        question5.setText(selectedQuestions[4].getQ());
        if (questionResults[4] != null && questionResults[4]) {
            question5.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            question5.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        }

        nextBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FeedbackActivity.this, EndActivity.class);
                FeedbackActivity.this.startActivity(intent);
            }
        });


    }
}