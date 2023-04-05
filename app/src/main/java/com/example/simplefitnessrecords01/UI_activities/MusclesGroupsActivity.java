package com.example.simplefitnessrecords01.UI_activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivityMusclesGroupsBinding;

public class MusclesGroupsActivity extends AppCompatActivity {

    private ActivityMusclesGroupsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusclesGroupsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Create ArrayAdapter for ListView and add it to ListView
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.muscles_groups,android.R.layout.simple_list_item_1);
        binding.lvGroups.setAdapter(arrayAdapter);
        binding.lvGroups.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        //Array from resource
        String[] groups = getResources().getStringArray(R.array.muscles_groups);

        //ListView listener
        binding.lvGroups.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(this, "position - " + position , Toast.LENGTH_SHORT).show();
        });

        //

    }





}