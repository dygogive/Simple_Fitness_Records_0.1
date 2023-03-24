package com.example.simplefitnessrecords01.sql;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLfitness extends SQLiteOpenHelper {

    /************   НАЗВА БД ТА ТАБЛИЦЬ *************************/
    private static final String DATABASE_NAME = "mydatabase.db";
    public static final String DATABASE_TABLE = "fitnessDay";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SUBNAME = "subname";

    private static final int DATABASE_VERSION = 1;





    /************* КОНСТРУКТОР  ********************/
    public SQLfitness(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    //methog give all information in Log
    public void getTableInLog(String logTag){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(DATABASE_TABLE,null,null,null,null,null,null);
        if(c.moveToNext()){
            do{
                int id_id      = c.getColumnIndex(COLUMN_ID);
                int id_day     = c.getColumnIndex(COLUMN_DAY);
                int id_name    = c.getColumnIndex(COLUMN_NAME);
                int id_subname = c.getColumnIndex(COLUMN_SUBNAME);
                //
                int id     = c.getInt(id_id);
                String day = c.getString(id_day);
                String name    = c.getString(id_name);
                String subname    = c.getString(id_subname);
                //
                Log.d(logTag, "Table " + DATABASE_TABLE + "has: " + COLUMN_ID + " - " + id + " ; " + COLUMN_DAY + " - " +
                        day + " ; " + COLUMN_NAME + " - " +
                        name + " ; " + COLUMN_SUBNAME + " - " +  subname + " ; " );
            }while (c.moveToNext());
        }else Log.d(logTag, "Table " + DATABASE_TABLE + " is empty.");
    }




    /*************** СТВОРЕННЯ ТАБЛИЦЬ **********************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Створення таблиць бази даних
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DAY + " TEXT, " + COLUMN_NAME + " TEXT, " +
                "" + COLUMN_SUBNAME + " TEXT)" );
    }





    /************************* ОНОВЛЕННЯ ТАБЛИЦЬ ********************************/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Оновлення таблиць бази даних
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }



}

