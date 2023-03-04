package com.example.simplefitnessrecords01.UI_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
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
import android.view.View;
import android.widget.Toast;

import com.example.simplefitnessrecords01.fitness.Exercise;
import com.example.simplefitnessrecords01.fitness.RecordSet;
import com.example.simplefitnessrecords01.fitness.Repeats;
import com.example.simplefitnessrecords01.fitness.SetFit;
import com.example.simplefitnessrecords01.fitness.Weight;
import com.example.simplefitnessrecords01.recycler_views.AdapterSets;
import com.example.simplefitnessrecords01.sql.MyDatabaseHelper;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivitySetBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SetActivity extends AppCompatActivity {
    //біндінг
    private ActivitySetBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.recyclerAddSet.setLayoutManager(new LinearLayoutManager(this));
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

        //обробити інфу від екстра чз інтент
        getIntentExtra();
    }

    private void getIntentExtra() {
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
//        sb.append("День тренування: " + textDay).append("\n").append("Назва тренування: ").append(textName).
//                append("\n").append("Підназва тренування: "). append(textSubname);
        binding.tvDay.setText(textDay);
        binding.tvName.setText(textName);
        binding.tvSubName.setText(textSubname);


    }





    /*
     * Викличте MenuInflater в методі onCreateOptionsMenu()
     * вашої діяльності або фрагмента. Ось код для діяльності:
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Роздування ресурсу меню з використанням MenuInflater
        getMenuInflater().inflate(R.menu.menu_training, menu);
        return true;
    }
    /*Додайте обробник події для пунктів меню, які ви хочете обробити.
     Для цього використовуйте метод onOptionsItemSelected().
      Ось код для діяльності:*/

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