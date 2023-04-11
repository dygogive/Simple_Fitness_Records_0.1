package com.example.simplefitnessrecords01.UI_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivityExerciseListBinding;
import com.example.simplefitnessrecords01.fitness.MuscleGroup;
import com.example.simplefitnessrecords01.fitness.Exercise;
import com.example.simplefitnessrecords01.fitness.Muscles;
import com.example.simplefitnessrecords01.recycler_adapters.AdapterRecyclerExercises;
import com.example.simplefitnessrecords01.sql.SQLhelper;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ExercisesActivity extends AppCompatActivity {

    private ActivityExerciseListBinding binding;

    SQLhelper sqLhelper;
    SQLiteDatabase database;

    private AdapterRecyclerExercises adapterRecyclerExercises;

    String[] extraArrayGroupMuscle = null;

    //Names of muscle groups
    String textGroup;
    String textMuscle;






    /*************************** LIFECYCLE ACTIVITY ***************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //SQL database
        sqLhelper = new SQLhelper(this);
        database  = sqLhelper.getWritableDatabase();

        //set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Exercices");
        actionBar.setSubtitle("Select/Create exercise");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get info from intent
        processIntentExtra();

    }

    @Override
    protected void onResume() {
        super.onResume();

        recyclerInit();
    }

    //Process the data obtained from the previous activity
    private void processIntentExtra() {
        //Intent with info
        Intent intent = getIntent();
        Log.d("whereIs" , "test - 1");
        //Info from the intent
        extraArrayGroupMuscle = intent.getStringArrayExtra("muscleGroup");
        Log.d("whereIs" , "test - 2");
        //text strings from the info from the intent
        if(extraArrayGroupMuscle != null) {
            textGroup  = extraArrayGroupMuscle[0];
            textMuscle = extraArrayGroupMuscle[1];
        }
        Log.d("whereIs" , "test - 3");


        //display texts on the screen
        binding.tvGroup.setText(textGroup);
        binding.tvChild.setText(textMuscle);

        Log.d("whereIs" , "test - 4");
    }





    /****************** RecyclerView initializing ****************************/
    private void recyclerInit() {
        adapterRecyclerExercises = new AdapterRecyclerExercises(this, getExercises());
        binding.rvExercises.setLayoutManager(new LinearLayoutManager(this));
        binding.rvExercises.setAdapter(adapterRecyclerExercises);
    }

    private List<Exercise> getExercises() {
        List<Exercise> exerciseList = new LinkedList<>();
        for(int i = 0; i < 10; i++) {

            String[] groups = this.getResources().getStringArray(R.array.muscles_groups);
            String[] muscles = this.getResources().getStringArray(R.array.lower_body);
            Muscles muscles1 = new Muscles(Arrays.copyOfRange(muscles, 0, 3));

            MuscleGroup muscleGroup = new MuscleGroup(groups[0],muscles1);
            Exercise exe = new Exercise("Exe " + i , new MuscleGroup[]{muscleGroup} );
            exerciseList.add(exe);
        }

        for(Exercise exe : exerciseList) {
            ContentValues cv = new ContentValues();
            cv.put(SQLhelper.COLUMN_NAME_EXE, exe.toString());
            cv.put(SQLhelper.COLUMN_MUSCLE1, exe.getMuscleGroup[0]);
            cv.put(SQLhelper.COLUMN_MUSCLE2, "Muscle " + 2);
            cv.put(SQLhelper.COLUMN_MUSCLE3, "Muscle " + 3);
            cv.put(SQLhelper.COLUMN_MUSCLE4, "Muscle " + 4);
            database.insert(SQLhelper.TABLE_EXERCISES,null,cv);
        }





        return  exerciseList;
    }





    /************************ OPTIONS MENU *****************************/

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Intent returnIntent = new Intent();
                if(extraArrayGroupMuscle != null) {
                    returnIntent.putExtra("exercisesExtra", new String[]{extraArrayGroupMuscle[0], extraArrayGroupMuscle[1], "exercise888"});
                    setResult(Activity.RESULT_OK, returnIntent);
                } else setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                return true;

            case R.id.action_new:

                return true;

            case R.id.action_settings:

                return true;

            case R.id.action_log_sql:
                //show table SQL in LOG
                sqLhelper.getTableInLogTrainings("Table_in_LOG");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}