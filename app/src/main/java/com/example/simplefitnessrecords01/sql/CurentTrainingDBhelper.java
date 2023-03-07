package com.example.simplefitnessrecords01.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CurentTrainingDBhelper extends SQLiteOpenHelper {
    public static final String COLUMN_ID = "id";
    private static final String COLUMN_EXE = "exe";
    private static final String COLUMN_WEIGHT = "weight";
    private static final String COLUMN_REPEATS = "repeats";

    String tableName;

    public CurentTrainingDBhelper(Context context, String tableName, int version) {
        super(context, "TablesWithTrainings", null, version);
        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Створення таблиць бази даних
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EXE + " TEXT, " +
                "" + COLUMN_WEIGHT + " INTEGER, " + COLUMN_REPEATS + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
