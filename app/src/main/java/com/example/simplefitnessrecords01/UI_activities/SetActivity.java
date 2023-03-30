package com.example.simplefitnessrecords01.UI_activities;

import static com.example.simplefitnessrecords01.sql.SQLfitness.COLUMN_SUB_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.simplefitnessrecords01.fitness.Exercise;
import com.example.simplefitnessrecords01.fitness.ExecutedExercise;
import com.example.simplefitnessrecords01.fitness.Repeats;
import com.example.simplefitnessrecords01.fitness.OneSet;
import com.example.simplefitnessrecords01.fitness.Weight;
import com.example.simplefitnessrecords01.recycler_views.RecyclerViewOneSetsAdapter;
import com.example.simplefitnessrecords01.sql.SQLSetFits;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivitySetBinding;
import com.example.simplefitnessrecords01.sql.SQLfitness;

import java.util.ArrayList;
import java.util.List;

public class SetActivity extends AppCompatActivity {


    //біндінг
    private ActivitySetBinding binding;



    //посилання на Помічник по роботі з базою даних
    private SQLSetFits sqlSetFits;

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



    RecyclerViewOneSetsAdapter adapter;




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
        List<OneSet> setsFitness = getSetsFitness();
        //
        adapter = new RecyclerViewOneSetsAdapter(SetActivity.this, setsFitness);
        //менеджер для РЕЦИКЛЕРА В'Ю
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Адаптер для рециклера
        binding.recyclerView.setAdapter(adapter);
    }

    private List<OneSet> getSetsFitness() {
        //пустий список для SetTraining з бази для рециклера
        List<OneSet> setsFitness = new ArrayList<>();
        //курсор з бази з вибором усього
        //Cursor c = db.rawQuery("SELECT * FROM " + SQLSetFits.DATABASE_TABLE, null);+
        String selection = COLUMN_SUB_NAME + " = ?";
        String[] selectionArgs = new String[] {getNameFitness()};
        Cursor c = db.query(sqlSetFits.DATABASE_TABLE, null, selection, selectionArgs, null, null, null);

        //перебрати рядки курсору
        if(c.moveToNext()){
            int id_id = c.getColumnIndex(sqlSetFits.COLUMN_ID);
            int id_fitName = c.getColumnIndex(COLUMN_SUB_NAME);
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
                setsFitness.add(   new OneSet( id, new Exercise(exe) , new ExecutedExercise(new Weight(wei) , new Repeats(rep)) , fitName  )   );

            } while (c.moveToNext());
        } else {
            Toast.makeText(this, "Виконай перший підхід!", Toast.LENGTH_SHORT).show();
        }
        //повернути список об'єктів тренувань
        return setsFitness;
    }

    private void updateRecycler(){
        List<OneSet> setsFitness = getSetsFitness();
        adapter.setSetOneSetList(setsFitness);
        adapter.notifyDataSetChanged();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.add);
        item.setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обробка натискання пунктів меню
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
                OneSet oneSet = new OneSet(nameFitness);
                sqlSetFits.addRow(oneSet);
                updateRecycler();
                return true;
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
                sqlSetFits.getTableInLog("MainActSQLog", nameFitness);
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

            case R.id.delete_set:
                OneSet oneSet = adapter.getOneSet(positionForContextMenu);
                //
                String query = "DELETE FROM " + SQLSetFits.DATABASE_TABLE + " WHERE " + SQLSetFits.COLUMN_ID + " = " + oneSet.getId();
                db.execSQL(query);
                //
                adapter.setSetOneSetList(getSetsFitness());
                //
                adapter.notifyDataSetChanged();

                return true;

            default:
                return super.onContextItemSelected(item);
    }

}

    public void setPosition(int position) {
        positionForContextMenu = position;
    }




    /**************  Save DATA TO DB ****************/
    public void saveToDBFromRecycler(RecyclerViewOneSetsAdapter recyclerViewOneSetsAdapter){
        //
        ContentValues cv = new ContentValues();
        //
        List<OneSet> setsFitness = recyclerViewOneSetsAdapter.getSetOneSetList();
        //
        String where_clause = SQLfitness.COLUMN_SUB_NAME + "=?";
        String[] where_args = new String[] { nameFitness };
        Cursor cursor = db.query(sqlSetFits.DATABASE_TABLE, null,where_clause,where_args,null,null,null);
        cursor.moveToFirst();
        //
        if(cursor.getCount() == setsFitness.size()) {
            for (OneSet oneSet : setsFitness) {
                //get data from setFitness
                String exe  = oneSet.getExe().toString();
                int weight  = oneSet.getRecordSet().getWeight().getIntWeight();
                int repeats = oneSet.getRecordSet().getRepeats().getIntRepeats();
                int id      = oneSet.getId();
                //put data in content
                cv.put(sqlSetFits.COLUMN_EXE, exe);
                cv.put(sqlSetFits.COLUMN_WEIGHT, weight);
                cv.put(sqlSetFits.COLUMN_REPEATS, repeats);
                //
                @SuppressLint("Range") int id1 = cursor.getInt(cursor.getColumnIndex(sqlSetFits.COLUMN_ID));
                Log.d("isCounts",  id + " " + exe + " " + weight + " " + repeats + " " + id1);
                if(id == id1) db.update(sqlSetFits.DATABASE_TABLE, cv, sqlSetFits.COLUMN_ID + " = ?" , new String[] {String.valueOf(id)} );
                //
                cv.clear();
                cursor.moveToNext();
            }
        }
    }





    public void toast(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
}