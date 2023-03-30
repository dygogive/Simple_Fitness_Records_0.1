package com.example.simplefitnessrecords01.UI_activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    //references
    private ActivityMainBinding binding;

    //Triggers other activities from this activity
    private ActivityResultLauncher activityResultLauncher;

    //link to the Database Assistant
    SQLfitness sqLfitness;
    SQLSetFits sqlSetFits;
    //link to the Database SQLfitness
    SQLiteDatabase db;





    /************** GETTERS SETTERS **********************/
    public SQLiteDatabase getDB() {
        return db;
    }

    //getter method to get the reference to the activity launcher
    public ActivityResultLauncher getActivityResultLauncher() {
        return activityResultLauncher;
    }






    /**********  ACTIVITY LIFECYCLE ***************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialization of links to databases
        sqLfitness = new SQLfitness(MainActivity.this);
        sqlSetFits = new SQLSetFits(MainActivity.this);
        //database sqLfitness
        db     = sqLfitness.getWritableDatabase();


        //luncher with handler
        activityResultLauncher = registerForActivityResult(new MyActivityResultContract(),
                result -> {
                    if (result) {
                        // Обробіть результат з активності
                    } else {
                        // Обробіть помилку
                    }
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();

        //initialize recycleView
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

    //initialization recycleView list of created fitness workouts
    private void recycleViewInit() {
        //get setTrainingList from database
        List<OneFitnessTraining> oneFitnessTrainingList = getFitnessList();

        //create a new adapter with this list
        RecyclerViewFitnessTrainingsAdapter adapter =
                new RecyclerViewFitnessTrainingsAdapter(oneFitnessTrainingList, this);

        //the display manager of the elements is transferred
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set up our listener for short-click processing and code the processing itself
        adapter.setOnItemRecyclerClickListener((position) -> {

            //let's create an array with unique name
            String[] uniqueName = null;

            //cursor
            Cursor c = db.query(SQLfitness.DATABASE_TABLE,null,null,null,null,null,null);
            //
            if(c.moveToPosition(position)) {
                @SuppressLint("Range") String day =     c.getString(c.getColumnIndex(SQLfitness.COLUMN_DAY));
                @SuppressLint("Range") String name =    c.getString(c.getColumnIndex(SQLfitness.COLUMN_NAME));
                @SuppressLint("Range") String subname = c.getString(c.getColumnIndex(SQLfitness.COLUMN_SUB_NAME));

                //array filling
                uniqueName = new String[] {day,name,subname};

                //run the activity SetActivity representing the training process by passing the training information to it
                MainActivity.this.getActivityResultLauncher().launch(uniqueName);

            }else {
                //in case of an error, start the toast like this
                Toast.makeText(this, "Position: " + position + " wrong", Toast.LENGTH_SHORT).show();
            }
        });

        //pass adapter to Recyclerview
        binding.recyclerView.setAdapter(adapter);
    }

    //method creates a list of database names
    private List<OneFitnessTraining>  getFitnessList(){

        //empty list for SetTraining from base for recycler
        List<OneFitnessTraining> oneFitnessTrainingList = new ArrayList<>();

        //cursor from base with selection of all
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLfitness.DATABASE_TABLE, null);

        //iterate through the cursor lines
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex(SQLfitness.COLUMN_ID));
                @SuppressLint("Range")
                String day = cursor.getString(cursor.getColumnIndex(SQLfitness.COLUMN_DAY));
                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(SQLfitness.COLUMN_NAME));
                @SuppressLint("Range")
                String subname = cursor.getString(cursor.getColumnIndex(SQLfitness.COLUMN_SUB_NAME));

                //add a row from the database to the list
                oneFitnessTrainingList.add(   new OneFitnessTraining(id, day, name, subname)   );
            } while (cursor.moveToNext());
        } else {

            //If the base is empty, then the dialog is started
            StartDialog dialog = new StartDialog(this, txts -> {
                ContentValues cv = new ContentValues();
                cv.put(SQLfitness.COLUMN_DAY,    txts[0]);
                cv.put(SQLfitness.COLUMN_NAME,   txts[1]);
                cv.put(SQLfitness.COLUMN_SUB_NAME,txts[2]);
                db.insert(SQLfitness.DATABASE_TABLE,null,cv);
                cv.clear();
                recycleViewInit();
                });

            //show dialogue
            dialog.show();
        }
        //повернути список об'єктів тренувань
        return oneFitnessTrainingList;
    }







    /********** Options Menu **************/
    //Creating a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflating the menu resource using MenuInflater
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Listener of Options Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle clicks on menu items
        int id = item.getItemId();
        switch (id) {
            case R.id.action_new:
                // Handling the click on the "NEW" item
                StartDialog dialog = new StartDialog(this, txts -> {
                    String selection = SQLfitness.COLUMN_DAY + "=? AND " + SQLfitness.COLUMN_NAME + "=? AND " + SQLfitness.COLUMN_SUB_NAME + "=?";
                    String[] selectionArgs = {txts[0] , txts[1] , txts[2]};

                    Cursor с = db.query(SQLfitness.DATABASE_TABLE,null,selection, selectionArgs,null,null,null);

                    if (  с.moveToFirst()  ) {
                        Toast.makeText(MainActivity.this, "Рядок уже є", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues cv = new ContentValues();
                        cv.put(SQLfitness.COLUMN_DAY,    txts[0]);
                        cv.put(SQLfitness.COLUMN_NAME,   txts[1]);
                        cv.put(SQLfitness.COLUMN_SUB_NAME,txts[2]);
                        db.insert(SQLfitness.DATABASE_TABLE,null,cv);
                        cv.clear();
                    }
                    //оновити список
                    recycleViewInit();
                });
                dialog.show();
                return true;

            case R.id.action_settings:
                // Processing clicks on the "Settings" item
                return true;

            case R.id.action_log_sql:
                //show table SQL in LOG
                sqLfitness.getTableInLog("MainActSQLog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }






    /************************ Context Menu  *****************************/

    //The position of the item in the RecyclerView, used for the context menu to identify which item was clicked
    private int positioContextMenu = -1;

    //setter positioContextMenu
    public void setPositioContextMenu(int posit) {
        positioContextMenu = posit;
    }

    // implementation of a method for processing clicks on context menu items
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // We process clicking on the context menu items
        switch (item.getItemId()) {
            case R.id.edit: changeDataOfItem();  //change names
                return true;
            case R.id.delete: deleteOneItem();  //delete training
                return true;
            default:
                return super.onContextItemSelected(item);  //default
        }
    }







    /***************  manipulations with trainings   ***************/

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