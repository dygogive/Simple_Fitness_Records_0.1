package com.example.simplefitnessrecords01.UI_activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivityMusclesGroupsBinding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MusclesGroupsActivity extends AppCompatActivity {

    private static final String GROUP_KEY = "keyGroups";
    private static final String CHILD_KEY = "keyChilds";

    SimpleExpandableListAdapter simpleExpandableListAdapter = null;
    private ActivityMusclesGroupsBinding binding;

    //Triggers other activities from this activity
    private ActivityResultLauncher<Intent> activityResultLauncher;


    //Extra from Exercises Activity
    String[] extraFromExercisesActiv = new String[] {"text1", "text2"};










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


        //launcher for activity to chose group of muscles
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK) {
                        Toast.makeText(this, "RESULT_OK", Toast.LENGTH_SHORT).show();

                        //get extra
                        String[] extra = result.getData().getStringArrayExtra("exercisesExtra");

                        //put extra
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("muscleGroupsExtra", extra);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                });


        //check if there is ExpandableListview in the activity, check if there is Listview in the activity
        if( binding.lvGroups instanceof ExpandableListView) {
            initExpandableListView();
        } else if ( binding.lvGroups instanceof ListView) {
            initListView();
        }
    }








    /********************************** LIST VIEWS **********************************/
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


        //List og groups from resource
        //Array from resource
        String[] groups = getResources().getStringArray(R.array.muscles_groups);
        //List og groups with key
        List<Map<String,String>> groupsKey = createGroupData(groups, "keyGroups");

        //List of childs without key
        List<String[]> childs = new LinkedList<>();
        childs.add(getResources().getStringArray(R.array.lower_body));
        childs.add(getResources().getStringArray(R.array.back));
        childs.add(getResources().getStringArray(R.array.chest));
        childs.add(getResources().getStringArray(R.array.shoulders));
        childs.add(getResources().getStringArray(R.array.arms));
        childs.add(getResources().getStringArray(R.array.abdominals));
        //list with key
        List<List<Map<String, String>>> childsKey = createChildData(childs , CHILD_KEY);

        //
        String[] groupFrom = new String[] {GROUP_KEY};
        int[] groupTo      = new int[] {R.id.tv_groups};
        //
        String[] childFrom = new String[] {CHILD_KEY};
        int[] childTo      = new int[] {R.id.tv_muscles};

        ExpandableListView expandableListView = (ExpandableListView) binding.lvGroups;
        simpleExpandableListAdapter = new SimpleExpandableListAdapter(
                (Context) MusclesGroupsActivity.this,
                groupsKey, R.layout.text_list_groups_body, groupFrom, groupTo,
                childsKey, R.layout.text_list_groups_muscles , childFrom, childTo
        );
        expandableListView.setAdapter(simpleExpandableListAdapter);

        listenersExpandableListView(expandableListView);

    }


    private List<List<Map<String, String>>> createChildData(List<String[]> childs, String key) {
        //list for return
        List<List<Map<String, String>>> listChilds = new LinkedList<>();
        //List of items
        List<Map<String,String>> listOneChild;
        //item
        Map<String, String> item;

        //fill this list by data from childs
        for(String[] strings : childs) {
            //create List of items
            listOneChild = new LinkedList<>();

            //fill items
            for(String s : strings) {
                item = new HashMap<>();
                item.put(key,s);
                listOneChild.add(item);
            }

            //add list items to base list
            listChilds.add(listOneChild);
        }


        return   listChilds;
    }


    //Method for getting map of Strings with key
    private List<Map<String,String>> createGroupData(String[] array, String key) {
        List<Map<String,String>> listGroups = new LinkedList<>();
        //new map
        Map<String,String> map;
        //fill in the data
        for(String s : array) {
            map = new HashMap<>();
            map.put(key, s);
            listGroups.add(map);
        }
        //return map with data
        return listGroups;
    }









    /****************************** LISTENERS ***********************************/
    private void listenersExpandableListView(ExpandableListView expandableListView) {
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            //get chosen names
            String groupName = getGroupText(expandableListView , groupPosition);
            String childName = getChildText(expandableListView , groupPosition, childPosition);

            //Toast.makeText(this, groupName + " -> " + childName , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MusclesGroupsActivity.this, ExercisesList.class);
            intent.putExtra("muscleGroup" , new String[]{groupName,childName});
            activityResultLauncher.launch(intent);


            return true;
        });
    }

    private String getGroupText( ExpandableListView expandableListView , int groupIndex) {
        Map<String,String> map = (Map<String, String>) simpleExpandableListAdapter.getGroup(groupIndex);
        return map.get(GROUP_KEY);
    }
    private String getChildText(ExpandableListView expandableListView ,int groupIndex, int childIndex) {
        return ((Map<String,String>) simpleExpandableListAdapter.getChild(groupIndex,childIndex)).get(CHILD_KEY);
    }




    /************************ OPTIONS MENU *****************************/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}