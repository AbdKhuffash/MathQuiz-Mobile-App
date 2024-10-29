package com.example.a1200970_khuffashtd2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizActivity extends AppCompatActivity {

    private TextView infoTV, questionTV, timerTV;
    private Button option1, option2, option3, option4;
    private List<Question> selectedQuestions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private CountDownTimer timer;
    private User user;

    private List<Boolean> questionResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        user = (User) getIntent().getSerializableExtra("user");

        infoTV = findViewById(R.id.info);
        questionTV = findViewById(R.id.Question);
        timerTV = findViewById(R.id.timer);
        option1 = findViewById(R.id.option1);
        option2 = findViewById(R.id.option2);
        option3 = findViewById(R.id.option3);
        option4 = findViewById(R.id.option4);

        List<Question> questions = readCSV();
        selectedQuestions = selectRandomQuestions(questions, 5);

        questionResults = new ArrayList<>(Collections.nCopies(selectedQuestions.size(), null));

        infoTV.setText(user.getUserName().toUpperCase() + " : " + score);

        displayQuestion();
    }

    private void displayQuestion() {
        if (currentQuestionIndex < selectedQuestions.size()) {
            Question currentQuestion = selectedQuestions.get(currentQuestionIndex);
            questionTV.setText(currentQuestion.getQ());

            List<Button> options = new ArrayList<>();
            options.add(option1);
            options.add(option2);
            options.add(option3);
            options.add(option4);
            Collections.shuffle(options);

            options.get(0).setText(currentQuestion.getA());
            options.get(1).setText(String.valueOf(Integer.parseInt(currentQuestion.getA()) + 1));
            options.get(2).setText(String.valueOf(Integer.parseInt(currentQuestion.getA()) - 1));
            options.get(3).setText(String.valueOf(Integer.parseInt(currentQuestion.getA()) + 2));

            for (Button option : options) {
                option.setOnClickListener(view -> checkAnswer((Button) view, currentQuestion.getA()));
            }

            startTimer();
        } else { // Quiz ended
            user.setScore(score);
            infoTV.setText(user.getUserName().toUpperCase() + " : " + score);
            DataBaseHelper db = new DataBaseHelper(QuizActivity.this, "ToDo2" , null, 1);
            db.updateUserScore(user.getUserName().toUpperCase(), score);

            Log.d("QuizActivity", "==================" + score);
            Intent intent = new Intent(QuizActivity.this, FeedbackActivity.class);
            intent.putExtra("questionResults", questionResults.toArray(new Boolean[0]));
            intent.putExtra("selectedQuestions", selectedQuestions.toArray(new Question[0]));
            intent.putExtra("user", user);
            QuizActivity.this.startActivity(intent);
        }
    }

    private void startTimer() {
        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTV.setText("Time left: " + millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {
                Toast.makeText(QuizActivity.this, "Time is UP!!", Toast.LENGTH_SHORT).show();
                score--;
                infoTV.setText(user.getUserName().toUpperCase() + " : " + score);
                currentQuestionIndex++;
                displayQuestion();
            }
        };
        timer.start();
    }

    private void checkAnswer(Button selectedOption, String correctAnswer) {
        if (timer != null) {
            timer.cancel();
        }

        boolean isCorrect = selectedOption.getText().toString().equals(correctAnswer);

        if (isCorrect) {
            selectedOption.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            score++;
            infoTV.setText(user.getUserName().toUpperCase() + " : " + score);
            questionResults.set(currentQuestionIndex, true);
            Toast.makeText(QuizActivity.this, "Correct Answer!!", Toast.LENGTH_SHORT).show();

        } else {
            selectedOption.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            score--;
            infoTV.setText(user.getUserName().toUpperCase() + " : " + score);
            questionResults.set(currentQuestionIndex, false);
            Toast.makeText(QuizActivity.this, "Wrong Answer!!", Toast.LENGTH_SHORT).show();

        }

        // delay 1s before moving to the next question
        new Handler().postDelayed(() -> {
            option1.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            option2.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            option3.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            option4.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            currentQuestionIndex++;
            displayQuestion();
        }, 1000);
    }

    private List<Question> readCSV() {
        List<Question> questions = new ArrayList<>();
        BufferedReader reader = null;

        try {
            InputStream is = getAssets().open("Questions.csv");
            reader = new BufferedReader(new InputStreamReader(is));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String question = parts[0].trim();
                    String answer = parts[1].trim();
                    questions.add(new Question(question, answer));
                } else {
                    Log.e("CSVReader", "Invalid line format: " + line);
                    Toast.makeText(QuizActivity.this, "Invalid line format: " + line, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (FileNotFoundException e) {
            Log.e("CSVReader", "Questions.csv file not found: " + e.getMessage());
            Toast.makeText(QuizActivity.this, "Questions file not found.", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("CSVReader", "Error reading Questions.csv: " + e.getMessage());
            Toast.makeText(QuizActivity.this, "Error reading questions.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("CSVReader", "Unexpected error: " + e.getMessage());
            Toast.makeText(QuizActivity.this, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("CSVReader", "Error closing the reader: " + e.getMessage());
                    Toast.makeText(QuizActivity.this, "Error closing the reader: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        return questions;
    }

    private List<Question> selectRandomQuestions(List<Question> questions, int n) {
        List<Question> selectedQuestions = new ArrayList<>();
        Set<Integer> selectedIndices = new HashSet<>();
        Random random = new Random();

        while (selectedQuestions.size() < n && selectedIndices.size() < questions.size()) {
            int index = random.nextInt(questions.size());
            if (!selectedIndices.contains(index)) {
                selectedQuestions.add(questions.get(index));
                selectedIndices.add(index);
            }
        }
        return selectedQuestions;
    }
}
