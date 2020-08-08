package com.example.project.fitnessapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.project.fitnessapplication.entities.BMIStatusHistory;
import com.example.project.fitnessapplication.entities.CalorieIntakeHistory;
import com.example.project.fitnessapplication.entities.UserObject;
import com.example.project.fitnessapplication.entities.WaterCountHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


// Singleton Class Database Helper
public class DatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static DatabaseHelper sInstance;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "user.db";

    // query for creating user table
    private static final String CREATE_TBL_USER = "CREATE TABLE "
            + DatabaseContract.UserTable.TABLE_NAME + " ("
            + DatabaseContract.UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DatabaseContract.UserTable.COL_EMAIL + " TEXT NOT NULL, "
            + DatabaseContract.UserTable.COL_PASSWORD + " TEXT NOT NULL)";

    // query for creating user_water_count table
    private static final String CREATE_TBL_USER_WATER_COUNT = "CREATE TABLE "
            + DatabaseContract.UserWaterCountTable.TABLE_NAME + " ("
            + DatabaseContract.UserWaterCountTable._ID + " INTEGER, "
            + DatabaseContract.UserWaterCountTable._COUNT + " INTEGER NOT NULL, "
            + DatabaseContract.UserWaterCountTable.COL_DATE + " TEXT NOT NULL)";


    //query for creating user_calorie_intake table
    private static final String CREATE_TBL_USER_CALORIE_INTAKE = "CREATE TABLE "
            + DatabaseContract.UserCalorieIntakeTable.TABLE_NAME + " ("
            + DatabaseContract.UserCalorieIntakeTable._ID + " INTEGER, "
            + DatabaseContract.UserCalorieIntakeTable._COUNT + " INTEGER NOT NULL, "
            + DatabaseContract.UserCalorieIntakeTable.COL_DATE + " TEXT NOT NULL)";

    // query for creating user_bmi_status table
    private static final String CREATE_TBL_USER_BMI_STATUS = "CREATE TABLE "
            + DatabaseContract.UserBMIStatusTable.TABLE_NAME + " ("
            + DatabaseContract.UserBMIStatusTable._ID + " INTEGER, "
            + DatabaseContract.UserBMIStatusTable.COL_STATUS + " TEXT NOT NULL, "
            + DatabaseContract.UserBMIStatusTable.COL_DATE + " TEXT NOT NULL)";


    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // The instance needs to be created only once because we only need to access database once and then use it throughout the whole application
    public static DatabaseHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TBL_USER);
        db.execSQL(CREATE_TBL_USER_WATER_COUNT);
        db.execSQL(CREATE_TBL_USER_CALORIE_INTAKE);
        db.execSQL(CREATE_TBL_USER_BMI_STATUS);
    }

    // No need for this method
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public boolean login(UserObject user) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + DatabaseContract.UserTable.TABLE_NAME + " where " + DatabaseContract.UserTable.COL_EMAIL + "=? and " + DatabaseContract.UserTable.COL_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user.getEmail(), user.getPassword()});
        int id = 0;
        boolean isLoginSuccessful = false;

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserTable._ID));
            } while (cursor.moveToNext());
        }

        if (id > 0) {
            SharedPreferences s = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = s.edit();
            editor.putInt("id", id);
            editor.putString("email", user.getEmail());
            editor.commit();
            isLoginSuccessful = true;
        }

        cursor.close();
        return isLoginSuccessful;
    }

    public boolean signUp(UserObject user) {
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + DatabaseContract.UserTable.TABLE_NAME + " where " + DatabaseContract.UserTable.COL_EMAIL + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{user.getEmail()});
        int userId = 0;
        boolean doesUserExist = false;

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserTable._ID));
            } while (cursor.moveToNext());
        }

        if (userId > 0) {
            doesUserExist = true;
        }

        if (!doesUserExist) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.UserTable.COL_EMAIL, user.getEmail());
            values.put(DatabaseContract.UserTable.COL_PASSWORD, user.getPassword());
            db.insert(DatabaseContract.UserTable.TABLE_NAME, null, values);
        }

        cursor.close();
        return doesUserExist;
    }


    public void setDailyWaterCount(int count) {
        SQLiteDatabase db = getReadableDatabase();
        String currentDate = getCurrentDate();
        SharedPreferences sharedPref = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        final int idFromSharedPreference = sharedPref.getInt("id", 0);
        String selectQuery = "SELECT  * FROM " + DatabaseContract.UserWaterCountTable.TABLE_NAME + " where " + DatabaseContract.UserWaterCountTable._ID + "=? and " + DatabaseContract.UserWaterCountTable.COL_DATE + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(idFromSharedPreference), currentDate});
        int userId = 0;

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserWaterCountTable._ID));
            } while (cursor.moveToNext());
        }

        // records exists for daily water count
        if (userId > 0) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.UserWaterCountTable._COUNT, count);
            String[] args = new String[]{String.valueOf(userId), currentDate};
            db.update(DatabaseContract.UserWaterCountTable.TABLE_NAME, values, DatabaseContract.UserWaterCountTable._ID + "=? and " + DatabaseContract.UserWaterCountTable.COL_DATE + "=?", args);
        } else {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.UserWaterCountTable._ID, idFromSharedPreference);
            values.put(DatabaseContract.UserWaterCountTable._COUNT, count);
            values.put(DatabaseContract.UserWaterCountTable.COL_DATE, currentDate);
            db.insert(DatabaseContract.UserWaterCountTable.TABLE_NAME, null, values);
        }
    }

    public void setDailyBMIStatus(String status) {
        SQLiteDatabase db = getReadableDatabase();
        String currentDate = getCurrentDate();
        SharedPreferences sharedPref = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        final int idFromSharedPreference = sharedPref.getInt("id", 0);
        String selectQuery = "SELECT  * FROM " + DatabaseContract.UserBMIStatusTable.TABLE_NAME + " where " + DatabaseContract.UserBMIStatusTable._ID + "=? and " + DatabaseContract.UserBMIStatusTable.COL_DATE + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(idFromSharedPreference), currentDate});
        int userId = 0;

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserBMIStatusTable._ID));
            } while (cursor.moveToNext());
        }

        // records exists for daily bmi status
        if (userId > 0) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.UserBMIStatusTable.COL_STATUS, status);
            String[] args = new String[]{String.valueOf(userId), currentDate};
            db.update(DatabaseContract.UserBMIStatusTable.TABLE_NAME, values, DatabaseContract.UserBMIStatusTable._ID + "=? and " + DatabaseContract.UserBMIStatusTable.COL_DATE + "=?", args);

        } else {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.UserBMIStatusTable._ID, idFromSharedPreference);
            values.put(DatabaseContract.UserBMIStatusTable.COL_STATUS, status);
            values.put(DatabaseContract.UserBMIStatusTable.COL_DATE, currentDate);
            db.insert(DatabaseContract.UserBMIStatusTable.TABLE_NAME, null, values);
        }
    }

    public void setDailyCalorieCount(int count) {
        SQLiteDatabase db = getReadableDatabase();
        String currentDate = getCurrentDate();
        SharedPreferences sharedPref = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        final int idFromSharedPreference = sharedPref.getInt("id", 0);
        String selectQuery = "SELECT  * FROM " + DatabaseContract.UserCalorieIntakeTable.TABLE_NAME + " where " + DatabaseContract.UserCalorieIntakeTable._ID + "=? and " + DatabaseContract.UserCalorieIntakeTable.COL_DATE + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(idFromSharedPreference), currentDate});
        int userId = 0;

        if (cursor.moveToFirst()) {
            do {
                userId = cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserCalorieIntakeTable._ID));
            } while (cursor.moveToNext());
        }

        // records exists for daily calorie count
        if (userId > 0) {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.UserCalorieIntakeTable._COUNT, count);
            String[] args = new String[]{String.valueOf(userId), currentDate};
            db.update(DatabaseContract.UserCalorieIntakeTable.TABLE_NAME, values, DatabaseContract.UserCalorieIntakeTable._ID + "=? and " + DatabaseContract.UserCalorieIntakeTable.COL_DATE + "=?", args);
        } else {
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.UserCalorieIntakeTable._ID, idFromSharedPreference);
            values.put(DatabaseContract.UserCalorieIntakeTable._COUNT, count);
            values.put(DatabaseContract.UserCalorieIntakeTable.COL_DATE, currentDate);
            db.insert(DatabaseContract.UserCalorieIntakeTable.TABLE_NAME, null, values);
        }
    }


    public int getDailyWaterCount() {
        SQLiteDatabase db = getReadableDatabase();
        String currentDate = getCurrentDate();
        SharedPreferences sharedPref = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        final int idFromSharedPreference = sharedPref.getInt("id", 0);
        String selectQuery = "SELECT  * FROM " + DatabaseContract.UserWaterCountTable.TABLE_NAME + " where " + DatabaseContract.UserWaterCountTable._ID + "=? and " + DatabaseContract.UserWaterCountTable.COL_DATE + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(idFromSharedPreference), currentDate});
        int count = 0;

        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserWaterCountTable._COUNT));
            } while (cursor.moveToNext());
        }

        return count;
    }

    public int getDailyCalorieCount() {
        SQLiteDatabase db = getReadableDatabase();
        String currentDate = getCurrentDate();
        SharedPreferences sharedPref = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        final int idFromSharedPreference = sharedPref.getInt("id", 0);
        String selectQuery = "SELECT  * FROM " + DatabaseContract.UserCalorieIntakeTable.TABLE_NAME + " where " + DatabaseContract.UserCalorieIntakeTable._ID + "=? and " + DatabaseContract.UserCalorieIntakeTable.COL_DATE + "=?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(idFromSharedPreference), currentDate});
        int count = 0;

        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserCalorieIntakeTable._COUNT));
            } while (cursor.moveToNext());
        }

        return count;
    }

    public List<WaterCountHistory> getDailyWaterCountHistory() {
        List<WaterCountHistory> historyList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        SharedPreferences sharedPref = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        final int idFromSharedPreference = sharedPref.getInt("id", 0);
        String selectQuery = "SELECT  * FROM " + DatabaseContract.UserWaterCountTable.TABLE_NAME + " where " + DatabaseContract.UserWaterCountTable._ID + "=? ";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(idFromSharedPreference)});

        if (cursor.moveToFirst()) {
            do {
                WaterCountHistory history = new WaterCountHistory();
                history.setCount(cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserWaterCountTable._COUNT)));
                history.setDate(cursor.getString(cursor.getColumnIndex(DatabaseContract.UserWaterCountTable.COL_DATE)));
                history.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserWaterCountTable._ID)));
                historyList.add(history);
            } while (cursor.moveToNext());
        }

        return historyList;
    }

    public List<BMIStatusHistory> getDailyBMIStatusHistory() {
        List<BMIStatusHistory> historyList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        SharedPreferences sharedPref = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        final int idFromSharedPreference = sharedPref.getInt("id", 0);
        String selectQuery = "SELECT  * FROM " + DatabaseContract.UserBMIStatusTable.TABLE_NAME + " where " + DatabaseContract.UserBMIStatusTable._ID + "=? ";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(idFromSharedPreference)});

        if (cursor.moveToFirst()) {
            do {
                BMIStatusHistory history = new BMIStatusHistory();
                history.setStatus(cursor.getString(cursor.getColumnIndex(DatabaseContract.UserBMIStatusTable.COL_STATUS)));
                history.setDate(cursor.getString(cursor.getColumnIndex(DatabaseContract.UserBMIStatusTable.COL_DATE)));
                history.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserBMIStatusTable._ID)));
                historyList.add(history);
            } while (cursor.moveToNext());
        }

        return historyList;
    }

    public List<CalorieIntakeHistory> getDailyCalorieCountHistory() {
        List<CalorieIntakeHistory> historyList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        SharedPreferences sharedPref = context.getSharedPreferences("LogIn", Context.MODE_PRIVATE);
        final int idFromSharedPreference = sharedPref.getInt("id", 0);
        String selectQuery = "SELECT  * FROM " + DatabaseContract.UserCalorieIntakeTable.TABLE_NAME + " where " + DatabaseContract.UserCalorieIntakeTable._ID + "=? ";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(idFromSharedPreference)});

        if (cursor.moveToFirst()) {
            do {
                CalorieIntakeHistory history = new CalorieIntakeHistory();
                history.setCount(cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserCalorieIntakeTable._COUNT)));
                history.setDate(cursor.getString(cursor.getColumnIndex(DatabaseContract.UserCalorieIntakeTable.COL_DATE)));
                history.setId(cursor.getInt(cursor.getColumnIndex(DatabaseContract.UserCalorieIntakeTable._ID)));
                historyList.add(history);
            } while (cursor.moveToNext());
        }

        return historyList;
    }


    public static String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(c);
    }

}
