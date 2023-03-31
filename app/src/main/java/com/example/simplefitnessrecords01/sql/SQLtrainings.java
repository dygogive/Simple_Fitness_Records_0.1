package com.example.simplefitnessrecords01.sql;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLtrainings extends SQLiteOpenHelper {


    /************   NAME OF DB AND TABLES *************************/
    private static final String DATABASE_NAME = "tablesWithTrainings";
    public static final String DATABASE_TABLE = "fitnessDay";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_INFO = "fitname";

    private static final int DATABASE_VERSION = 1;




    //methog give all information in Log
    public void getTableInLog(String logTag){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(DATABASE_TABLE,null,null,null,null,null,null);
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
                Log.d(logTag, "Table " + DATABASE_TABLE + "has: " + COLUMN_ID + " - " + id + " ; " + COLUMN_DAY + " - " +
                        day + " ; " + COLUMN_NAME + " - " +
                        name + " ; " + COLUMN_INFO + " - " +  subname + " ; " );
            }while (c.moveToNext());
        }else Log.d(logTag, "Table " + DATABASE_TABLE + " is empty.");
    }






    /************* CONSTRUCTOR  ********************/
    public SQLtrainings(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }






    //methods for changing rows
    public void deleteRow(String uniqueName) {
        //code to delete
        String table_name = DATABASE_TABLE;
        String where_clause = COLUMN_INFO + "=?";
        String[] where_args = new String[] { uniqueName };
        String sql = "DELETE FROM " + table_name + " WHERE " + where_clause;
        //delete from database
        getWritableDatabase().execSQL(sql, where_args);
    }

    public void updateRow(int position, String[] data) {
        //get strings from dialog
        ContentValues cv = new ContentValues();
        cv.put(SQLtrainings.COLUMN_DAY, data[0]);
        cv.put(SQLtrainings.COLUMN_NAME, data[1]);
        cv.put(SQLtrainings.COLUMN_INFO, data[2]);


        //get cursor from table sql
        Cursor c = getWritableDatabase().query(DATABASE_TABLE,null,null,null,null,null,null);
        //put cursor to chosen position of recycler
        if(c.moveToPosition(position)) {
            //update the table in selected id parameter
            int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID));
            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = new String[] {Integer.toString(id)};
            getWritableDatabase().update(SQLtrainings.DATABASE_TABLE, cv, whereClause, whereArgs);
            cv.clear();
        }
    }

    //get String[] of row in table from database
    public String[] getRow(int position) {
        String[] str = null;
        Cursor cursor = getWritableDatabase().query(DATABASE_TABLE,null,null,null,null,null,null);
        if(cursor.moveToPosition(position)){
            @SuppressLint("Range") int id      = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            @SuppressLint("Range") String day  = cursor.getString(cursor.getColumnIndex(COLUMN_DAY));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") String info = cursor.getString(cursor.getColumnIndex(COLUMN_INFO));
            str = new String[]{String.valueOf(id), day, name, info};
        }
            return str;
    }







    /*************** CREATION OF TABLES **********************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Створення таблиць бази даних
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DAY + " TEXT, " + COLUMN_NAME + " TEXT, " +
                "" + COLUMN_INFO + " TEXT)" );
    }


    /************************* UPDATE TABLES ********************************/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Оновлення таблиць бази даних
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

}

