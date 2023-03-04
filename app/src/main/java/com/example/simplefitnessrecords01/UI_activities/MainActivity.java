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
import com.example.simplefitnessrecords01.fitness.TrainingFitness;
import com.example.simplefitnessrecords01.activityResultContracts.MyActivityResultContract;
import com.example.simplefitnessrecords01.databinding.ActivityMainBinding;
import com.example.simplefitnessrecords01.dialog.CustomDialog;
import com.example.simplefitnessrecords01.recycler_views.SetTrainingAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetterDB {

    //Позиція елемента в РециклерВ'ю, використовується для контекстного меню, щоб оприділити який елемент був натиснений
    private int position;

    //використовується при довгому натисненні на РециклерВ'ю, щоб закинути номер посиції на оброблення контекстного меню
    public void setPosition(int posit) {
        position = posit;
    }

    //Біндінг цього актівіті
    private ActivityMainBinding binding;

    //Запускач інших актівіті із цього актівіті
    private ActivityResultLauncher activityResultLauncher;
    //метод гетер для торимання посилання на запускач актівіті
    public ActivityResultLauncher getActivityResultLauncher() {
        return activityResultLauncher;
    }

    //посилання на Помічник по роботі з базою даних
    MyDatabaseHelper dbHelp;
    //посилання на База даних
    SQLiteDatabase db;

    public SQLiteDatabase getDB() {
        return db;
    }

    /**********  Activity Lifecycle ***************/

    //метод життєвого циклу актівіті
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Ініціалізація посилання на Запускач Актівіті з результатом і тут оброблення цього результату
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


    //метод життєвого циклу актівіті
    @Override
    protected void onResume() {
        super.onResume();

        //ініціалізація посилань на базу даних
        dbHelp = new MyDatabaseHelper(MainActivity.this);
        db     = dbHelp.getWritableDatabase();

        //ініціалізація recycleView
        recycleViewInit();
    }

    @Override
    protected void onPause() {

        db.close();

        super.onPause();
    }




    /**********  RecyclerView ***************/

    //метод створює список імен баз даних
    private List<TrainingFitness> trainingList(){

        //пустий список для SetTraining з бази для рециклера
        List<TrainingFitness> trainingListist = new ArrayList<>();

        //курсор з бази з вибором усього
        Cursor cursor = db.rawQuery("SELECT * FROM " + MyDatabaseHelper.DATABASE_TABLE, null);

        //перебрати рядки курсору
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
                trainingListist.add(new TrainingFitness(id, day, name, subname));
            } while (cursor.moveToNext());
        } else {
            //Якщо база порожня то запуск діалогу
            CustomDialog dialog = new CustomDialog(this, txts -> {
                //обробник масиву текстів, отриманих з діалогу
                MainActivity.this.getActivityResultLauncher().launch(txts);});
            //показати діалог
            dialog.show();
        }
        //повернути список об'єктів тренувань
        return trainingListist;
    }

    //ініціалізація recycleView списку створених тренувань фітнесу
    private void recycleViewInit() {
        //дістати setTrainingList з бази даних
        List<TrainingFitness> fitnessList = trainingList();

        //створити новий адаптер з цим списком
        SetTrainingAdapter adapter = new SetTrainingAdapter(fitnessList, this);

        //менеджер відображення елементів передаємо
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //встановлюємо наш слухач оброблень короткого натискання і закодуємо саме оброблення
        adapter.setOnItemRecyclerClickListener(position -> {

            //створимо масив з інфою про тренування при натисканні на елемент рециклера
            String[] dayNameSubname = null;

            //курсор
            Cursor c = db.query(MyDatabaseHelper.DATABASE_TABLE,null,null,null,null,null,null);
            if(c.moveToPosition(position)) {
                @SuppressLint("Range") String day =     c.getString(c.getColumnIndex(MyDatabaseHelper.COLUMN_DAY));
                @SuppressLint("Range") String name =    c.getString(c.getColumnIndex(MyDatabaseHelper.COLUMN_NAME));
                @SuppressLint("Range") String subname = c.getString(c.getColumnIndex(MyDatabaseHelper.COLUMN_SUBNAME));

                //заповнення масиву
                dayNameSubname = new String[] {day,name,subname};

                //запустити актівіті, що представляє процес тренування, передавши йому інформацію про тренування
                MainActivity.this.getActivityResultLauncher().launch(dayNameSubname);

            }else {
                //при помилці запустити тост такий
                Toast.makeText(this, "Немає в БВ позиції - " + position, Toast.LENGTH_SHORT).show();
            }
        });

        //передати адаптер
        binding.recyclerView.setAdapter(adapter);
    }










    /********** Options Menu **************/

    //Створення меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Роздування ресурсу меню з використанням MenuInflater
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*Listener of Options Menu */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Обробка натискання пунктів меню
        int id = item.getItemId();
        switch (id) {
            case R.id.action_new:
                // Обробка натискання на пункт "NEW"
                CustomDialog dialog = new CustomDialog(this,txts -> {
                    //ідентифікувати помічник і базу
                    MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
                    SQLiteDatabase db = dbHelper.getWritableDatabase();

                    String selection = MyDatabaseHelper.COLUMN_DAY + "=? AND " + MyDatabaseHelper.COLUMN_NAME + "=? AND " + MyDatabaseHelper.COLUMN_SUBNAME + "=?";
                    String[] selectionArgs = {txts[0] , txts[1] , txts[2]};
                    Cursor с = db.query(MyDatabaseHelper.DATABASE_TABLE,new String[]{"id"},selection, selectionArgs,null,null,null);
                    if (  !с.moveToFirst()  ) {
                        ContentValues cv = new ContentValues();
                        cv.put("day", txts[0]);
                        cv.put("name", txts[1]);
                        cv.put("subname", txts[2]);
                        db.insert("sets", null, cv);
                    } else Toast.makeText(MainActivity.this, "Рядок уже є", Toast.LENGTH_SHORT).show();
                    db.close();


                    MainActivity.this.getActivityResultLauncher().launch(txts);
                });
                dialog.show();
                return true;
            case R.id.action_settings:
                // Обробка натискання на пункт "Налаштування"
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