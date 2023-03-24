package com.example.simplefitnessrecords01.UI_activities;

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

import com.example.simplefitnessrecords01.dialog.StartDialog;
import com.example.simplefitnessrecords01.fitness.Exercise;
import com.example.simplefitnessrecords01.fitness.Fitness;
import com.example.simplefitnessrecords01.fitness.RecordExercise;
import com.example.simplefitnessrecords01.fitness.Repeats;
import com.example.simplefitnessrecords01.fitness.SetFitness;
import com.example.simplefitnessrecords01.fitness.Weight;
import com.example.simplefitnessrecords01.recycler_views.AdapterSets;
import com.example.simplefitnessrecords01.sql.SQLfitness;
import com.example.simplefitnessrecords01.sql.SQLsets;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivitySetBinding;

import java.util.ArrayList;
import java.util.List;

public class SetActivity extends AppCompatActivity implements GetterDB, GetterSQLhelper  {


    //біндінг
    private ActivitySetBinding binding;




    //посилання на Помічник по роботі з базою даних
    private SQLsets sqLsets;
    @Override
    public SQLiteOpenHelper getHelper() {
        return sqLsets;
    }
    //база
    SQLiteDatabase db;


    //назва таблиці бази даних
    private String nameTableDB = "testName";
    public String getNameTableDB() {
        return nameTableDB;
    }
    //посилання на База даних
    public SQLiteDatabase getDB() {
        return db;
    }


    private int positionForContextMenu = -1;







    /**********  Activity Lifecycle ***************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //обробити інфу від екстра чз інтент
        procIntentExtra();
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
        nameTableDB = textDay + textName + textSubname;
    }
    //метод життєвого циклу актівіті
    @Override
    protected void onResume() {
        super.onResume();

        //ініціалізація посилань на базу даних
        if (  !nameTableDB.equals("")  ) {
            sqLsets = new SQLsets(SetActivity.this, "nameTableDB");
            db = sqLsets.getWritableDatabase();
        }
        //ініціалізація Рециклера в'ю
        recycleViewInit();
    }

    @Override
    protected void onPause() {
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


        List<SetFitness> setsFitness = getSetsFitness();

        AdapterSets adapter = new AdapterSets(SetActivity.this, setsFitness);
        //менеджер для РЕЦИКЛЕРА В'Ю
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Адаптер для рециклера
        binding.recyclerView.setAdapter(adapter);
    }

    private List<SetFitness> getSetsFitness() {
        //пустий список для SetTraining з бази для рециклера
        List<SetFitness> setsFitness = new ArrayList<>();

        //курсор з бази з вибором усього
        Cursor c = db.rawQuery("SELECT * FROM " + "nameTableDB", null);

        //перебрати рядки курсору
        if(c.moveToNext()){
            int id_id = c.getColumnIndex(SQLsets.COLUMN_ID);
            int id_exe = c.getColumnIndex(SQLsets.COLUMN_EXE);
            int id_wei = c.getColumnIndex(SQLsets.COLUMN_WEIGHT);
            int id_rep = c.getColumnIndex(SQLsets.COLUMN_REPEATS);
            do {
                int id     = c.getInt(id_id);
                String exe = c.getString(id_exe);
                int wei    = c.getInt(id_wei);
                int rep    = c.getInt(id_rep);

                //добавити в список
                setsFitness.add(   new SetFitness(id, new Exercise(exe) , new RecordExercise(new Weight(wei) , new Repeats(rep)))   );
            } while (c.moveToNext());
        } else {
            Toast.makeText(this, "Виконай перший підхід!", Toast.LENGTH_SHORT).show();
        }
        //повернути список об'єктів тренувань
        return setsFitness;
    }








    /***************** onClick button *******************/
    public void onClick(View view){
        switch (view.getId()){

            case R.id.btnAddExe:{
//                SetFitness setFitness = new SetFitness();
//                sqLsets.addSetFitnessToSQL(setFitness);
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
                sqLsets.getTableInLog("MainActSQLog");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }











    /************************ Context Menu  *****************************/

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

}