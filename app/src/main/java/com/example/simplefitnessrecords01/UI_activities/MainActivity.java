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

import com.example.simplefitnessrecords01.dialog.ButtonOK;
import com.example.simplefitnessrecords01.sql.SQLfitness;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.fitness.Fitness;
import com.example.simplefitnessrecords01.activityResultContracts.MyActivityResultContract;
import com.example.simplefitnessrecords01.databinding.ActivityMainBinding;
import com.example.simplefitnessrecords01.dialog.StartDialog;
import com.example.simplefitnessrecords01.recycler_views.FitnessListAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetterDB {




    //Біндінг цього актівіті
    private ActivityMainBinding binding;




    //Запускач інших актівіті із цього актівіті
    private ActivityResultLauncher activityResultLauncher;
    //метод гетер для торимання посилання на запускач актівіті
    public ActivityResultLauncher getActivityResultLauncher() {
        return activityResultLauncher;
    }







    //посилання на Помічник по роботі з базою даних
    SQLfitness dbHelp;
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
        Log.d("loga", "ON CREATE IN MainActivity" );
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //ініціалізація посилань на базу даних
        dbHelp = new SQLfitness(MainActivity.this);
        db     = dbHelp.getWritableDatabase();


        //ланчер з обробником
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

        //ініціалізація recycleView
        recycleViewInit();
    }
    @Override
    protected void onPause() {
        super.onPause();
//        db.close();
//        dbHelp.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }






    /**********  RecyclerView ***************/

    //метод створює список імен баз даних
    private List<Fitness>  getFitnessList(){

        //пустий список для SetTraining з бази для рециклера
        List<Fitness> fitnessList = new ArrayList<>();

        //курсор з бази з вибором усього
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLfitness.DATABASE_TABLE, null);

        //перебрати рядки курсору
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex(SQLfitness.COLUMN_ID));
                @SuppressLint("Range")
                String day = cursor.getString(cursor.getColumnIndex(SQLfitness.COLUMN_DAY));
                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(SQLfitness.COLUMN_NAME));
                @SuppressLint("Range")
                String subname = cursor.getString(cursor.getColumnIndex(SQLfitness.COLUMN_FITNAME));

                //добавити в список
                fitnessList.add(   new Fitness(id, day, name, subname)   );
            } while (cursor.moveToNext());
        } else {
            //Якщо база порожня то запуск діалогу
            StartDialog dialog = new StartDialog(this, txts -> {
                ContentValues cv = new ContentValues();
                cv.put(SQLfitness.COLUMN_DAY,    txts[0]);
                cv.put(SQLfitness.COLUMN_NAME,   txts[1]);
                cv.put(SQLfitness.COLUMN_FITNAME,txts[2]);
                db.insert(SQLfitness.DATABASE_TABLE,null,cv);
                cv.clear();
                recycleViewInit();
                });
            //показати діалог
            dialog.show();

        }
        //повернути список об'єктів тренувань
        return fitnessList;
    }

    //ініціалізація recycleView списку створених тренувань фітнесу
    private void recycleViewInit() {
        //дістати setTrainingList з бази даних
        List<Fitness> fitnessList = getFitnessList();

        //створити новий адаптер з цим списком
        FitnessListAdapter adapter = new FitnessListAdapter(fitnessList, this);

        //менеджер відображення елементів передаємо
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //встановлюємо наш слухач оброблень короткого натискання і закодуємо саме оброблення
        adapter.setOnItemRecyclerClickListener((position) -> {

            //створимо масив з інфою про тренування при натисканні на елемент рециклера
            String[] dayNameSubname = null;

            //курсор
            Cursor c = db.query(SQLfitness.DATABASE_TABLE,null,null,null,null,null,null);
            if(c.moveToPosition(position)) {
                @SuppressLint("Range") String day =     c.getString(c.getColumnIndex(SQLfitness.COLUMN_DAY));
                @SuppressLint("Range") String name =    c.getString(c.getColumnIndex(SQLfitness.COLUMN_NAME));
                @SuppressLint("Range") String subname = c.getString(c.getColumnIndex(SQLfitness.COLUMN_FITNAME));

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
                StartDialog dialog = new StartDialog(this, txts -> {
                    String selection = SQLfitness.COLUMN_DAY + "=? AND " + SQLfitness.COLUMN_NAME + "=? AND " + SQLfitness.COLUMN_FITNAME + "=?";
                    String[] selectionArgs = {txts[0] , txts[1] , txts[2]};

                    Cursor с = db.query(SQLfitness.DATABASE_TABLE,null,selection, selectionArgs,null,null,null);

                    if (  с.moveToFirst()  ) {
                        Toast.makeText(MainActivity.this, "Рядок уже є", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues cv = new ContentValues();
                        cv.put(SQLfitness.COLUMN_DAY,    txts[0]);
                        cv.put(SQLfitness.COLUMN_NAME,   txts[1]);
                        cv.put(SQLfitness.COLUMN_FITNAME,txts[2]);
                        db.insert(SQLfitness.DATABASE_TABLE,null,cv);
                        cv.clear();
                    }
                    //оновити список
                    recycleViewInit();
                });
                dialog.show();
                return true;

            case R.id.action_settings:
                // Обробка натискання на пункт "Налаштування"
                return true;

            case R.id.action_log_sql:
                dbHelp.getTableInLog("MainActSQLog");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }









    /************************ Context Menu  *****************************/
    //Позиція елемента в РециклерВ'ю, використовується для контекстного меню, щоб оприділити який елемент був натиснений
    private int positioContextMenu = -1;

    //використовується при довгому натисненні на РециклерВ'ю, щоб закинути номер посиції на оброблення контекстного меню
    public void setPositioContextMenu(int posit) {
        positioContextMenu = posit;
    }

    // Реалізація методу для обробки натискань на пункти контекстного меню
    @Override
    public boolean onContextItemSelected(MenuItem item)       {

        // Обробляємо натискання на пункти контекстного меню
        switch (item.getItemId()) {

            case R.id.edit:
                //Обробник натискання кнопки ОК діалогу
                @SuppressLint("Range") ButtonOK buttonOK = dayNameSubname -> {
                    //get strings from dialog
                    ContentValues cv = new ContentValues();
                    cv.put(SQLfitness.COLUMN_DAY, dayNameSubname[0]);
                    cv.put(SQLfitness.COLUMN_NAME, dayNameSubname[1]);
                    cv.put(SQLfitness.COLUMN_FITNAME, dayNameSubname[2]);
                    //get cursor from table sql
                    Cursor c = db.query(SQLfitness.DATABASE_TABLE,null,null,null,null,null,null);
                    //put cursor to chosen position of recycler
                    if(c.moveToPosition(positioContextMenu)) {
                        //update the table in selected id parameter
                        int id = c.getInt(c.getColumnIndexOrThrow(SQLfitness.COLUMN_ID));
                        String whereClause = SQLfitness.COLUMN_ID + " = ?";
                        String[] whereArgs = new String[] {Integer.toString(id)};
                        db.update(SQLfitness.DATABASE_TABLE, cv, whereClause, whereArgs);
                        cv.clear();
                        //from db to adapter update data
                        ((FitnessListAdapter)binding.recyclerView.getAdapter()).setSetTrainingList(getFitnessList());
                        //notify adapter
                        binding.recyclerView.getAdapter().notifyDataSetChanged();
                    }
                };

                StartDialog dialog = new StartDialog(this, buttonOK);
                dialog.show();

                return true;


            case R.id.delete:
                //адаптер
                FitnessListAdapter fitnessListAdapter = (FitnessListAdapter) binding.recyclerView.getAdapter();
                fitnessListAdapter.deleteItem(positioContextMenu);

                //from db to adapter update data
                ((FitnessListAdapter)binding.recyclerView.getAdapter()).setSetTrainingList(getFitnessList());
                //notify adapter
                binding.recyclerView.getAdapter().notifyDataSetChanged();

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }


}