package com.example.simplefitnessrecords01.UI_activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

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

import com.example.simplefitnessrecords01.sql.MyDatabaseHelper;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivitySetBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SetActivity extends AppCompatActivity {
    //біндінг
    private ActivitySetBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Creating FAB
        // Знаходимо FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ваш код, що виконується після натискання кнопки
            }
        });








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
        sb.append("День тренування: " + textDay).append("\n").append("Назва тренування: ").append(textName).
                append("\n").append("Підназва тренування: "). append(textSubname);
        binding.textMultiLine.setText(sb.toString());

        //ідентифікувати помічник і базу
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = MyDatabaseHelper.COLUMN_DAY + "=? AND " + MyDatabaseHelper.COLUMN_NAME + "=? AND " + MyDatabaseHelper.COLUMN_SUBNAME + "=?";
        String[] selectionArgs = {textDay , textName , textSubname};
        Cursor с = db.query(MyDatabaseHelper.DATABASE_TABLE,new String[]{"id"},selection, selectionArgs,null,null,null);
        if (  !с.moveToFirst()  ) {
            ContentValues cv = new ContentValues();
            cv.put("day", textDay);
            cv.put("name", textName);
            cv.put("subname", textSubname);

            db.insert("sets", null, cv);
        } else Toast.makeText(SetActivity.this, "Рядок уже є", Toast.LENGTH_SHORT).show();
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