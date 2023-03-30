package com.example.simplefitnessrecords01.sql;

import static com.example.simplefitnessrecords01.sql.SQLtrainings.COLUMN_SUB_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.simplefitnessrecords01.fitness.OneSet;

public class SQLSetFits extends SQLiteOpenHelper {


    /************   NAME OF DB AND TABLES *************************/
    Context context;
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EXE = "exe";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_REPEATS = "repeats";
    public static final String DATABASE_TABLE = "tableSets";



    /************* CONSTRUCTOR  ********************/
    public SQLSetFits(Context context) {
        super(context, "TablesWithTrainings", null, 5);
        this.context = context;
    }




    //methog give all information in Log
    public void getTableInLog(String logTag, String selection){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query(DATABASE_TABLE,null, COLUMN_SUB_NAME + " = ?",new String[]{selection},null,null,null);

        if(c.moveToNext()){
            int id_id = c.getColumnIndex(COLUMN_ID);
            int id_exe = c.getColumnIndex(COLUMN_EXE);
            int id_wei = c.getColumnIndex(COLUMN_WEIGHT);
            int id_rep = c.getColumnIndex(COLUMN_REPEATS);
            int id_fitname = c.getColumnIndex(COLUMN_SUB_NAME);
            do {
                int id     = c.getInt(id_id);
                String exe = c.getString(id_exe);
                int wei    = c.getInt(id_wei);
                int rep    = c.getInt(id_rep);
                String fitname = c.getString(id_fitname);

                Log.d(logTag, "Table " + "tableName" + "has: " + COLUMN_ID + " - " + id + " ; " + COLUMN_EXE + " - " +
                        exe + " ; " + COLUMN_WEIGHT + " - " +
                        wei + " ; " + COLUMN_REPEATS + " - " +  rep + " ; " + COLUMN_SUB_NAME + " - " +  fitname + " ; " );
            } while ( c.moveToNext() );
        }else Log.d(logTag, "Table " + DATABASE_TABLE + " is empty.");

        c.close();
    }






    //add delete, update Rows
    public void addRow(OneSet oneSet){

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_EXE     , oneSet.getExe().toString());
        cv.put(COLUMN_SUB_NAME, oneSet.getUniqueFitTraining());
        getWritableDatabase().insert(DATABASE_TABLE,null,cv);
        cv.clear();
    }
    public void deleteRow(String uniqueName) {
        //code to delete
        String where_clause = COLUMN_SUB_NAME + " = ?";
        String[] where_args = new String[] { uniqueName };
        String sql = "DELETE FROM " + DATABASE_TABLE + " WHERE " + where_clause;
        //delete from database
        getWritableDatabase().execSQL(sql, where_args);
    }
    public void updateRow(String oldUniqueName , String newUniqueName) {
        //get strings from dialog
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SUB_NAME, newUniqueName);

        //get cursor from table sql
        Cursor c = getWritableDatabase().query(DATABASE_TABLE,null, COLUMN_SUB_NAME + " = ?",new String[]{oldUniqueName},null,null,null);
        //put cursor to chosen position of recycler
        if(c.moveToFirst()) {
            //update the table in selected id parameter
            String whereClause = COLUMN_SUB_NAME + " = ?";
            String[] whereArgs = new String[] {oldUniqueName};
            getWritableDatabase().update(SQLtrainings.DATABASE_TABLE, cv, whereClause, whereArgs);
            cv.clear();
        }
    }






    /*************** CREATION OF TABLES **********************/
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Створення таблиць бази даних
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " " +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " + COLUMN_SUB_NAME + " TEXT, " + COLUMN_EXE + " TEXT, "
                + COLUMN_WEIGHT + " INTEGER, " + COLUMN_REPEATS + " INTEGER)");
    }


    /************************* UPDATE TABLES ********************************/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Оновлення таблиць бази даних
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }
}
