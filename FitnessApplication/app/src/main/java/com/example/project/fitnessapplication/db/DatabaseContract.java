package com.example.project.fitnessapplication.db;

import android.provider.BaseColumns;

public class DatabaseContract {

    /* Inner class that defines the table contents */
    public static class UserTable implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COL_EMAIL = "email";
        public static final String COL_PASSWORD = "password";
    }


    /* Inner class that defines the table contents */
    public static class UserWaterCountTable implements BaseColumns {
        public static final String TABLE_NAME = "user_water_count";
        public static final String COL_DATE = "date";
    }


    public static class UserBMIStatusTable implements BaseColumns {
        public static final String TABLE_NAME = "user_bmi_status";
        public static final String COL_STATUS = "status";
        public static final String COL_DATE = "date";
    }


    /* Inner class that defines the table contents */
    public static class UserCalorieIntakeTable implements BaseColumns {
        public static final String TABLE_NAME = "user_calorie_intake";
        public static final String COL_DATE = "date";
    }



}
