package com.example.simplefitnessrecords01.UI_activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.simplefitnessrecords01.databinding.ActivityMainBinding;
import com.example.simplefitnessrecords01.dialogs.DeleteDialog;
import com.example.simplefitnessrecords01.dialogs.DialogOnClick;
import com.example.simplefitnessrecords01.dialogs.DialogUniqueNameProcessor;
import com.example.simplefitnessrecords01.fitness.Workout;
import com.example.simplefitnessrecords01.sql.SQLhelper;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.dialogs.DialogCreateWorkout;
import com.example.simplefitnessrecords01.recycler_adapters.AdapterWorkouts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Біндінг
    private ActivityMainBinding
            binding;
    //Різалт лаунчери
    private ActivityResultLauncher
            activityWorkoutLauncher,
            activityExercisesLauncher;
    //Хелпер БД
    SQLhelper
            sqlHelper;


    //Отримати адаптер на РециклерВ'ю
    private AdapterWorkouts getAdapterRecyclerView(){
        return (AdapterWorkouts) binding.recyclerViewID.getAdapter();
    }

    //Створити унікальне ім'я тренування (
    private String getNameWorkout(int position) {
        //get Workout from position
        Workout workout = getAdapterRecyclerView().getWorkout(position);
        //generosity unique name
        return workout.getDay() + workout.getName() + workout.getInfo();
    }
    private String[] getArrayNameWorkout(int position) {

        //unique Name To Delete
        Workout workout = getAdapterRecyclerView().getWorkout(position);

        String[] uniqueName = new String[3];

        uniqueName[0] = workout.getDay();
        uniqueName[1] = workout.getName();
        uniqueName[2] = workout.getInfo();

        return uniqueName;
    }

























    /**********  ACTIVITY LIFECYCLE ***************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set action bar Title and Subtitle
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Trainings");
        actionBar.setSubtitle("Create new training");

        //initialization of links to databases
        sqlHelper = new SQLhelper(MainActivity.this);

        //luncher for ExerciseSetActivity
        activityWorkoutLauncher = registerForActivityResult(
                new ExerciseSetContract(),
                result -> {}
        );

        //launcher for ExercisesActivity
        activityExercisesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {}
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
        sqlHelper.close();
    }
















    /**********  RecyclerView ***************/

    //initialization recycleView list of created fitness workouts
    private void recycleViewInit() {

        //the display manager of the elements is transferred
        binding.recyclerViewID.setLayoutManager(new LinearLayoutManager(this));

        //get setTrainingList from database
        List<Workout> workoutList = getWorkoutList();

        //create My adapter with Workout list
        AdapterWorkouts adapter = new AdapterWorkouts(workoutList, this);

        //pass adapter to Recyclerview
        binding.recyclerViewID.setAdapter(adapter);

        //launcher Workout from RecView
        adapter.setOnItemRecyclerClickListener((position) -> {
            activityWorkoutLauncher.launch(getArrayNameWorkout(position));
        });

    }

    //update recycler view
    private void updateListInRecyclerView(){
        //from db to adapter update data
        ((AdapterWorkouts)binding.recyclerViewID.getAdapter()).setWorkoutList(getWorkoutList());
        //notify adapter
        binding.recyclerViewID.getAdapter().notifyDataSetChanged();
    }


    //method creates a list of database names
    @SuppressLint("Range")
    public List<Workout> getWorkoutList(){

        //empty list for SetTraining from base for recycler
        List<Workout> workoutList = new ArrayList<>();

        //cursor from base with selection of all
        Cursor cursor = sqlHelper.getWritableDatabase().rawQuery("SELECT * FROM " + SQLhelper.TABLE_ONEGYM, null);

        //Strings
        String day = "", name = "", info = "";

        //iterate through the cursor lines
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                int id = cursor.getInt(cursor.getColumnIndex(SQLhelper.COLUMN_ID));
                day = cursor.getString(cursor.getColumnIndex(SQLhelper.COLUMN_DAY));
                name = cursor.getString(cursor.getColumnIndex(SQLhelper.COLUMN_NAME));
                info = cursor.getString(cursor.getColumnIndex(SQLhelper.COLUMN_INFO));
                //add a row from the database to the list
                workoutList.add(   new Workout(id, day, name, info)   );
            } while (cursor.moveToNext());
            return workoutList;
        } else {
            //If the base is empty, then the dialog is started
            DialogCreateWorkout dialog = new DialogCreateWorkout(this, uniqueName -> {
                ContentValues cv = new ContentValues();
                cv.put(SQLhelper.COLUMN_DAY,    uniqueName[0]);
                cv.put(SQLhelper.COLUMN_NAME,   uniqueName[1]);
                cv.put(SQLhelper.COLUMN_INFO,   uniqueName[2]);
                sqlHelper.getWritableDatabase().insert(SQLhelper.TABLE_ONEGYM,null,cv);
                cv.clear();
                recycleViewInit();
            });
            //show dialogue
            dialog.show();
        }
        return workoutList;
    }






















    /********** Options Menu **************/
    //Creating a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflating the menu resource using MenuInflater
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
                DialogCreateWorkout dialog = new DialogCreateWorkout(this, uniqueName -> {
                    String selection = SQLhelper.COLUMN_DAY + "=? AND " + SQLhelper.COLUMN_NAME + "=? AND " + SQLhelper.COLUMN_INFO + "=?";
                    String[] selectionArgs = {uniqueName[0] , uniqueName[1] , uniqueName[2]};

                    Cursor cursor = sqlHelper.getWritableDatabase().query(SQLhelper.TABLE_ONEGYM,null,selection, selectionArgs,null,null,null);

                    if (  cursor.moveToFirst()  ) {
                        Toast.makeText(MainActivity.this, "Рядок уже є", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues cv = new ContentValues();
                        cv.put(SQLhelper.COLUMN_DAY,    uniqueName[0]);
                        cv.put(SQLhelper.COLUMN_NAME,   uniqueName[1]);
                        cv.put(SQLhelper.COLUMN_INFO,   uniqueName[2]);
                        sqlHelper.getWritableDatabase().insert(SQLhelper.TABLE_ONEGYM,null,cv);
                        cv.clear();
                    }
                    cursor.close();
                    //оновити список
                    recycleViewInit();
                });
                dialog.show();
                return true;

            case R.id.action_settings: openSettingsLayout();
                return true;

            case R.id.exercises:
                //launch the activity with all Exercises
                Intent intent = new Intent(MainActivity.this, ExercisesActivity.class);
                intent.putExtra("goal_launch", "show_exe");
                this.activityExercisesLauncher.launch(intent);
                return true;

            case R.id.action_log_sql:
                //show table SQL in LOG
                sqlHelper.getTableGymsInLog("Table_in_LOG");
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
            case R.id.delete: deleteOneItemInDatabase();  //delete training
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
            sqlHelper.updateRowTrainings(positioContextMenu, dayNameInfo);

            //update info in database 2
            sqlHelper.updateRowSets(getNameWorkout(positioContextMenu) ,dayNameInfo[0] + dayNameInfo[1] + dayNameInfo[2]);

            //update RecyclerView
            updateListInRecyclerView();
        };
        //get OneFitnessTraining from database
        String[] row = sqlHelper.getRowTrainings(positioContextMenu);
        //create dayNameInfo
        String[] dayNameInfo;
        if(row.length == 4)
            dayNameInfo = Arrays.copyOfRange(row,1,4);
        else {
            dayNameInfo = new String[]{"error", "error", "error"};
            Toast.makeText(this, "error: " + "if(row.length == 4)", Toast.LENGTH_SHORT).show();
        }

        //Start Dialog
        DialogCreateWorkout dialog = new DialogCreateWorkout(this, dialogUniqueNameProcessor, dayNameInfo);
        // show Dialog
        dialog.show();
    }







    /******************** DATABASES **************************/
    //delete item from database and from recycler view
    private void deleteOneItemInDatabase(){

        //RUN delete for dialog
        DialogOnClick dialogOnClick = () -> {
            //delete from the database of Fitness Trainings
            sqlHelper.deleteRowTrainings(getArrayNameWorkout(positioContextMenu));

            Log.d("howDel" , getNameWorkout(positioContextMenu));
            //delete from the database of performed sets
            sqlHelper.deleteRowSets(getNameWorkout(positioContextMenu));

            updateListInRecyclerView();
        };

        //show dialog DELETE OR CANCEL
        new DeleteDialog(MainActivity.this, dialogOnClick).show();

    }








    /********************** SETTINGS OF THE PROGRAM ************************/
    private void openSettingsLayout(){
        Intent intent = new Intent(MainActivity.this,  SettingsActivity.class);
        startActivity(intent);
    }









    /********************** ACTIVITY EXERCISESET CONTRACT ************************/
    private static class ExerciseSetContract extends ActivityResultContract<String[], Boolean> {

        //create Intent for sending to a new Activity
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String[] txts) {
            Intent intent = new Intent(context, ExerciseSetActivity.class);
            intent.putExtra("start fitness", txts);
            return intent;
        }

        //parse result`s Intent from Activity
        @Override
        public Boolean parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == Activity.RESULT_OK && intent != null) {
                return intent.getBooleanExtra("result fitness", false);
            }
            return false;
        }
    }



}