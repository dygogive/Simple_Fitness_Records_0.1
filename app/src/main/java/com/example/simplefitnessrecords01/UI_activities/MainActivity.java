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

import com.example.simplefitnessrecords01.dialogs.DeleteDialog;
import com.example.simplefitnessrecords01.dialogs.DialogOnClick;
import com.example.simplefitnessrecords01.dialogs.DialogUniqueNameProcessor;
import com.example.simplefitnessrecords01.sql.SQLhelper;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.fitness.OneGym;
import com.example.simplefitnessrecords01.databinding.ActivityMainBinding;
import com.example.simplefitnessrecords01.dialogs.StartDialog;
import com.example.simplefitnessrecords01.recycler_adapters.AdapterRecyclerFitnessTrainings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Біндінг
    private ActivityMainBinding
            binding;
    //Різалт лаунчери
    private ActivityResultLauncher
            activityResultLauncher ,
            activityExercisesLauncher;
    //Хелпер БД
    SQLhelper
            sqLhelper;





    /************** GETTERS SETTERS **********************/

    //Отримати адаптер на РециклерВ'ю
    private AdapterRecyclerFitnessTrainings getAdapterRecyclerView(){
        return (AdapterRecyclerFitnessTrainings) binding.recyclerViewID.getAdapter();
    }

    //Створити унікальне ім'я тренування (
    private String createGymName(int position) {
        //get OneGym from position
        OneGym oneGym = getAdapterRecyclerView().getItem(position);
        //generosity unique name
        return oneGym.getDay() + oneGym.getName() + oneGym.getInfo();
    }


    private String[] getUniqueNameArray(int position) {

        //unique Name To Delete
        OneGym oneGym = getAdapterRecyclerView().getItem(position);

        String[] uniqueName = new String[3];

        uniqueName[0] = oneGym.getDay();
        uniqueName[1] = oneGym.getName();
        uniqueName[2] = oneGym.getInfo();

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
        sqLhelper = new SQLhelper(MainActivity.this);

        //luncher for SetActivity
        activityResultLauncher = registerForActivityResult(
                new MyActivityResultContract(),
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
        sqLhelper.close();
    }






    /**********  RecyclerView ***************/

    //initialization recycleView list of created fitness workouts
    private void recycleViewInit() {
        //get setTrainingList from database
        List<OneGym> oneGymList = getOneGymListFromDB();

        //create a new adapter with this list
        AdapterRecyclerFitnessTrainings adapter =
                new AdapterRecyclerFitnessTrainings(oneGymList, this);

        //the display manager of the elements is transferred
        binding.recyclerViewID.setLayoutManager(new LinearLayoutManager(this));

        //pass adapter to Recyclerview
        binding.recyclerViewID.setAdapter(adapter);

        //set up our listener for short-click processing and code the processing itself
        adapter.setOnItemRecyclerClickListener((position) -> {
            activityResultLauncher.launch(getUniqueNameArray(position));
        });
    }

    //method creates a list of database names
    private List<OneGym> getOneGymListFromDB(){

        //empty list for SetTraining from base for recycler
        List<OneGym> oneGymList = new ArrayList<>();

        //cursor from base with selection of all
        Cursor cursor = sqLhelper.getWritableDatabase().rawQuery("SELECT * FROM " + SQLhelper.TABLE_ONEGYM, null);

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
                String info = cursor.getString(cursor.getColumnIndex(SQLhelper.COLUMN_INFO));

                //add a row from the database to the list
                oneGymList.add(   new OneGym(id, day, name, info)   );
            } while (cursor.moveToNext());
        } else {
            //If the base is empty, then the dialog is started
            StartDialog dialog = new StartDialog(this, uniqueName -> {
                ContentValues cv = new ContentValues();
                cv.put(SQLhelper.COLUMN_DAY,    uniqueName[0]);
                cv.put(SQLhelper.COLUMN_NAME,   uniqueName[1]);
                cv.put(SQLhelper.COLUMN_INFO,   uniqueName[2]);
                sqLhelper.getWritableDatabase().insert(SQLhelper.TABLE_ONEGYM,null,cv);
                cv.clear();
                recycleViewInit();
                });
            //show dialogue
            dialog.show();
        }
        //повернути список об'єктів тренувань
        return oneGymList;
    }

    //update recycler view
    private void updateRecyclerView(){
        //from db to adapter update data
        ((AdapterRecyclerFitnessTrainings)binding.recyclerViewID.getAdapter()).setFitnessList(getOneGymListFromDB());
        //notify adapter
        binding.recyclerViewID.getAdapter().notifyDataSetChanged();
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
                StartDialog dialog = new StartDialog(this, uniqueName -> {
                    String selection = SQLhelper.COLUMN_DAY + "=? AND " + SQLhelper.COLUMN_NAME + "=? AND " + SQLhelper.COLUMN_INFO + "=?";
                    String[] selectionArgs = {uniqueName[0] , uniqueName[1] , uniqueName[2]};

                    Cursor cursor = sqLhelper.getWritableDatabase().query(SQLhelper.TABLE_ONEGYM,null,selection, selectionArgs,null,null,null);

                    if (  cursor.moveToFirst()  ) {
                        Toast.makeText(MainActivity.this, "Рядок уже є", Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues cv = new ContentValues();
                        cv.put(SQLhelper.COLUMN_DAY,    uniqueName[0]);
                        cv.put(SQLhelper.COLUMN_NAME,   uniqueName[1]);
                        cv.put(SQLhelper.COLUMN_INFO,   uniqueName[2]);
                        sqLhelper.getWritableDatabase().insert(SQLhelper.TABLE_ONEGYM,null,cv);
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
                sqLhelper.getTableGymsInLog("Table_in_LOG");
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
            sqLhelper.updateRowSets(createGymName(positioContextMenu) ,dayNameInfo[0] + dayNameInfo[1] + dayNameInfo[2]);

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

            Log.d("howDel" , createGymName(positioContextMenu));
            //delete from the database of performed sets
            sqLhelper.deleteRowSets(createGymName(positioContextMenu));

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









    /********************** ACTIVITY RESULT CONTRACT ************************/
    private static class MyActivityResultContract extends ActivityResultContract<String[], Boolean> {

        //create Intent for sending to a new Activity
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String[] txts) {
            Intent intent = new Intent(context, SetActivity.class);
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