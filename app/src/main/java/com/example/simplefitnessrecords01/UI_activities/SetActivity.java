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
    private SQLSetFits sqlSetFits;
    @Override
    public SQLiteOpenHelper getHelper() {
        return sqlSetFits;
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
        sqlSetFits = new SQLSetFits(SetActivity.this);
        db = sqlSetFits.getWritableDatabase();

        Log.d("findErr", "test - 3  onCreate");

        //ініціалізація Рециклера в'ю
        recycleViewInit();

        Log.d("findErr", "test - 4  onCreate");

    }
    @Override
    protected void onPause() {

        //save all to db
        saveToDBFromRecycler(adapter);

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
        Cursor c = db.query(sqlSetFits.DATABASE_TABLE, null, selection, selectionArgs, null, null, null);


        Log.d("findErr", "test - 3.12  ");
        //перебрати рядки курсору
        if(c.moveToNext()){
            int id_id = c.getColumnIndex(sqlSetFits.COLUMN_ID);
            int id_fitName = c.getColumnIndex(COLUMN_FITNAME);
            int id_exe = c.getColumnIndex(sqlSetFits.COLUMN_EXE);
            int id_wei = c.getColumnIndex(sqlSetFits.COLUMN_WEIGHT);
            int id_rep = c.getColumnIndex(sqlSetFits.COLUMN_REPEATS);
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
                sqlSetFits.addSetFitToSQL(setFitness);
                updateRecycler();
            } break;
            default:{}
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
                sqlSetFits.getTableInLog("MainActSQLog");
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
        Log.d("findErr2" , "test0");
        switch (item.getItemId()) {

            case R.id.delete_set:

                Log.d("findErr" , "test0");
                SetFitness setFitness = adapter.getSetFitness(positionForContextMenu);
                Log.d("findErr" , "test1");
                String where_clause = SQLfitness.COLUMN_FITNAME + "=?" +
                        " AND " + SQLSetFits.COLUMN_ID + "=?" + " AND " + SQLSetFits.COLUMN_EXE + "=?" +
                        " AND " + SQLSetFits.COLUMN_WEIGHT + "=?" + " AND " + SQLSetFits.COLUMN_REPEATS;
                Log.d("findErr" , "test2");
                String[] where_args = new String[] { nameFitness , String.valueOf(setFitness.getId()),
                        setFitness.getExe().toString() , String.valueOf(setFitness.getRecordSet().getWeight().getIntWeight()),
                        String.valueOf(setFitness.getRecordSet().getRepeats().getIntRepeats())};
                Log.d("findErr" , "test3");
                db.delete(sqlSetFits.DATABASE_TABLE,where_clause,where_args);
                Log.d("findErr" , "test4");
                //
                adapter.getSetFitList().remove(positionForContextMenu);
                Log.d("findErr" , "test5");
                //
                adapter.notifyDataSetChanged();
                Log.d("findErr" , "test6");

                return true;

            default:
                return super.onContextItemSelected(item);
    }

}

    public void setPosition(int position) {
        positionForContextMenu = position;
    }




    /**************  Save DATA TO DB ****************/
    private void saveToDBFromRecycler(AdapterSets adapterSets){
        //
        ContentValues cv = new ContentValues();
        //
        List<SetFitness> setsFitness = adapterSets. getSetFitList();
        //
        String where_clause = SQLfitness.COLUMN_FITNAME + "=?";
        String[] where_args = new String[] { nameFitness };
        Cursor cursor = db.query(sqlSetFits.DATABASE_TABLE, null,where_clause,where_args,null,null,null);
        cursor.moveToFirst();
        //
        Log.d("isCounts", "Записи " + cursor.getCount()  + " " + nameFitness);
        Log.d("isCounts", "Записи " + setsFitness.size() + " " + setsFitness.get(0).getFitnessName());
        if(cursor.getCount() == setsFitness.size()) {
            Log.d("isCounts", "Записи співпали");
            for (SetFitness setFitness : setsFitness) {
                //get data from setFitness
                String exe  = setFitness.getExe().toString();
                int weight  = setFitness.getRecordSet().getWeight().getIntWeight();
                int repeats = setFitness.getRecordSet().getRepeats().getIntRepeats();
                int id      = setFitness.getId();
                //put data in content
                cv.put(sqlSetFits.COLUMN_EXE, exe);
                cv.put(sqlSetFits.COLUMN_WEIGHT, weight);
                cv.put(sqlSetFits.COLUMN_REPEATS, repeats);
                //
                @SuppressLint("Range") int id1 = cursor.getInt(cursor.getColumnIndex(sqlSetFits.COLUMN_ID));
                Log.d("isCounts",  id + " " + exe + " " + weight + " " + repeats + " " + id1);
                if(id == id1) db.update(sqlSetFits.DATABASE_TABLE, cv, sqlSetFits.COLUMN_ID + " = ?" , new String[] {String.valueOf(id)} );
                //
                //
                cv.clear();
                cursor.moveToNext();
            }
        }
    }
}