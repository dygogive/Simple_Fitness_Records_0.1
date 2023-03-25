package com.example.simplefitnessrecords01.UI_activities;

import static com.example.simplefitnessrecords01.sql.SQLfitness.COLUMN_FITNAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.simplefitnessrecords01.fitness.Exercise;
import com.example.simplefitnessrecords01.fitness.RecordExercise;
import com.example.simplefitnessrecords01.fitness.Repeats;
import com.example.simplefitnessrecords01.fitness.SetFitness;
import com.example.simplefitnessrecords01.fitness.Weight;
import com.example.simplefitnessrecords01.recycler_views.AdapterSets;
import com.example.simplefitnessrecords01.sql.SQLSetFits;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivitySetBinding;
import com.example.simplefitnessrecords01.sql.SQLfitness;

import java.util.ArrayList;
import java.util.List;

public class SetActivity extends AppCompatActivity implements GetterDB, GetterSQLhelper  {


    //біндінг
    private ActivitySetBinding binding;




    //посилання на Помічник по роботі з базою даних
    private SQLSetFits SQLSetFits;
    @Override
    public SQLiteOpenHelper getHelper() {
        return SQLSetFits;
    }
    //база
    SQLiteDatabase db;




    //назва таблиці бази даних
    private String nameFitness = "testName";
    public String getNameFitness() {
        return nameFitness;
    }
    //посилання на База даних
    public SQLiteDatabase getDB() {
        return db;
    }





    AdapterSets adapter;








    /**********  Activity Lifecycle ***************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Log.d("findErr", "test - 0  onCreate");

        //обробити інфу від екстра чз інтент
        procIntentExtra();

        Log.d("findErr", "test - 1  onCreate");

    }
    //Обробити дані, що отримані від попереднього актівіті
    private void procIntentExtra() {
        //Інтент з інфою
        Intent intent = getIntent();
        //Інфа з інтенту
        String[] getExtraArray = intent.getStringArrayExtra("start fitness");
        //текстові строки з інфи з інтенту
        String textDay =     getExtraArray[0];
        String textName =    getExtraArray[1];
        String textSubname = getExtraArray[2];


        //показати тексти на екрані
        binding.tvDay.setText(textDay);
        binding.tvName.setText(textName);
        binding.tvSubName.setText(textSubname);

        //унікальна назва тренування
        nameFitness = textDay + textName + textSubname;


        Log.d("findErr", "test - 2  onCreate");
    }
    //метод життєвого циклу актівіті
    @Override
    protected void onResume() {
        super.onResume();

        //ініціалізація посилань на базу даних
        SQLSetFits = new SQLSetFits(SetActivity.this);
        db = SQLSetFits.getWritableDatabase();

        Log.d("findErr", "test - 3  onCreate");

        //ініціалізація Рециклера в'ю
        recycleViewInit();

        Log.d("findErr", "test - 4  onCreate");

    }

    @Override
    protected void onPause() {

        //save all to db
        saveToDBFromRecycler();

        //Закрити базу
//        getHelper().close();
//        db.close();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        db.close();
        super.onDestroy();
    }








    /**********  RecyclerView ***************/
    private void recycleViewInit() {
        Log.d("findErr", "test - 3.1  ");
        List<SetFitness> setsFitness = getSetsFitness();
        Log.d("findErr", "test - 3.2  ");

        adapter = new AdapterSets(SetActivity.this, setsFitness);
        //менеджер для РЕЦИКЛЕРА В'Ю
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Адаптер для рециклера
        binding.recyclerView.setAdapter(adapter);
    }

    private List<SetFitness> getSetsFitness() {
        //пустий список для SetTraining з бази для рециклера
        List<SetFitness> setsFitness = new ArrayList<>();

        Log.d("findErr", "test - 3.11  ");

        //курсор з бази з вибором усього
        //Cursor c = db.rawQuery("SELECT * FROM " + SQLSetFits.DATABASE_TABLE, null);+
        String selection = COLUMN_FITNAME + " = ?";
        String[] selectionArgs = new String[] {getNameFitness()};
        Cursor c = db.query(SQLSetFits.DATABASE_TABLE, null, selection, selectionArgs, null, null, null);


        Log.d("findErr", "test - 3.12  ");
        //перебрати рядки курсору
        if(c.moveToNext()){
            int id_id = c.getColumnIndex(SQLSetFits.COLUMN_ID);
            int id_fitName = c.getColumnIndex(COLUMN_FITNAME);
            int id_exe = c.getColumnIndex(SQLSetFits.COLUMN_EXE);
            int id_wei = c.getColumnIndex(SQLSetFits.COLUMN_WEIGHT);
            int id_rep = c.getColumnIndex(SQLSetFits.COLUMN_REPEATS);
            do {
                int id         = c.getInt(id_id);
                String fitName = c.getString(id_fitName);
                String exe     = c.getString(id_exe);
                int wei        = c.getInt(id_wei);
                int rep        = c.getInt(id_rep);

                //добавити в список
                setsFitness.add(   new SetFitness( id, new Exercise(exe) , new RecordExercise(new Weight(wei) , new Repeats(rep)) , fitName  )   );

            } while (c.moveToNext());
        } else {
            Toast.makeText(this, "Виконай перший підхід!", Toast.LENGTH_SHORT).show();
        }
        //повернути список об'єктів тренувань
        return setsFitness;
    }

    private void updateRecycler(){
        List<SetFitness> setsFitness = getSetsFitness();
        adapter.setSetFitList(setsFitness);
        adapter.notifyDataSetChanged();
    }






    /***************** onClick button *******************/
    public void onClick(View view){
        switch (view.getId()){

            case R.id.btnAddExe:{
                SetFitness setFitness = new SetFitness(nameFitness);
                SQLSetFits.addSetFitToSQL(setFitness);
                updateRecycler();
            } break;

            default:{

            }
        }
    }















    /********** Options Menu **************/

    /* Створення меню */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Роздування ресурсу меню з використанням MenuInflater
        getMenuInflater().inflate(R.menu.menu_training, menu);
        return true;
    }
    /* Listener of Options Menu */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обробка натискання пунктів меню
        int id = item.getItemId();
        switch (id) {
            case R.id.theend:
                // Обробка натискання на пункт "Закінчити тренування"
                //тестове натиснення з передаванням інфи вииваючому актівіті і завершенні цього актівіті
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result fitness", true);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return true;

            case R.id.settngs:
                // Обробка натискання на пункт "Налаштування"
                return true;

            case R.id.action_log_sql2:
                SQLSetFits.getTableInLog("MainActSQLog");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }











    /************************ Context Menu  *****************************/

    private int positionForContextMenu = -1;

    // Реалізація методу для обробки натискань на пункти контекстного меню
    @Override
    public boolean onContextItemSelected(MenuItem item)       {
        // Обробляємо натискання на пункти контекстного меню
        switch (item.getItemId()) {

            case R.id.edit:
                // Додаткові дії при натисканні на пункт "Редагувати"
                //Обробник натискання кнопки ОК діалогу
//                @SuppressLint("Range") DialogOK dialogOK = dayNameSubname -> {
//                    Log.d("log12",dayNameSubname[0] + " / " + dayNameSubname[1] + " / " + dayNameSubname[2]);
//                    SQLfitness dbHelp = new SQLfitness(MainActivity.this);
//                    SQLiteDatabase db = dbHelp.getWritableDatabase();
//
//                    ContentValues cv = new ContentValues();
//                    cv.put(SQLfitness.COLUMN_DAY, dayNameSubname[0]);
//                    cv.put(SQLfitness.COLUMN_NAME, dayNameSubname[1]);
//                    cv.put(SQLfitness.COLUMN_SUBNAME, dayNameSubname[2]);
//                    Cursor c = db.query(SQLfitness.DATABASE_TABLE,null,null,null,null,null,null);
//                    if(c.moveToPosition(position)) {
//                        int id = c.getInt(c.getColumnIndexOrThrow(SQLfitness.COLUMN_ID));
//                        String whereClause = SQLfitness.COLUMN_ID + " = ?";
//                        String[] whereArgs = new String[] {Integer.toString(id)};
//                        int rowsUpd = db.update(SQLfitness.DATABASE_TABLE, cv, whereClause, whereArgs);
//                        cv.clear();
//
//                        db.close();
//                        dbHelp.close();
//
//                        ((FitnessListAdapter)binding.recyclerView.getAdapter()).setSetTrainingList(getFitnessList());
//                        binding.recyclerView.getAdapter().notifyDataSetChanged();
//
//                    }
//                };
//                CustomDialog dialog = new CustomDialog(this, dialogOK);
//                dialog.show();

                return true;


            case R.id.delete:

                ((AdapterSets)binding.recyclerView.getAdapter()).deleteItem(positionForContextMenu);

                return true;

            default:
                return super.onContextItemSelected(item);
    }

}

    public void setPosition(int position) {
        positionForContextMenu = position;
    }







    /**************  Save DATA ****************/
    private void saveToDBFromRecycler(){
        //
        ContentValues cv = new ContentValues();
        //
        List<SetFitness> setsFitness = adapter. getSetFitList();
        //
        Cursor cursor = db.query(SQLSetFits.DATABASE_TABLE, null,null,null,null,null,null);
        cursor.moveToFirst();
        //
        Log.d("isCounts", "Записи " + cursor.getCount());
        Log.d("isCounts", "Записи " + setsFitness.size());
        if(cursor.getCount() == setsFitness.size()) {
            Log.d("isCounts", "Записи співпали");
            for (SetFitness setFitness : setsFitness) {
                //
                String exe = setFitness.getExe().toString();
                int weight = setFitness.getRecordSet().getWeight().getIntWeight();
                int repeats = setFitness.getRecordSet().getRepeats().getIntRepeats();
                //
                cv.put(SQLSetFits.COLUMN_EXE, exe);
                cv.put(SQLSetFits.COLUMN_WEIGHT, weight);
                cv.put(SQLSetFits.COLUMN_REPEATS, repeats);
                cv.put(COLUMN_FITNAME, nameFitness);
                //
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(SQLSetFits.COLUMN_ID));
                Log.d("isCounts",  id + " " + exe + " " + weight + " " + repeats);
                db.update(SQLSetFits.DATABASE_TABLE, cv, SQLSetFits.COLUMN_ID + " = ?" , new String[] {"1"} );
                //
                //
                cv.clear();
                cursor.moveToNext();
            }
        }
    }

}