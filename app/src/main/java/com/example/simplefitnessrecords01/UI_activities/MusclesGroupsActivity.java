package com.example.simplefitnessrecords01.UI_activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivityMusclesGroupsBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusclesGroupsActivity extends AppCompatActivity {

    private ActivityMusclesGroupsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusclesGroupsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //set action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Muscles groups");
        actionBar.setSubtitle("Select group");
        actionBar.setDisplayHomeAsUpEnabled(true);

        //initExpandableListView();

        initListView();
    }


    private void initListView() {
        //Create ArrayAdapter for ListView and add it to ListView
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this,R.array.muscles_groups,android.R.layout.simple_list_item_1);
        binding.lvGroups.setAdapter(arrayAdapter);
        binding.lvGroups.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        //Array from resource
        String[] groups = getResources().getStringArray(R.array.muscles_groups);

        //ListView listener
        binding.lvGroups.setOnItemClickListener((parent, view, position, id) -> {
            Toast.makeText(this, "position - " + position + "String - " + groups[position] , Toast.LENGTH_SHORT).show();
        });
    }

    private void initExpandableListView() {
        //Array from resource
        String[] groups = getResources().getStringArray(R.array.muscles_groups);
//        String[] groups = getResources().getStringArray(R.array.muscles_groups);
//        String[] groups = getResources().getStringArray(R.array.muscles_groups);
//        String[] groups = getResources().getStringArray(R.array.muscles_groups);
//        String[] groups = getResources().getStringArray(R.array.muscles_groups);
//        String[] groups = getResources().getStringArray(R.array.muscles_groups);





    }


    private Map<String,String> getListWithKey(List<String> list, String key) {
        Map<String,String> map = new HashMap<>();

        for(String s : list) {
            map.put(key, s);
        }

        return map;
    }



}