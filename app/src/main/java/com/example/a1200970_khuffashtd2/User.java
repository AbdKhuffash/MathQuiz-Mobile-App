package com.example.a1200970_khuffashtd2;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    private int Id;
    private String UserName;
    private String Email;
    private String BirthDate;
    private int Score;

    public User() {

    }

    public User(String userName, String email, String birthDate) {
        UserName = userName;
        Email = email;
        BirthDate = birthDate;
        this.Score=0;
    }

    public String getUserName() {
        return UserName;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public String getEmail() {
        return Email;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {
        Score = score;
    }
}
