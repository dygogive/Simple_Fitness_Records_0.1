package com.example.simplefitnessrecords01.UI_activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.simplefitnessrecords01.dialog.DialogOK;
import com.example.simplefitnessrecords01.sql.MyDatabaseHelper;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.fitness.SetTraining;
import com.example.simplefitnessrecords01.activityResultContracts.MyActivityResultContract;
import com.example.simplefitnessrecords01.databinding.ActivityMainBinding;
import com.example.simplefitnessrecords01.dialog.CustomDialog;
import com.example.simplefitnessrecords01.recycler_views.SetTrainingAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActivityResultLauncher activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        //Запускач Актівіті з результатом
        activityResultLauncher = registerForActivityResult(new MyActivityResultContract(),
                result -> {
                    if (result) {
                        // Обробіть результат з активності
                        Toast.makeText(this, "Завершено тренування !!!!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Не завершена тренування", Toast.LENGTH_SHORT).show();
                        // Обробіть помилку
                    }
                }
        );
    }

    private int position;

    public void setPosition(int posit) {
        position = posit;
    }


    public ActivityResultLauncher getActivityResultLauncher() {
        return activityResultLauncher;
    }



    @Override
    protected void onResume() {
        super.onResume();

        //ініціалізація recycleView
        recycleViewInit();
    }


    private List<SetTraining> trainingList(){
        //ідентифікувати помічник і базу
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //пустий список для SetTraining з бази для рециклера
        List<SetTraining> trainingListist = new ArrayList<>();
        //курсор з бази
        Cursor cursor = db.rawQuery("SELECT * FROM " + MyDatabaseHelper.DATABASE_TABLE, null);
        //перебрати рядки курсору якщо в базі є записи
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_ID));
                @SuppressLint("Range")
                String day = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_DAY));
                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_NAME));
                @SuppressLint("Range")
                String subname = cursor.getString(cursor.getColumnIndex(MyDatabaseHelper.COLUMN_SUBNAME));
                trainingListist.add(new SetTraining(id, day, name, subname));
            } while (cursor.moveToNext());
        } else {
            //Якщо база порожня то виконати наступне
            CustomDialog dialog = new CustomDialog(this, txts -> {MainActivity.this.getActivityResultLauncher().launch(txts);});
            dialog.show();
        }
        cursor.close();
        db.close();
        return trainingListist;
    }

    //ініціалізація recycleView
    private void recycleViewInit() {
        //дістати setTrainingList з бази даних
        List<SetTraining> setTrainingList = trainingList();

        //створити новий адаптер
        SetTrainingAdapter adapter = new SetTrainingAdapter(setTrainingList, this);

        //менеджер відображення елементів передаємо
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //встановлюємо наш слухач оброблень короткого натискання
        adapter.setOnItemRecyclerClickListener(setTraining -> {
            //реакція на натискання на елемент рециклер тут
            Toast.makeText(this, setTraining.getDay(), Toast.LENGTH_SHORT).show();
        });

        //передати адаптер
        binding.recyclerView.setAdapter(adapter);
    }





    /*
     * Викличте MenuInflater в методі onCreateOptionsMenu()
     * вашої діяльності або фрагмента. Ось код для діяльності:
     * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Роздування ресурсу меню з використанням MenuInflater
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.action_new:
                // Обробка натискання на пункт "NEW"
                CustomDialog dialog = new CustomDialog(this,txts -> {MainActivity.this.getActivityResultLauncher().launch(txts);});
                dialog.show();
                return true;
            case R.id.action_settings:
                // Обробка натискання на пункт "Налаштування"
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }








    // Реалізація методу для обробки натискань на пункти контекстного меню
    @Override
    public boolean onContextItemSelected(MenuItem item)       {

        // Обробляємо натискання на пункти контекстного меню
        switch (item.getItemId()) {

            case R.id.edit:
                // Додаткові дії при натисканні на пункт "Редагувати"

                //Обробник натискання кнопки ОК діалогу
                @SuppressLint("Range") DialogOK dialogOK = dayNameSubname -> {
                    Log.d("log12",dayNameSubname[0] + " / " + dayNameSubname[1] + " / " + dayNameSubname[2]);
                    MyDatabaseHelper dbHelp = new MyDatabaseHelper(MainActivity.this);
                    SQLiteDatabase db = dbHelp.getWritableDatabase();

                    ContentValues cv = new ContentValues();
                    cv.put(MyDatabaseHelper.COLUMN_DAY, dayNameSubname[0]);
                    cv.put(MyDatabaseHelper.COLUMN_NAME, dayNameSubname[1]);
                    cv.put(MyDatabaseHelper.COLUMN_SUBNAME, dayNameSubname[2]);
                    Cursor c = db.query(MyDatabaseHelper.DATABASE_TABLE,null,null,null,null,null,null);
                    if(c.moveToPosition(position)) {
                        int id = c.getInt(c.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_ID));
                        String whereClause = MyDatabaseHelper.COLUMN_ID + " = ?";
                        String[] whereArgs = new String[] {Integer.toString(id)};
                        int rowsUpd = db.update(MyDatabaseHelper.DATABASE_TABLE, cv, whereClause, whereArgs);
                        cv.clear();

                        db.close();
                        dbHelp.close();

                        ((SetTrainingAdapter)binding.recyclerView.getAdapter()).setSetTrainingList(trainingList());
                        binding.recyclerView.getAdapter().notifyDataSetChanged();

                    }
                };
                CustomDialog dialog = new CustomDialog(this, dialogOK);
                dialog.show();

                return true;

            case R.id.tr_red:

                String[] dayNameSubname = null;

                MyDatabaseHelper dbHelp = new MyDatabaseHelper(MainActivity.this);
                SQLiteDatabase db = dbHelp.getWritableDatabase();

                Cursor c = db.query(MyDatabaseHelper.DATABASE_TABLE,null,null,null,null,null,null);
                if(c.moveToPosition(position)) {
                    String day =     c.getString(c.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_DAY));
                    String name =    c.getString(c.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_NAME));
                    String subname = c.getString(c.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_SUBNAME));


                    db.close();
                    dbHelp.close();

                    dayNameSubname = new String[] {day,name,subname};

                }

                MainActivity.this.getActivityResultLauncher().launch(dayNameSubname);

                return true;

            case R.id.delete:
                //адаптер
                SetTrainingAdapter setTrainingAdapter = (SetTrainingAdapter) binding.recyclerView.getAdapter();
                //видалити
                setTrainingAdapter.deleteItem(position);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }




}