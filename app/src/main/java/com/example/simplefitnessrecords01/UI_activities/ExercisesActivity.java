package com.example.simplefitnessrecords01.UI_activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivityExerciseListBinding;
import com.example.simplefitnessrecords01.dialog.DialogUniqueNameProcessor;
import com.example.simplefitnessrecords01.dialog.NewExeDialog;
import com.example.simplefitnessrecords01.fitness.MuscleGroup;
import com.example.simplefitnessrecords01.fitness.Exercise;
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
    String textGroup = "";
    String textMuscle = "";

    //Triggers other activities from this activity
    private ActivityResultLauncher<Intent> activityMusclesGroupLauncher;

    //chosen muscles for new exercise
    String[] musclesChosen;





    /*************************** LIFECYCLE ACTIVITY ***************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        activityMusclesGroupLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
            if(result.getResultCode() == RESULT_OK) {

                //get data for new Exercise
                musclesChosen = result.getData().getStringArrayExtra("musclesChosen");

                createNewExercise(musclesChosen);

                Toast.makeText(ExercisesActivity.this, musclesChosen [0] + " " + musclesChosen [1], Toast.LENGTH_SHORT).show();

            }
        });



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

        //Info from the intent
        extraArrayGroupMuscle = intent.getStringArrayExtra("muscleGroup");

        //text strings from the info from the intent
        if(extraArrayGroupMuscle != null) {
            textGroup  = extraArrayGroupMuscle[0];
            textMuscle = extraArrayGroupMuscle[1];
        }

        //display texts on the screen
        binding.tvGroup.setText(textGroup);
        binding.tvChild.setText(textMuscle);

        Log.d("whereIs" , "test - 0");
    }





    /****************** RecyclerView initializing ****************************/
    private void recyclerInit() {
        adapterRecyclerExercises = new AdapterRecyclerExercises(this, getExercises());
        binding.rvExercises.setLayoutManager(new LinearLayoutManager(this));
        binding.rvExercises.setAdapter(adapterRecyclerExercises);
    }
    Cursor c = null;
    private List<Exercise> getExercises() {
        List<Exercise> exerciseList = new LinkedList<>();

        if(!textGroup.equals("") & !textMuscle.equals("")) {
            String selection = "" + SQLhelper.COLUMN_GROUP + "=? AND (" + SQLhelper.COLUMN_MUSCLE1 +
                    "=? OR " + SQLhelper.COLUMN_MUSCLE2 + "=? OR " + SQLhelper.COLUMN_MUSCLE3 +
                    "=? OR " + SQLhelper.COLUMN_MUSCLE4 + "=?)";

            String[] selectionArgs = {textGroup, textGroup};
            c = database.query(SQLhelper.TABLE_EXERCISES, null, selection, selectionArgs, null, null, null);
        }else {
            c = database.query(SQLhelper.TABLE_EXERCISES, null, null, null, null, null, null);
        }

        Exercise    exercise;
        MuscleGroup muscleGroup;

        if(c.moveToNext()){
            int id_id       = c.getColumnIndex( SQLhelper.COLUMN_ID       );
            int id_exename  = c.getColumnIndex( SQLhelper.COLUMN_NAME_EXE );
            int id_group    = c.getColumnIndex( SQLhelper.COLUMN_GROUP    );
            int id_muscle1  = c.getColumnIndex( SQLhelper.COLUMN_MUSCLE1  );
            int id_muscle2  = c.getColumnIndex( SQLhelper.COLUMN_MUSCLE2  );
            int id_muscle3  = c.getColumnIndex( SQLhelper.COLUMN_MUSCLE3  );
            int id_muscle4  = c.getColumnIndex( SQLhelper.COLUMN_MUSCLE4  );
            do {
                int id           = c.getInt   ( id_id      );
                String exename   = c.getString( id_exename );
                String group     = c.getString( id_group   );
                String muscle1   = c.getString( id_muscle1 );
                String muscle2   = c.getString( id_muscle2 );
                String muscle3   = c.getString( id_muscle3 );
                String muscle4   = c.getString( id_muscle4 );

                muscleGroup = new MuscleGroup( group, new String[]{muscle1,muscle2,muscle3,muscle4});
                exercise = new Exercise(exename, muscleGroup);

                exerciseList.add(exercise);

            } while ( c.moveToNext() );
        }

        c.close();

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
                Intent intent = new Intent(this, MusclesGroupsActivity.class);
                intent.putExtra("goal_launch", "StartNewExercise");
                activityMusclesGroupLauncher.launch(intent);


                return true;

            case R.id.action_settings:

                return true;

            case R.id.action_log_sql:
                //show table SQL in LOG
                sqLhelper.getTableInLogExercises("Table_in_LOG");
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }








    /******************* CREATING NEW EXERCISE *************************/
    String nameNewExercise = "";
    private void createNewExercise(String[] musclesChosen) {
        DialogUniqueNameProcessor dialogUniqueNameProcessor = uniqueName -> {
            //
            String[] data = new String[musclesChosen.length + 1];
            data[0] = uniqueName[0];
            for(int i = 0; i < musclesChosen.length; i++){
                data[i + 1] = musclesChosen[i];
            }

            sqLhelper.createNewExeInSQL(data);

            adapterRecyclerExercises.updateList(getExercises());
            adapterRecyclerExercises.notifyDataSetChanged();
        };

        NewExeDialog dialog = new NewExeDialog(ExercisesActivity.this, dialogUniqueNameProcessor);
        dialog.show();
    }



}