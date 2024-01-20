package com.example.simplefitnessrecords01.UI_activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.simplefitnessrecords01.databinding.ActivityExerciseSetBinding;
import com.example.simplefitnessrecords01.fitness.EmptyExerciseSet;
import com.example.simplefitnessrecords01.fitness.ExerciseSet;
import com.example.simplefitnessrecords01.fitness.MuscleGroup;
import com.example.simplefitnessrecords01.fitness.ExecutedExercise;
import com.example.simplefitnessrecords01.fitness.Repeats;
import com.example.simplefitnessrecords01.fitness.Weight;
import com.example.simplefitnessrecords01.recycler_adapters.AdapterExerciseSets;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.sql.SQLhelper;

import java.util.ArrayList;
import java.util.List;

public class ExerciseSetActivity extends AppCompatActivity {


    //binding
    private ActivityExerciseSetBinding binding;

    //link to the Database Assistant
    private SQLhelper sqLhelper;

    //Database
    SQLiteDatabase db;

    //database table name
    private String nameFitness;

    //Adapter RecyclerView
    AdapterExerciseSets adapter;

    // current position of recycler
    private int positionOfRecycler = -1;

    //extra From Exercise with name og group and exercise
    String[] extraFromExercise = null;

    //Triggers other activities from this activity
    private ActivityResultLauncher<Intent> activityChoseExeLauncher, activityExercisesLauncher;





    /************************ SET GET *******************************/
    //GET DATABASE
    public SQLiteDatabase getDB() {
        return db;
    }

    //set Position
    public void setPosition(int position) {
        positionOfRecycler = position;
    }

    //GET NAME OF TRAINING
    public String getNameFitness() {
        return nameFitness;
    }

    public ActivityResultLauncher getActivityChoseExeLauncher() {
        return activityChoseExeLauncher;
    }
    public ActivityResultLauncher getactivityExercisesLauncher() {
        return activityExercisesLauncher;
    }






    /************************  Activity Lifecycle **********************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseSetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initialization of database links
        sqLhelper = new SQLhelper(ExerciseSetActivity.this);
        db = sqLhelper.getWritableDatabase();


        //launcher for activity to chose group of muscles
        activityChoseExeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK) {
                        //get data for new Exercise
                        extraFromExercise = result.getData().getStringArrayExtra("muscleGroupsExtra");
                        if(extraFromExercise != null) {
                            //change in database
                            sqLhelper.updateRowSets(positionOfRecycler, nameFitness, extraFromExercise);
                        }
                    }
                });


        activityExercisesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK) {
                        Toast.makeText(this, "return from Exercises Activity", Toast.LENGTH_SHORT).show();
                    }
                });




        //set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Sets");
        actionBar.setSubtitle("Put your records");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //process info from extra via intent
        processIntentExtra();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //set size of text
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ExerciseSetActivity.this);
        String selectedTextSize = preferences.getString("text_size_preference", getResources().getString(R.string.default_text_size));
        binding.tvSubName.setTextSize(Float.parseFloat(selectedTextSize));
        binding.tvName.setTextSize(Float.parseFloat(selectedTextSize));
        binding.tvDay.setTextSize(Float.parseFloat(selectedTextSize));



        //initialization of the Recycler view
        recycleViewInit();
    }

    @Override
    protected void onPause() {
        //save all to db
        updateTableDBFromList(adapter.getExerciseSetList());

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        //close database
        db.close();
        super.onDestroy();
    }






    //Process the data obtained from the previous activity
    private void processIntentExtra() {
        //Intent with info
        Intent intent = getIntent();
        //Info from the intent
        String[] getExtraArray = intent.getStringArrayExtra("start fitness");
        //text strings from the info from the intent
        String textDay =     getExtraArray[0];
        String textName =    getExtraArray[1];
        String textSubname = getExtraArray[2];


        //display texts on the screen
        binding.tvDay.setText(textDay);
        binding.tvName.setText(textName);
        binding.tvSubName.setText(textSubname);

        //a unique workout name
        nameFitness = textDay + textName + textSubname;
    }






    /******************  RecyclerView **********************/
    private void recycleViewInit() {
        List<ExerciseSet> setsFitness = getSetsFitness();
        //Adapter for recycler
        adapter = new AdapterExerciseSets(ExerciseSetActivity.this, setsFitness);
        //manager for RECYCLER
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //set Adapter for recycler
        binding.recyclerView.setAdapter(adapter);
    }

    private List<ExerciseSet> getSetsFitness() {
        //empty list for SetTraining from base for recycler
        List<ExerciseSet> setsTrainings = new ArrayList<>();

        String selection = SQLhelper.COLUMN_UNIC_NAME + " = ?";
        String[] selectionArgs = new String[] {getNameFitness()};
        Cursor c = db.query(sqLhelper.TABLE_SETS, null, selection, selectionArgs, null, null, null);

        //iterate through the cursor lines
        if(c.moveToNext()){
            int id_id = c.getColumnIndex(sqLhelper.COLUMN_ID);
            int id_uniName = c.getColumnIndex(SQLhelper.COLUMN_UNIC_NAME);
            int id_group = c.getColumnIndex(sqLhelper.COLUMN_GROUP);
            int id_exe = c.getColumnIndex(sqLhelper.COLUMN_EXE);
            int id_muscle1 = c.getColumnIndex(sqLhelper.COLUMN_MUSCLE1);
            int id_muscle2 = c.getColumnIndex(sqLhelper.COLUMN_MUSCLE2);
            int id_muscle3 = c.getColumnIndex(sqLhelper.COLUMN_MUSCLE3);
            int id_muscle4 = c.getColumnIndex(sqLhelper.COLUMN_MUSCLE4);
            int id_wei = c.getColumnIndex(sqLhelper.COLUMN_WEIGHT);
            int id_rep = c.getColumnIndex(sqLhelper.COLUMN_REPEATS);
            do {
                int id         = c.getInt(id_id);
                String uniName = c.getString(id_uniName);
                String group     = c.getString(id_group);
                String muscle1     = c.getString(id_muscle1);
                String muscle2     = c.getString(id_muscle2);
                String muscle3     = c.getString(id_muscle3);
                String muscle4     = c.getString(id_muscle4);
                String exe     = c.getString(id_exe);
                int wei        = c.getInt(id_wei);
                int rep        = c.getInt(id_rep);

                ExecutedExercise executedExercise = new ExecutedExercise(exe,new MuscleGroup(group , new String[]{muscle1,muscle2,muscle3,muscle4}),
                        new Weight(wei), new Repeats(rep));
                ExerciseSet setTraining = new ExerciseSet(id, executedExercise, uniName);

                //add to list
                setsTrainings.add( setTraining );

            } while (c.moveToNext());
        } else {
            Toast.makeText(this, "Press the plus, write a new set.", Toast.LENGTH_SHORT).show();
        }
        //return a list of training objects
        return setsTrainings;
    }

    //update Recycler View
    private void updateRecycler() {
        // get List<OneSet>
        List<ExerciseSet> sets = getSetsFitness();
        //put into adapter
        adapter.setExerciseSetList(sets);
        //renew display
        adapter.notifyDataSetChanged();
    }






    /******************* Options Menu **********************/

    // Creating a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Роздування ресурсу меню з використанням MenuInflater
        getMenuInflater().inflate(R.menu.menu_settings, menu);

        return true;
    }

    //Prepare Menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_new);
        item.setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    //listener menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle clicks on menu items
        int id = item.getItemId();
        switch (id) {
            case R.id.action_new:
                //add Set
                EmptyExerciseSet emptySetTraining = new EmptyExerciseSet(nameFitness);
                //add this set to sql
                sqLhelper.addRowSets(emptySetTraining);
                //
                updateRecycler();
                return true;
//            case R.id.theend:
//                // End workout click processing
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("result fitness", true);
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();
//                return true;

            case R.id.action_settings:
                openSettingsLayout();
                return true;

            case R.id.exercises:

                //launch the activity with all Exercises
                Intent intent = new Intent(ExerciseSetActivity.this, ExercisesActivity.class);
                intent.putExtra("goal_launch", "show_exe");
                ExerciseSetActivity.this.getactivityExercisesLauncher().launch(intent);

                return true;

            case R.id.action_log_sql:
                //table SQL to log
                sqLhelper.getTableInLogSets("Table_in_LOG", nameFitness);


                return true;

            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result fitness", true);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }





    /************************ Context Menu  *****************************/


    // Implementation of a method for processing clicks on context menu items
    @Override
    public boolean onContextItemSelected(MenuItem item)       {
        // We process clicking on the context menu items
        switch (item.getItemId()) {

            case R.id.delete_set:
                //get OneSet for deleting
                ExerciseSet setTraining = adapter.getExerciseSet(positionOfRecycler);
                //delete from DB
                String query = "DELETE FROM " + sqLhelper.TABLE_SETS + " WHERE " + sqLhelper.COLUMN_ID + " = " + setTraining.getId();
                db.execSQL(query);
                //update OneSetList in adapter
                adapter.setExerciseSetList(getSetsFitness());
                //renew display
                adapter.notifyDataSetChanged();

                return true;

            default:
                return super.onContextItemSelected(item);
    }
}





    /********************  Save DATA TO DB **********************/
    public void updateTableDBFromList(List<ExerciseSet> setsFitness1){
        //
        ContentValues cv = new ContentValues();

        // get OneSet list from recycler
        List<ExerciseSet> setsFitness = setsFitness1;

        // create selection from database
        String where_clause = SQLhelper.COLUMN_UNIC_NAME + "=?";
        String[] where_args = new String[] { nameFitness };

        // get cursor with selection
        Cursor cursor = db.query(sqLhelper.TABLE_SETS, null,where_clause,where_args,null,null,null);
        cursor.moveToFirst();

        //number of elements in database and recycler must be equal
        if(cursor.getCount() == setsFitness.size()) {
            //iteration for OneSet list
            for (ExerciseSet setTraining : setsFitness) {
                //get data from setFitness
                String exe       = setTraining.getExecutedExercise().toString();
                String exeGroup  = setTraining.getExecutedExercise().getMuscleGroup().getBodyPart();
                int weight       = setTraining.getExecutedExercise().getWeight().toInt();
                int repeats      = setTraining.getExecutedExercise().getRepeats().toInt();
                int id           = setTraining.getId();

                //put data in content
                cv.put(sqLhelper.COLUMN_GROUP,   exeGroup);
                cv.put(sqLhelper.COLUMN_EXE,     exe);
                cv.put(sqLhelper.COLUMN_WEIGHT,  weight);
                cv.put(sqLhelper.COLUMN_REPEATS, repeats);

                //get id from database
                @SuppressLint("Range") int id1 = cursor.getInt(cursor.getColumnIndex(sqLhelper.COLUMN_ID));

                //if id equals - update database
                if(id == id1)
                    db.update(sqLhelper.TABLE_SETS, cv, sqLhelper.COLUMN_ID + " = ?" , new String[] {String.valueOf(id)} );

                // next iteration
                cv.clear();
                cursor.moveToNext();
            }
        }
    }





    /********************** SETTINGS OF THE PROGRAM ************************/
    private void openSettingsLayout(){
        Intent intent = new Intent(ExerciseSetActivity.this,  SettingsActivity.class);
        startActivity(intent);
    }
}