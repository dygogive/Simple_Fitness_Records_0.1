package com.example.simplefitnessrecords01.sql;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLfitness extends SQLiteOpenHelper {

    /************   НАЗВА БД ТА ТАБЛИЦЬ *************************/
    private static final String DATABASE_NAME = "tablesWithSets";
    public static final String DATABASE_TABLE = "fitnessDay";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_UNIQ_NAME = "fitname";

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
                int id_subname = c.getColumnIndex(COLUMN_UNIQ_NAME);
                //
                int id     = c.getInt(id_id);
                String day = c.getString(id_day);
                String name    = c.getString(id_name);
                String subname    = c.getString(id_subname);
                //
                Log.d(logTag, "Table " + DATABASE_TABLE + "has: " + COLUMN_ID + " - " + id + " ; " + COLUMN_DAY + " - " +
                        day + " ; " + COLUMN_NAME + " - " +
                        name + " ; " + COLUMN_UNIQ_NAME + " - " +  subname + " ; " );
            }while (c.moveToNext());
        }else Log.d(logTag, "Table " + DATABASE_TABLE + " is empty.");
    }






    public void deleteRow(String uniqueName) {
        //code to delete
        String table_name = DATABASE_TABLE;
        String where_clause = COLUMN_UNIQ_NAME + "=?";
        String[] where_args = new String[] { uniqueName };
        String sql = "DELETE FROM " + table_name + " WHERE " + where_clause;
        //delete from database
        getWritableDatabase().execSQL(sql, where_args);
    }

    public void updateRow(int position, String[] data) {
        //get strings from dialog
        ContentValues cv = new ContentValues();
        cv.put(SQLfitness.COLUMN_DAY, data[0]);
        cv.put(SQLfitness.COLUMN_NAME, data[1]);
        cv.put(SQLfitness.COLUMN_UNIQ_NAME, data[2]);


        //get cursor from table sql
        Cursor c = getWritableDatabase().query(DATABASE_TABLE,null,null,null,null,null,null);
        //put cursor to chosen position of recycler
        if(c.moveToPosition(position)) {
            //update the table in selected id parameter
            int id = c.getInt(c.getColumnIndexOrThrow(COLUMN_ID));
            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = new String[] {Integer.toString(id)};
            getWritableDatabase().update(SQLfitness.DATABASE_TABLE, cv, whereClause, whereArgs);
            cv.clear();
        }
    }










    /*************** СТВОРЕННЯ ТАБЛИЦЬ **********************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Створення таблиць бази даних
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DAY + " TEXT, " + COLUMN_NAME + " TEXT, " +
                "" + COLUMN_UNIQ_NAME + " TEXT)" );
    }





    /************************* ОНОВЛЕННЯ ТАБЛИЦЬ ********************************/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Оновлення таблиць бази даних
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }



}

