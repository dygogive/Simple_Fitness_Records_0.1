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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivityMusclesGroupsBinding;

import java.util.Arrays;
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
    private ActivityResultLauncher<Intent> activityResultLauncherExercises, activityResultNewExercise;

    //extra to know what to do when onClick on item of ExpandableListView
    String intentExtra;










    /******************************* LIFECYCLE ****************************************/
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

        extraProcess();

        //launcher for activity to chose group of muscles
        activityResultLauncherExercises = registerForActivityResult(
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
        activityResultNewExercise = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if(result.getResultCode() == RESULT_OK) {
                        Toast.makeText(this, "RESULT_OK", Toast.LENGTH_SHORT).show();

                        //get extra
                        String[] extra = result.getData().getStringArrayExtra("newExerciseExtra");

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







    /***************************** GET INTENT EXTRA *****************************************/

    private void extraProcess(){
        //Intent with info
        Intent intent = getIntent();

        //Info from the intent
        intentExtra = intent.getStringExtra("whatToDo");

        Toast.makeText(this, "intentExtra-" +  intentExtra, Toast.LENGTH_SHORT).show();
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

        if(intentExtra.equals("StartNewExercise")) {
            MyExpandableListAdapter myExpandableListAdapter = new MyExpandableListAdapter(
                    MusclesGroupsActivity.this, Arrays.asList(groups),
            );







            expandableListView.setAdapter(simpleExpandableListAdapter);
        }

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


    private Map<String, List<String>> getMapItems(String[] groups, List<String[]> childs) {
        Map<String, List<String>> mapItems = new HashMap<>();




    }







    /****************************** LISTENERS ***********************************/
    private void listenersExpandableListView(ExpandableListView expandableListView) {
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {

            //get chosen names
            String groupName = getGroupText(expandableListView , groupPosition);
            String childName = getChildText(expandableListView , groupPosition, childPosition);

            //Toast.makeText(this, groupName + " -> " + childName , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MusclesGroupsActivity.this, ExercisesActivity.class);
            intent.putExtra("muscleGroup" , new String[]{groupName,childName});
            activityResultLauncherExercises.launch(intent);


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














    public static class MyExpandableListAdapter extends BaseExpandableListAdapter {

        private Context context;
        private List<String> listDataHeader;
        private HashMap<String, List<String>> listDataChild;

        public MyExpandableListAdapter(Context context, List<String> listDataHeader,
                                       HashMap<String, List<String>> listChildData) {
            this.context = context;
            this.listDataHeader = listDataHeader;
            this.listDataChild = listChildData;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            // Код для отримання вигляду заголовку
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            String childText = (String) getChild(groupPosition, childPosition);
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item, null);
            }

            TextView txtListChild = (TextView) convertView.findViewById(R.id.textView);
            txtListChild.setText(childText);

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            checkBox.setChecked(false);
            checkBox.setVisibility(View.VISIBLE);

            return convertView;
        }

        @Override
        public int getGroupCount() {
            return 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        // Код для інших методів
    }

}