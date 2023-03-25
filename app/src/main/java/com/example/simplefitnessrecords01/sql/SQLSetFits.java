package com.example.simplefitnessrecords01.sql;

import static com.example.simplefitnessrecords01.sql.SQLfitness.COLUMN_FITNAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.simplefitnessrecords01.fitness.SetFitness;

public class SQLSetFits extends SQLiteOpenHelper {
    Context context = null;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EXE = "exe";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_REPEATS = "repeats";
    public static final String DATABASE_TABLE = "tableSets";




    public SQLSetFits(Context context) {
        super(context, "TablesWithTrainings", null, 5);
        this.context = context;
    }


    //methog give all information in Log
    public void getTableInLog(String logTag, String selection){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(DATABASE_TABLE,null,COLUMN_FITNAME + " = ?",new String[]{selection},null,null,null);

        if(c.moveToNext()){
            int id_id = c.getColumnIndex(COLUMN_ID);
            int id_exe = c.getColumnIndex(COLUMN_EXE);
            int id_wei = c.getColumnIndex(COLUMN_WEIGHT);
            int id_rep = c.getColumnIndex(COLUMN_REPEATS);
            int id_fitname = c.getColumnIndex(COLUMN_FITNAME);
            do {
                int id     = c.getInt(id_id);
                String exe = c.getString(id_exe);
                int wei    = c.getInt(id_wei);
                int rep    = c.getInt(id_rep);
                String fitname = c.getString(id_fitname);

                Log.d(logTag, "Table " + "tableName" + "has: " + COLUMN_ID + " - " + id + " ; " + COLUMN_EXE + " - " +
                        exe + " ; " + COLUMN_WEIGHT + " - " +
                        wei + " ; " + COLUMN_REPEATS + " - " +  rep + " ; " + COLUMN_FITNAME + " - " +  fitname + " ; " );
            } while ( c.moveToNext() );
        }else Log.d(logTag, "Table " + DATABASE_TABLE + " is empty.");
    }



    public void addSetFitToSQL(SetFitness setFitness){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EXE     , setFitness.getExe().toString());
        cv.put(COLUMN_FITNAME , setFitness.getFitnessName());
        getWritableDatabase().insert(DATABASE_TABLE,null,cv);
        cv.clear();
    }


    public void updateSetFitInSQL(SetFitness setFitness){

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Створення таблиць бази даних
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_FITNAME + " TEXT, " + COLUMN_EXE + " TEXT, "
                + COLUMN_WEIGHT + " INTEGER, " + COLUMN_REPEATS + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Оновлення таблиць бази даних
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }
}
