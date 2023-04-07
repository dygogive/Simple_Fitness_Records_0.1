package com.example.simplefitnessrecords01.sql;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.simplefitnessrecords01.fitness.OneSet;

public class SQLhelper extends SQLiteOpenHelper {
    //
    Context context;

    /************   NAME OF DB AND TABLES *************************/
    private static final String DATABASE_NAME = "tablesWithTrainings";
    private static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ID = "_id";

    public static final String TABLE_TRAININGS = "tableTraining";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_INFO = "info";


    public static final String TABLE_SETS = "tableSets";
    public static final String COLUMN_EXE = "exe";
    public static final String COLUMN_GROUP = "exegroup";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_REPEATS = "repeats";
    public static final String COLUMN_UNIC_NAME = "unicName";





    //methog give all information in Log
    public void getTableInLogTrainings(String logTag){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_TRAININGS,null,null,null,null,null,null);
        if(c.moveToNext()){
            do{
                int id_id      = c.getColumnIndex(COLUMN_ID);
                int id_day     = c.getColumnIndex(COLUMN_DAY);
                int id_name    = c.getColumnIndex(COLUMN_NAME);
                int id_subname = c.getColumnIndex(COLUMN_INFO);
                //
                int id     = c.getInt(id_id);
                String day = c.getString(id_day);
                String name    = c.getString(id_name);
                String subname    = c.getString(id_subname);
                //
                Log.d(logTag, "Table " + TABLE_TRAININGS + "has: " + COLUMN_ID + " - " + id + " ; " + COLUMN_DAY + " - " +
                        day + " ; " + COLUMN_NAME + " - " +
                        name + " ; " + COLUMN_INFO + " - " +  subname + " ; " );
            }while (c.moveToNext());
        }else Log.d(logTag, "Table " + TABLE_TRAININGS + " is empty.");
    }



    //methog give all information in Log
    public void getTableInLogSets(String logTag, String selection){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor c = db.query(TABLE_SETS,null, COLUMN_UNIQUE_NAME + " = ?",new String[]{selection},null,null,null);
        Cursor c = db.query(TABLE_SETS,null, null,null,null,null,null);

        if(c.moveToNext()){
            int id_id = c.getColumnIndex(COLUMN_ID);
            int id_group = c.getColumnIndex(COLUMN_GROUP);
            int id_exe = c.getColumnIndex(COLUMN_EXE);
            int id_wei = c.getColumnIndex(COLUMN_WEIGHT);
            int id_rep = c.getColumnIndex(COLUMN_REPEATS);
            int id_fitname = c.getColumnIndex(COLUMN_UNIC_NAME);
            do {
                int id     = c.getInt(id_id);
                String group = c.getString(id_group);
                String exe = c.getString(id_exe);
                int wei    = c.getInt(id_wei);
                int rep    = c.getInt(id_rep);
                String fitname = c.getString(id_fitname);

                Log.d(logTag, "Table " + "tableName" + "has: " + COLUMN_ID + " - " + id + " ; " + COLUMN_GROUP + " - " +
                        group + " ; " + COLUMN_EXE + " - " +  exe + " ; " + COLUMN_WEIGHT + " - " +
                        wei + " ; " + COLUMN_REPEATS + " - " +  rep + " ; " + COLUMN_UNIC_NAME + " - " +  fitname + " ; " );
            } while ( c.moveToNext() );
        }else Log.d(logTag, "Table " + TABLE_SETS + " is empty.");

        c.close();
    }






    /************* CONSTRUCTOR  ********************/
    public SQLhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    //methods for changing rows
    public void deleteRowTrainings(String[] uniqueName) {
        //code to delete
        String table_name = TABLE_TRAININGS;
        String where_clause = COLUMN_DAY + "=?" + " AND " + COLUMN_NAME + "=?" + " AND " + COLUMN_INFO + "=?";
        String[] where_args = new String[] { uniqueName[0], uniqueName[1], uniqueName[2] };
        String sql = "DELETE FROM " + table_name + " WHERE " + where_clause;
        //delete from database
        getWritableDatabase().execSQL(sql, where_args);
    }

    public void updateRowTrainings(int position, String[] data) {
        //get strings from dialog
        ContentValues cv = new ContentValues();
        cv.put(SQLhelper.COLUMN_DAY, data[0]);
        cv.put(SQLhelper.COLUMN_NAME, data[1]);
        cv.put(SQLhelper.COLUMN_INFO, data[2]);


        //get cursor from table sql
        Cursor c = getWritableDatabase().query(TABLE_TRAININGS,null,null,null,null,null,null);
        //put cursor to chosen position of recycler
        if(c.moveToPosition(position)) {
            //update the table in selected id parameter
            int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID));
            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = new String[] {Integer.toString(id)};
            getWritableDatabase().update(SQLhelper.TABLE_TRAININGS, cv, whereClause, whereArgs);
            cv.clear();
        }
    }

    //get String[] of row in table from database
    public String[] getRowTrainings(int position) {
        String[] str = null;
        Cursor cursor = getWritableDatabase().query(TABLE_TRAININGS,null,null,null,null,null,null);
        if(cursor.moveToPosition(position)){
            @SuppressLint("Range") int id      = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            @SuppressLint("Range") String day  = cursor.getString(cursor.getColumnIndex(COLUMN_DAY));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") String info = cursor.getString(cursor.getColumnIndex(COLUMN_INFO));
            str = new String[]{String.valueOf(id), day, name, info};
        }
            return str;
    }









    //add delete, update Rows
    public void addRowSets(OneSet oneSet){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EXE     , oneSet.getExe().toString());
        cv.put(COLUMN_UNIC_NAME, oneSet.getUniqueFitTraining());
        getWritableDatabase().insert(TABLE_SETS,null,cv);
        cv.clear();
    }
    public void deleteRowSets(String uniqueName) {
        //code to delete
        String where_clause = COLUMN_UNIC_NAME + " = ?";
        String[] where_args = new String[] { uniqueName };
        String sql = "DELETE FROM " + TABLE_SETS + " WHERE " + where_clause;
        Log.d("deleting" , sql);
        Log.d("deleting" , "deleting " + uniqueName);
        //delete from database
        getWritableDatabase().execSQL(sql, where_args);
    }
    public void updateRowSets(String oldUniqueName , String newUniqueName) {
        //get strings from dialog
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_UNIC_NAME, newUniqueName);

        //get cursor from table sql
        Cursor c = getWritableDatabase().query(TABLE_SETS,null, COLUMN_UNIC_NAME + " = ?",new String[]{oldUniqueName},null,null,null);
        //put cursor to chosen position of recycler
        if(c.moveToFirst()) {
            //update the table in selected id parameter
            String whereClause = COLUMN_UNIC_NAME + " = ?";
            String[] whereArgs = new String[] {oldUniqueName};
            getWritableDatabase().update(SQLhelper.TABLE_SETS, cv, whereClause, whereArgs);
            cv.clear();
        }
    }

    public void updateRowSets(int position, String unicName, String[] groupExercise) {


        ContentValues cv = new ContentValues();
        cv.put(COLUMN_GROUP, groupExercise[0] + ": " + groupExercise[1]);
        cv.put(COLUMN_EXE, "Some exe");

        //get cursor from table sql
        Cursor c = getWritableDatabase().query(TABLE_SETS,null,COLUMN_UNIC_NAME + " = ?",new String[]{unicName},null,null,null);
        //put cursor to chosen position of recycler
        if(c.moveToPosition(position)) {
            //update the table in selected id parameter
            int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID));
            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = new String[] {Integer.toString(id)};
            getWritableDatabase().update(SQLhelper.TABLE_SETS, cv, whereClause, whereArgs);
            cv.clear();
        }

    }























    /*************** CREATION OF TABLES **********************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("notStart" , "start");
        // Creation of database tables
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TRAININGS + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DAY + " TEXT, " + COLUMN_NAME + " TEXT, " +
                "" + COLUMN_INFO + " TEXT)" );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SETS + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_UNIC_NAME + " TEXT, " + COLUMN_GROUP + " TEXT, "
                + COLUMN_EXE + " TEXT, " + COLUMN_WEIGHT + " INTEGER, " + COLUMN_REPEATS + " INTEGER)");

    }


    /************************* UPDATE TABLES ********************************/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Updating database tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAININGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETS);
        onCreate(db);

    }

}

