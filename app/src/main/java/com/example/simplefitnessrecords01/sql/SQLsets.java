package com.example.simplefitnessrecords01.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.simplefitnessrecords01.fitness.SetFitness;
import com.example.simplefitnessrecords01.fitness.Weight;

public class SQLsets extends SQLiteOpenHelper {
    Context context = null;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EXE = "exe";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_REPEATS = "repeats";

    private String tableName = "test";
    public String getTableName() {
        return tableName;
    }

    public SQLsets(Context context, String tableName) {
        super(context, "TablesWithTrainings", null, 5);
        this.tableName = tableName;
        this.context = context;
    }


    //methog give all information in Log
    public void getTableInLog(String logTag){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(tableName,null,null,null,null,null,null);
        if(c.moveToNext()){
            int id_id = c.getColumnIndex(COLUMN_ID);
            int id_exe = c.getColumnIndex(COLUMN_EXE);
            int id_wei = c.getColumnIndex(COLUMN_WEIGHT);
            int id_rep = c.getColumnIndex(COLUMN_REPEATS);
            do {
                int id     = c.getInt(id_id);
                String exe = c.getString(id_exe);
                int wei    = c.getInt(id_wei);
                int rep    = c.getInt(id_rep);
                Log.d(logTag, "Table " + "tableName" + "has: " + COLUMN_ID + " - " + id + " ; " + COLUMN_EXE + " - " +
                        exe + " ; " + COLUMN_WEIGHT + " - " +
                        wei + " ; " + COLUMN_REPEATS + " - " +  rep + " ; " );
            } while ( c.moveToNext() );
        }else Log.d(logTag, "Table " + tableName + " is empty.");
    }



    public void addSetFitnessToSQL(SetFitness setFitness){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EXE     , setFitness.getExe().toString());
        cv.put(COLUMN_WEIGHT  , setFitness.getRecordSet().getWeight().getWeight());
        cv.put(COLUMN_REPEATS , setFitness.getRecordSet().getRepeats().getRepeats());
        getWritableDatabase().insert(tableName,null,cv);
        cv.clear();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // Створення таблиць бази даних
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EXE + " TEXT, "
                + COLUMN_WEIGHT + " INTEGER, " + COLUMN_REPEATS + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Оновлення таблиць бази даних
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }
}
