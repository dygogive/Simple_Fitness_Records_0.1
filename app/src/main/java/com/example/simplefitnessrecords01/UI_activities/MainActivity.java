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

import com.example.simplefitnessrecords01.dialog.UniqueNameProcessor;
import com.example.simplefitnessrecords01.sql.SQLSetFits;
import com.example.simplefitnessrecords01.sql.SQLfitness;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.fitness.OneFitnessTraining;
import com.example.simplefitnessrecords01.activityResultContracts.MyActivityResultContract;
import com.example.simplefitnessrecords01.databinding.ActivityMainBinding;
import com.example.simplefitnessrecords01.dialog.StartDialog;
import com.example.simplefitnessrecords01.recycler_views.RecyclerViewFitnessTrainingsAdapter;

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
    SQLfitness sqLfitness;
    SQLSetFits sqlSetFits;
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

        //ініціалізація посилань на бази даних
        sqLfitness = new SQLfitness(MainActivity.this);
        sqlSetFits = new SQLSetFits(MainActivity.this);

        db     = sqLfitness.getWritableDatabase();


        //ланчер з обробником
        activityResultLauncher = registerForActivityResult(new MyActivityResultContract(),
                result -> {
                    if (result) {
                        // Обробіть результат з активності
                        Toast.makeText(this, "End training", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "End training", Toast.LENGTH_SHORT).show();
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
    protected void onDestroy() {
        super.onDestroy();

        //close dadabases
        sqLfitness.close();
        sqlSetFits.close();
    }







    /**********  RecyclerView ***************/

    //метод створює список імен баз даних
    private List<OneFitnessTraining>  getFitnessList(){

        //пустий список для SetTraining з бази для рециклера
        List<OneFitnessTraining> oneFitnessTrainingList = new ArrayList<>();

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
                String subname = cursor.getString(cursor.getColumnIndex(SQLfitness.COLUMN_UNIQ_NAME));

                //добавити в список
                oneFitnessTrainingList.add(   new OneFitnessTraining(id, day, name, subname)   );
            } while (cursor.moveToNext());
        } else {
            //Якщо база порожня то запуск діалогу
            StartDialog dialog = new StartDialog(this, txts -> {
                ContentValues cv = new ContentValues();
                cv.put(SQLfitness.COLUMN_DAY,    txts[0]);
                cv.put(SQLfitness.COLUMN_NAME,   txts[1]);
                cv.put(SQLfitness.COLUMN_UNIQ_NAME,txts[2]);
                db.insert(SQLfitness.DATABASE_TABLE,null,cv);
                cv.clear();
                recycleViewInit();
                });
            //показати діалог
            dialog.show();

        }
        //повернути список об'єктів тренувань
        return oneFitnessTrainingList;
    }




    //ініціалізація recycleView списку створених тренувань фітнесу
    private void recycleViewInit() {
        //дістати setTrainingList з бази даних
        List<OneFitnessTraining> oneFitnessTrainingList = getFitnessList();

        //створити новий адаптер з цим списком
        RecyclerViewFitnessTrainingsAdapter adapter = new RecyclerViewFitnessTrainingsAdapter(oneFitnessTrainingList, this);

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
                @SuppressLint("Range") String subname = c.getString(c.getColumnIndex(SQLfitness.COLUMN_UNIQ_NAME));

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
                    String selection = SQLfitness.COLUMN_DAY + "=? AND " + SQLfitness.COLUMN_NAME + "=? AND " + SQLfitness.COLUMN_UNIQ_NAME + "=?";
                    String[] selectionArgs = {txts[0] , txts[1] , txts[2]};

                    Cursor с = db.query(SQLfitness.DATABASE_TABLE,null,selection, selectionArgs,null,null,null);

                    if (  с.moveToFirst()  ) {
                        Toast.makeText(MainActivity.this, "Рядок уже є", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues cv = new ContentValues();
                        cv.put(SQLfitness.COLUMN_DAY,    txts[0]);
                        cv.put(SQLfitness.COLUMN_NAME,   txts[1]);
                        cv.put(SQLfitness.COLUMN_UNIQ_NAME,txts[2]);
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
                sqLfitness.getTableInLog("MainActSQLog");
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
            case R.id.edit: changeDataOfItem();  //change names
                return true;
            case R.id.delete: deleteOneItem();  //delete training
                return true;
            default:
                return super.onContextItemSelected(item);  //default
        }
    }










    /* **************  manipulations with trainings   *********** */

    //change data of item
    private void changeDataOfItem(){
        //The processor of received information from the dialog
        @SuppressLint("Range") UniqueNameProcessor uniqueNameProcessor = dayNameSubname -> {
            //update info in database 1
            sqLfitness.updateRow(positioContextMenu, dayNameSubname);

            //update info in database 2
            sqlSetFits.updateRow(getUniqueName(positioContextMenu) ,dayNameSubname[0] + dayNameSubname[1] + dayNameSubname[2]);

            //update RecyclerView
            updateRecyclerView();
        };

        //Start Dialog
        StartDialog dialog = new StartDialog(this, uniqueNameProcessor);
        // show Dialog
        dialog.show();
    }


    //delete item from database and from recycler view
    private void deleteOneItem(){


        //delete from the database of Fitness Trainings
        sqLfitness.deleteRow(getUniqueName(positioContextMenu));

        //delete from the database of performed sets
        sqlSetFits.deleteRow(getUniqueName(positioContextMenu));

        updateRecyclerView();
    }


    //update recycler view
    private void updateRecyclerView(){
        //from db to adapter update data
        ((RecyclerViewFitnessTrainingsAdapter)binding.recyclerView.getAdapter()).setFitnessList(getFitnessList());
        //notify adapter
        binding.recyclerView.getAdapter().notifyDataSetChanged();
    }

    private String getUniqueName(int position) {
        //адаптер
        RecyclerViewFitnessTrainingsAdapter recyclerViewFitnessTrainingsAdapter = (RecyclerViewFitnessTrainingsAdapter) binding.recyclerView.getAdapter();

        //unique Name To Delete
        String uniqueName = recyclerViewFitnessTrainingsAdapter.getItem(positioContextMenu).getUniqueName();

        return uniqueName;
    }

}