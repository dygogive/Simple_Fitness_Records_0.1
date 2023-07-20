package com.example.simplefitnessrecords01.sql;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.simplefitnessrecords01.fitness.EmptySetTraining;

public class SQLhelper extends SQLiteOpenHelper {
    //
    Context context;

    /************   NAME OF DB AND TABLES *************************/
    private static final String DATABASE_NAME = "tablesWithTrainings";
    private static final int DATABASE_VERSION = 1;
    public static final String COLUMN_ID = "_id";



    /*********************** TABLE_TRAININGS *****************************/
    public static final String TABLE_ONEGYM = "tableTraining";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_INFO = "info";

    /******************* TABLE_SETS ***********************/
    public static final String TABLE_SETS = "tableSets";
    public static final String COLUMN_EXE = "exe";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_REPEATS = "repeats";
    public static final String COLUMN_UNIC_NAME = "unicName";

    /************************** TABLE_EXERCISES ******************************/
    public static final String TABLE_EXERCISES = "tableExercises";
    public static final String COLUMN_NAME_EXE = "nameExe";
    public static final String COLUMN_GROUP = "exegroup";
    public static final String COLUMN_MUSCLE1 = "muscles1";
    public static final String COLUMN_MUSCLE2 = "muscles2";
    public static final String COLUMN_MUSCLE3 = "muscles3";
    public static final String COLUMN_MUSCLE4 = "muscles4";



    //methog give all information in Log
    public void getTableGymsInLog(String logTag){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(TABLE_ONEGYM,null,null,null,null,null,null);
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
                Log.d(logTag, "Table " + TABLE_ONEGYM + "has: " + COLUMN_ID + " - " + id + " ; " + COLUMN_DAY + " - " +
                        day + " ; " + COLUMN_NAME + " - " +
                        name + " ; " + COLUMN_INFO + " - " +  subname + " ; " );
            }while (c.moveToNext());
        }else Log.d(logTag, "Table " + TABLE_ONEGYM + " is empty.");
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


    //methog give all information in Log
    public void getTableInLogExercises(String logTag){
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor c = db.query(TABLE_SETS,null, COLUMN_UNIQUE_NAME + " = ?",new String[]{selection},null,null,null);
        Cursor c = db.query(TABLE_EXERCISES,null, null,null,null,null,null);

        if(c.moveToNext()){
            int id_id        = c.getColumnIndex(COLUMN_ID    );
            int id_exename = c.getColumnIndex(COLUMN_NAME_EXE);
            int id_group   = c.getColumnIndex(COLUMN_GROUP   );
            int id_muscle1 = c.getColumnIndex(COLUMN_MUSCLE1 );
            int id_muscle2 = c.getColumnIndex(COLUMN_MUSCLE2 );
            int id_muscle3 = c.getColumnIndex(COLUMN_MUSCLE3 );
            int id_muscle4 = c.getColumnIndex(COLUMN_MUSCLE4 );
            do {
                int id           = c.getInt   (id_id     );
                String exename   = c.getString(id_exename);
                String group     = c.getString(id_group  );
                String muscle1   = c.getString(id_muscle1);
                String muscle2   = c.getString(id_muscle2);
                String muscle3   = c.getString(id_muscle3);
                String muscle4   = c.getString(id_muscle4);

                Log.d(logTag, "Table " + TABLE_EXERCISES + "has: " + COLUMN_ID + " - " + id + " ; " + COLUMN_NAME_EXE + " - " +
                        exename + " ; " + COLUMN_GROUP + " - " +  group + " ; " + COLUMN_MUSCLE1 + " - " +
                        muscle1 + " ; " + COLUMN_MUSCLE2 + " - " +  muscle2 + " ; " + COLUMN_MUSCLE3 + " - " +  muscle3 + " ; "
                        + COLUMN_MUSCLE4 + " - " +  muscle4 + " ; ");

            } while ( c.moveToNext() );
        }else Log.d(logTag, "Table " + TABLE_EXERCISES + " is empty.");

        c.close();
    }






    /************* CONSTRUCTOR  ********************/
    public SQLhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //methods for changing rows
    public void deleteRowTrainings(String[] uniqueName) {
        //code to delete
        String table_name = TABLE_ONEGYM;
        String where_clause = COLUMN_DAY + "=?" + " AND " + COLUMN_NAME + "=?" + " AND " + COLUMN_INFO + "=?";
        String[] where_args = new String[] { uniqueName[0], uniqueName[1], uniqueName[2] };
        String sql = "DELETE FROM " + table_name + " WHERE " + where_clause;
        //delete from database
        getWritableDatabase().execSQL(sql, where_args);
    }

    //
    public void updateRowTrainings(int position, String[] data) {
        //get strings from dialog
        ContentValues cv = new ContentValues();
        cv.put(SQLhelper.COLUMN_DAY, data[0]);
        cv.put(SQLhelper.COLUMN_NAME, data[1]);
        cv.put(SQLhelper.COLUMN_INFO, data[2]);


        //get cursor from table sql
        Cursor c = getWritableDatabase().query(TABLE_ONEGYM,null,null,null,null,null,null);
        //put cursor to chosen position of recycler
        if(c.moveToPosition(position)) {
            //update the table in selected id parameter
            int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID));
            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = new String[] {Integer.toString(id)};
            getWritableDatabase().update(SQLhelper.TABLE_ONEGYM, cv, whereClause, whereArgs);
            cv.clear();
        }
    }

    //get String[] of row in table from database
    public String[] getRowTrainings(int position) {
        String[] str = null;
        Cursor cursor = getWritableDatabase().query(TABLE_ONEGYM,null,null,null,null,null,null);
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
    public void addRowSets(EmptySetTraining emptySetTraining){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EXE     , "Exercise");
        cv.put(COLUMN_UNIC_NAME, emptySetTraining.getUniqueFitTraining());
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
        cv.put(COLUMN_GROUP, groupExercise[0]);
        cv.put(COLUMN_MUSCLE1, groupExercise[1]);
        cv.put(COLUMN_MUSCLE2, groupExercise[2]);
        cv.put(COLUMN_MUSCLE3, groupExercise[3]);
        cv.put(COLUMN_MUSCLE4, groupExercise[4]);
        cv.put(COLUMN_EXE, groupExercise[5]);

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

    public void deleteRowExercise(int position) {

        //get cursor from table sql
        Cursor c = getWritableDatabase().query(TABLE_EXERCISES,null,null,null,null,null,null);
        //put cursor to chosen position of recycler
        if(c.moveToPosition(position)) {
            //update the table in selected id parameter
            int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID));
            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = new String[] {Integer.toString(id)};
            getWritableDatabase().delete(TABLE_EXERCISES, whereClause, whereArgs);
        }
    }

    public void editExeName(int position, String exeName){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_EXE,exeName);

        //get cursor from table sql
        Cursor c = getWritableDatabase().query(TABLE_EXERCISES,null,null,null,null,null,null);
        //put cursor to chosen position of recycler
        if(c.moveToPosition(position)) {
            //update the table in selected id parameter
            int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID));
            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = new String[] {Integer.toString(id)};
            getWritableDatabase().update(TABLE_EXERCISES,cv, whereClause, whereArgs);
        }

        cv.clear();

    }





    //add row in table Exercises
    public void createNewExeInSQL(String[] dataToNewExercise) {
        //
        SQLiteDatabase db = getWritableDatabase();
        //
        ContentValues cv = new ContentValues();
        //
        int i = 0;
        if(i < dataToNewExercise.length) cv.put(COLUMN_NAME_EXE, dataToNewExercise[i++]);
        if(i < dataToNewExercise.length) cv.put(COLUMN_GROUP, dataToNewExercise[i++]);
        if(i < dataToNewExercise.length) cv.put(COLUMN_MUSCLE1, dataToNewExercise[i++]);
        if(i < dataToNewExercise.length) cv.put(COLUMN_MUSCLE2, dataToNewExercise[i++]);
        if(i < dataToNewExercise.length) cv.put(COLUMN_MUSCLE3, dataToNewExercise[i++]);
        if(i < dataToNewExercise.length) cv.put(COLUMN_MUSCLE4, dataToNewExercise[i++]);

        //
        db.insert(TABLE_EXERCISES,null,cv);
        //
        cv.clear();
    }
















    /*************** CREATION OF TABLES **********************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("notStart" , "start");

        // Creation of database tables
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ONEGYM + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DAY + " TEXT, " + COLUMN_NAME + " TEXT, " +
                "" + COLUMN_INFO + " TEXT)" );

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_SETS + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_UNIC_NAME + " TEXT, " + COLUMN_GROUP + " TEXT, "
                + COLUMN_MUSCLE1 + " TEXT, " + COLUMN_MUSCLE2 + " TEXT, " + COLUMN_MUSCLE3 + " TEXT, "
                + COLUMN_MUSCLE4 + " TEXT, " + COLUMN_EXE + " TEXT, " + COLUMN_WEIGHT + " INTEGER, " + COLUMN_REPEATS + " INTEGER)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_EXERCISES + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME_EXE + " TEXT, " + COLUMN_GROUP + " TEXT, "
                + COLUMN_MUSCLE1 + " TEXT, " + COLUMN_MUSCLE2 + " TEXT, " + COLUMN_MUSCLE3 + " TEXT, "
                + COLUMN_MUSCLE4 + " TEXT)");
    }


    /************************* UPDATE TABLES ********************************/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Updating database tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ONEGYM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETS);
        onCreate(db);

    }

}

