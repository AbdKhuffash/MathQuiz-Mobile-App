package com.example.a1200970_khuffashtd2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registerBTN=findViewById(R.id.Register);
        Button startBTN=findViewById(R.id.Start);
        EditText usernameET=findViewById(R.id.UserName);
        EditText emailET=findViewById(R.id.Email);
        EditText birthdateET=findViewById(R.id.BirthDate);

        DataBaseHelper db= new DataBaseHelper(MainActivity.this, "ToDo2" , null, 1);


        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=usernameET.getText().toString().trim().toUpperCase();
                String email=emailET.getText().toString().trim();
                String birthdate=birthdateET.getText().toString().trim();

                //checks if any of the fields is empty.
                if(username.isEmpty() || email.isEmpty() || birthdate.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidDate(birthdate)) {
                    Toast.makeText(MainActivity.this, "Please enter a valid date (yyyy-MM-dd)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isValidEmail(email)){
                    Toast.makeText(MainActivity.this, "Please enter a valid Email (example@gmail.com)", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user=new User(username,email,birthdate);

                if(db.registerUser(user)){
                    startBTN.setEnabled(true);
                    startBTN.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    hideKeyboard(getCurrentFocus());
                    startBTN.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.zoom_in_out));

                    Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                }
            }
        });

        startBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,QuizActivity.class);
                intent.putExtra("user", new User(usernameET.getText().toString().trim()
                        ,emailET.getText().toString().trim()
                        ,birthdateET.getText().toString().trim()));
                MainActivity.this.startActivity(intent);
                finish();
            }
        });
    }

    private boolean isValidDate(String date) {
        return date.matches("^(19|20)\\d{2}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])$");
    }
    public boolean isValidEmail(String email) {
        return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}