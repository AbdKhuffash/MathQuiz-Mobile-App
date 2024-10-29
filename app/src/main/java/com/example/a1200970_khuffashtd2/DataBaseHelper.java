package com.example.a1200970_khuffashtd2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USER (ID INTEGER PRIMARY KEY AUTOINCREMENT, USERNAME TEXT, EMAIL TEXT, BIRTHDATE TEXT, SCORE INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM USER WHERE USERNAME = ?", new String[]{username.toUpperCase()});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public boolean registerUser(User user) {
        if (checkUsernameExists(user.getUserName())) {
            return false;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("USERNAME", user.getUserName().toUpperCase());
        values.put("EMAIL", user.getEmail());
        values.put("BIRTHDATE", user.getBirthDate().toString());
        values.put("SCORE",0);


        long result = db.insert("USER", null, values);
        return result != -1;
    }

    public void updateUserScore(String userName, int score) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String query = "UPDATE USER SET SCORE = ? WHERE USERNAME = ?";
        sqLiteDatabase.execSQL(query, new Object[]{score, userName.toUpperCase()});

        Log.d("DataBaseHelper", " ====================db " + userName);
        sqLiteDatabase.close();
    }


    public List<String> getTop5Scores() {
        List<String> topScores = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();


        String query = "SELECT USERNAME, SCORE FROM USER ORDER BY SCORE DESC LIMIT 5";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String username = cursor.getString(0);
                int score = cursor.getInt(1);
                topScores.add(username + ": " + score);
            } while (cursor.moveToNext());
        }

        cursor.close();
        sqLiteDatabase.close();

        return topScores;
    }

    public int getPlayersCount() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int playerCount = 0;

        String query = "SELECT COUNT(DISTINCT USERNAME) FROM USER";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            playerCount = cursor.getInt(0);
        }

        cursor.close();
        sqLiteDatabase.close();

        return playerCount;
    }

    public int getScoreForPlayer(String username) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int score=0;
        String query = "SELECT SCORE FROM USER WHERE USERNAME = ?";
        Log.d("DataBaseHelper", " ====================db " + username);

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            score = cursor.getInt(0);
        }
        cursor.close();
        sqLiteDatabase.close();

        return score;
    }

    public double getAverageScore() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        double averageScore = 0;

        String query = "SELECT AVG(SCORE) FROM USER";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            averageScore = cursor.getDouble(0);
        }

        cursor.close();
        sqLiteDatabase.close();

        return averageScore;
    }

    public String getHighestScore() {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        String highestScoreInfo = "";

        String query = "SELECT USERNAME, SCORE ,EMAIL FROM USER ORDER BY SCORE DESC LIMIT 1";
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            String username = cursor.getString(0);
            int score = cursor.getInt(1);
            String email=cursor.getString(2);
            highestScoreInfo = username + ": " + score + ": "+ email;
        }

        cursor.close();
        sqLiteDatabase.close();

        return highestScoreInfo;
    }



}
