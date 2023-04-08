package com.example.simplefitnessrecords01.UI_activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivityExerciseListBinding;

public class ExercisesList extends AppCompatActivity {


    ActivityExerciseListBinding binding;

    String[] extraArrayGroupMuscle = null;


    //Names of muscle groups
    String textGroup;
    String textMuscle;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExerciseListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Exercices");
        actionBar.setSubtitle("Select/Create exercise");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get info from intent
        processIntentExtra();

        initializingListView();
    }



    //Process the data obtained from the previous activity
    private void processIntentExtra() {
        //Intent with info
        Intent intent = getIntent();
        //Info from the intent
        extraArrayGroupMuscle = intent.getStringArrayExtra("muscleGroup");
        //text strings from the info from the intent
        textGroup =     extraArrayGroupMuscle[0];
        textMuscle =    extraArrayGroupMuscle[1];


        //display texts on the screen
        binding.tvGroup.setText(textGroup);
        binding.tvChild.setText(textMuscle);
    }


    private void initializingListView() {



    }






    /************************ OPTIONS MENU *****************************/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra("exercisesExtra", new String[] { extraArrayGroupMuscle[0], extraArrayGroupMuscle[1], "exercise888" });
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}