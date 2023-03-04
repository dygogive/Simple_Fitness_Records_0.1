package com.example.simplefitnessrecords01.UI_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.simplefitnessrecords01.fitness.Exercise;
import com.example.simplefitnessrecords01.fitness.RecordSet;
import com.example.simplefitnessrecords01.fitness.Repeats;
import com.example.simplefitnessrecords01.fitness.SetFit;
import com.example.simplefitnessrecords01.fitness.Weight;
import com.example.simplefitnessrecords01.recycler_views.AdapterSets;
import com.example.simplefitnessrecords01.sql.MyDatabaseHelper;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivitySetBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SetActivity extends AppCompatActivity {


    //біндінг
    private ActivitySetBinding binding;

    //посилання на Помічник по роботі з базою даних
    MyDatabaseHelper dbHelp;
    //посилання на База даних
    SQLiteDatabase db;






    /**********  Activity Lifecycle ***************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        //обробити інфу від екстра чз інтент
        procIntentExtra();

    }

    //метод життєвого циклу актівіті
    @Override
    protected void onResume() {
        super.onResume();

        //ініціалізація посилань на базу даних
        dbHelp = new MyDatabaseHelper(SetActivity.this);
        db     = dbHelp.getWritableDatabase();

        //ініціалізація Рециклера в'ю
        recycleViewInit();
    }

    @Override
    protected void onPause() {
        //Закрити базу
        db.close();

        super.onPause();
    }





    /**********  RecyclerView ***************/
    private void recycleViewInit() {

        //менеджер для РЕЦИКЛЕРА В'Ю
        binding.recyclerAddSet.setLayoutManager(new LinearLayoutManager(this));
        //Адаптер для рециклера
        binding.recyclerAddSet.setAdapter(new AdapterSets(this));


        //записати тестові дані в рециклер
        List<SetFit> setFitList1 = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            Random random = new Random();
            Weight weight   = new Weight(random.nextInt(100));
            Repeats repeats = new Repeats(random.nextInt(8+4));
            SetFit setFit = new SetFit(
                    new Exercise("Exercise "+ i),
                    new RecordSet(weight,repeats)
            );
            setFitList1.add(setFit);
        }
        ((AdapterSets)binding.recyclerAddSet.getAdapter()).setSetFitList(setFitList1);
    }







    /******************* IntentExtra **************************/


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
        //записати ці строки на екран
        StringBuilder sb = new StringBuilder();

        //показати тексти на екрані
        binding.tvDay.setText(textDay);
        binding.tvName.setText(textName);
        binding.tvSubName.setText(textSubname);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}