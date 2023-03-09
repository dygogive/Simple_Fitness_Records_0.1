package com.example.simplefitnessrecords01.UI_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.simplefitnessrecords01.fitness.SetFitness;
import com.example.simplefitnessrecords01.recycler_views.AdapterSets;
import com.example.simplefitnessrecords01.sql.SetsFitnessDB;
import com.example.simplefitnessrecords01.sql.MyDatabaseHelper;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivitySetBinding;

public class SetActivity extends AppCompatActivity {


    //біндінг
    private ActivitySetBinding binding;

    //посилання на Помічник по роботі з базою даних
    MyDatabaseHelper dbHelp;
    SetsFitnessDB dbHelpThisTraining;
    String unicNameFitness;
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
        dbHelpThisTraining = new SetsFitnessDB(SetActivity.this, unicNameFitness, 1);



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
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Адаптер для рециклера
        binding.recyclerView.setAdapter(new AdapterSets(this));
        //        //записати тестові дані в рециклер
//        List<SetFit> setFitList1 = new ArrayList<>();
//        for (int i = 0; i < 20; i++){
//            Random random = new Random();
//            Weight weight   = new Weight(random.nextInt(100));
//            Repeats repeats = new Repeats(random.nextInt(8+4));
//            SetFit setFit = new SetFit(
//                    new Exercise("Exercise "+ i),
//                    new RecordSet(weight,repeats)
//            );
//            setFitList1.add(setFit);
//        }
//        ((AdapterSets)binding.recyclerAddSet.getAdapter()).setSetFitList(setFitList1);
    }




    /***************** onClick button *******************/
    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnAddExe:{
                ((AdapterSets)binding.recyclerView.getAdapter()).addSet(new SetFitness());
                ((AdapterSets)binding.recyclerView.getAdapter()).notifyDataSetChanged();
            } break;
            default:{

            }
        }
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


        //показати тексти на екрані
        binding.tvDay.setText(textDay);
        binding.tvName.setText(textName);
        binding.tvSubName.setText(textSubname);

        //унікальна назва тренування
        unicNameFitness = getExtraArray[3];
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