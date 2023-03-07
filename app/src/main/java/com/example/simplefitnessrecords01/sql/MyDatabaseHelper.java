package com.example.simplefitnessrecords01.sql;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    /************   НАЗВА БД ТА ТАБЛИЦЬ *************************/
    private static final String DATABASE_NAME = "mydatabase.db";
    public static final String DATABASE_TABLE = "sets";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SUBNAME = "subname";

    private static final int DATABASE_VERSION = 1;


    /************* КОНСТРУКТОР  ********************/
    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /*************** СТВОРЕННЯ ТАБЛИЦЬ **********************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Створення таблиць бази даних
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DAY + " TEXT, " + COLUMN_NAME + " TEXT, " +
                "" + COLUMN_SUBNAME + " TEXT)");
    }



    /************************* ОНОВЛЕННЯ ТАБЛИЦЬ ********************************/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Оновлення таблиць бази даних
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }



}

