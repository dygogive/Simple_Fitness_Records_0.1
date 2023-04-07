package com.example.simplefitnessrecords01.UI_activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.simplefitnessrecords01.dialog.DeleteDialog;
import com.example.simplefitnessrecords01.dialog.DialogOnClick;
import com.example.simplefitnessrecords01.dialog.DialogUniqueNameProcessor;
import com.example.simplefitnessrecords01.sql.SQLhelper;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.fitness.OneFitnessTraining;
import com.example.simplefitnessrecords01.activityResultContracts.MyActivityResultContract;
import com.example.simplefitnessrecords01.databinding.ActivityMainBinding;
import com.example.simplefitnessrecords01.dialog.StartDialog;
import com.example.simplefitnessrecords01.recycler_views.RecyclerViewFitnessTrainingsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //references
    private ActivityMainBinding binding;

    //Triggers other activities from this activity
    private ActivityResultLauncher activityResultLauncher;

    //link to the Database Assistant
    SQLhelper sqLhelper;
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

        //set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Trainings");
        actionBar.setSubtitle("Create new training");

        //binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        //initialization of links to databases
        sqLhelper = new SQLhelper(MainActivity.this);


        //database sqLfitness
        db     = sqLhelper.getWritableDatabase();


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
        sqLhelper.close();
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
            Cursor c = db.query(SQLhelper.TABLE_TRAININGS,null,null,null,null,null,null);
            //
            if(c.moveToPosition(position)) {
                @SuppressLint("Range") String day =     c.getString(c.getColumnIndex(SQLhelper.COLUMN_DAY));
                @SuppressLint("Range") String name =    c.getString(c.getColumnIndex(SQLhelper.COLUMN_NAME));
                @SuppressLint("Range") String subname = c.getString(c.getColumnIndex(SQLhelper.COLUMN_INFO));

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
        Cursor cursor = db.rawQuery("SELECT * FROM " + SQLhelper.TABLE_TRAININGS, null);

        //iterate through the cursor lines
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex(SQLhelper.COLUMN_ID));
                @SuppressLint("Range")
                String day = cursor.getString(cursor.getColumnIndex(SQLhelper.COLUMN_DAY));
                @SuppressLint("Range")
                String name = cursor.getString(cursor.getColumnIndex(SQLhelper.COLUMN_NAME));
                @SuppressLint("Range")
                String subname = cursor.getString(cursor.getColumnIndex(SQLhelper.COLUMN_INFO));

                //add a row from the database to the list
                oneFitnessTrainingList.add(   new OneFitnessTraining(id, day, name, subname)   );
            } while (cursor.moveToNext());
        } else {

            //If the base is empty, then the dialog is started
            StartDialog dialog = new StartDialog(this, txts -> {
                ContentValues cv = new ContentValues();
                cv.put(SQLhelper.COLUMN_DAY,    txts[0]);
                cv.put(SQLhelper.COLUMN_NAME,   txts[1]);
                cv.put(SQLhelper.COLUMN_INFO,txts[2]);
                db.insert(SQLhelper.TABLE_TRAININGS,null,cv);
                cv.clear();
                recycleViewInit();
                });

            //show dialogue
            dialog.show();
        }
        //повернути список об'єктів тренувань
        return oneFitnessTrainingList;
    }

    //update recycler view
    private void updateRecyclerView(){
        //from db to adapter update data
        ((RecyclerViewFitnessTrainingsAdapter)binding.recyclerView.getAdapter()).setFitnessList(getFitnessList());
        //notify adapter
        binding.recyclerView.getAdapter().notifyDataSetChanged();
    }

    //getUniqueName
    private String getUniqueName(int position) {
        //адаптер
        RecyclerViewFitnessTrainingsAdapter recyclerViewFitnessTrainingsAdapter =
                (RecyclerViewFitnessTrainingsAdapter) binding.recyclerView.getAdapter();

        //unique Name To Delete
        OneFitnessTraining training = recyclerViewFitnessTrainingsAdapter.getItem(positioContextMenu);
        String uniqueName = training.getDay() + training.getName() + training.getInfo();

        return uniqueName;
    }
    private String[] getUniqueNameArray(int position) {
        //адаптер
        RecyclerViewFitnessTrainingsAdapter recyclerViewFitnessTrainingsAdapter =
                (RecyclerViewFitnessTrainingsAdapter) binding.recyclerView.getAdapter();

        //unique Name To Delete
        OneFitnessTraining training = recyclerViewFitnessTrainingsAdapter.getItem(positioContextMenu);

        String[] uniqueName = new String[3];

        uniqueName[0] = training.getDay();
        uniqueName[1] = training.getName();
        uniqueName[2] = training.getInfo();

        return uniqueName;
    }







    /********** Options Menu **************/
    //Creating a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflating the menu resource using MenuInflater
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return super.onPrepareOptionsMenu(menu);
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
                    String selection = SQLhelper.COLUMN_DAY + "=? AND " + SQLhelper.COLUMN_NAME + "=? AND " + SQLhelper.COLUMN_INFO + "=?";
                    String[] selectionArgs = {txts[0] , txts[1] , txts[2]};

                    Cursor с = db.query(SQLhelper.TABLE_TRAININGS,null,selection, selectionArgs,null,null,null);

                    if (  с.moveToFirst()  ) {
                        Toast.makeText(MainActivity.this, "Рядок уже є", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues cv = new ContentValues();
                        cv.put(SQLhelper.COLUMN_DAY,    txts[0]);
                        cv.put(SQLhelper.COLUMN_NAME,   txts[1]);
                        cv.put(SQLhelper.COLUMN_INFO,txts[2]);
                        db.insert(SQLhelper.TABLE_TRAININGS,null,cv);
                        cv.clear();
                    }
                    //оновити список
                    recycleViewInit();
                });
                dialog.show();
                return true;

            case R.id.action_settings: openSettingsLayout();
                return true;

            case R.id.action_log_sql:
                //show table SQL in LOG
                sqLhelper.getTableInLogTrainings("MainActSQLog");
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
            case R.id.edit: changeDataOfItemAcrossDialog();  //change names
                return true;
            case R.id.delete: deleteOneItemInDatabases();  //delete training
                return true;
            default:
                return super.onContextItemSelected(item);  //default
        }
    }







    /***************  DIALOG   ***************/

    //change data of item
    private void changeDataOfItemAcrossDialog(){
        //The processor of received information from the dialog
        @SuppressLint("Range") DialogUniqueNameProcessor dialogUniqueNameProcessor = dayNameInfo -> {
            //update info in database 1
            sqLhelper.updateRowTrainings(positioContextMenu, dayNameInfo);

            //update info in database 2
            sqLhelper.updateRowSets(getUniqueName(positioContextMenu) ,dayNameInfo[0] + dayNameInfo[1] + dayNameInfo[2]);

            //update RecyclerView
            updateRecyclerView();
        };
        //get OneFitnessTraining from database
        String[] row = sqLhelper.getRowTrainings(positioContextMenu);
        //create dayNameInfo
        String[] dayNameInfo;
        if(row.length == 4)
            dayNameInfo = Arrays.copyOfRange(row,1,4);
        else {
            dayNameInfo = new String[]{"error", "error", "error"};
            Toast.makeText(this, "error: " + "if(row.length == 4)", Toast.LENGTH_SHORT).show();
        }

        //Start Dialog
        StartDialog dialog = new StartDialog(this, dialogUniqueNameProcessor, dayNameInfo);
        // show Dialog
        dialog.show();
    }







    /******************** DATABASES **************************/
    //delete item from database and from recycler view
    private void deleteOneItemInDatabases(){

        //RUN delete for dialog
        DialogOnClick dialogOnClick = () -> {
            //delete from the database of Fitness Trainings
            sqLhelper.deleteRowTrainings(getUniqueNameArray(positioContextMenu));

            Log.d("howDel" , getUniqueName(positioContextMenu));
            //delete from the database of performed sets
            sqLhelper.deleteRowSets(getUniqueName(positioContextMenu));

            updateRecyclerView();
        };

        //show dialog DELETE OR CANCEL
        new DeleteDialog(MainActivity.this, dialogOnClick).show();

    }










    /********************** SETTINGS OF THE PROGRAM ************************/
    private void openSettingsLayout(){
        Intent intent = new Intent(MainActivity.this,  SettingsActivity.class);
        startActivity(intent);
    }

}