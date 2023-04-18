package com.example.simplefitnessrecords01.UI_activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;

import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivityMusclesGroupsBinding;
import com.example.simplefitnessrecords01.dialog.StartDialog;
import com.example.simplefitnessrecords01.fitness.EmptySetTraining;
import com.example.simplefitnessrecords01.sql.SQLhelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
//            if(intentExtra.equals("StartNewExercise")) initExpandableListView2();
//            else initExpandableListView();
            initExpandableListView2();
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

        expandableListView.setAdapter(simpleExpandableListAdapter);

        listenersExpandableListView(expandableListView);
    }

    private void initExpandableListView2() {


        //List og groups from resource
        //Array from resource
        String[] groups = getResources().getStringArray(R.array.muscles_groups);

        //List of childs without key
        String[] childs;
        //list of childs with keys(groups names)
        Map<String, List<String>> mapChilds = new HashMap<>();

        for(String group : groups){
            childs = getResources().getStringArray(R.array.lower_body);
//            int identifier = getResources().getIdentifier(group, "array", getPackageName());
//            childs = getResources().getStringArray(identifier);
            mapChilds.put(group , List.of(childs));
        }


        ExpandableListView expandableListView = binding.lvGroups;

        MyExpandableListAdapter myAdapter = new MyExpandableListAdapter( this,
                Arrays.asList(groups), mapChilds
        );


        expandableListView.setAdapter(myAdapter);

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

            Toast.makeText(this, groupName + " -> " + childName , Toast.LENGTH_SHORT).show();
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

    //Creating a menu
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

        // Handle clicks on menu items
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                return true;
            case R.id.action_new:
                // Handling the click on the "NEW" item
                Toast.makeText(this, "OK", Toast.LENGTH_SHORT).show();
                return true;

        }


        return super.onOptionsItemSelected(item);
    }













    public class MyExpandableListAdapter extends BaseExpandableListAdapter {

        private Context                           context;
        //
        private List<String>                       groups;
        private Map<String, List<String>>           items;
        //
        private Map<String, Boolean>      groupSelections;
        private Map<String, List<Boolean>> itemSelections;




        public MyExpandableListAdapter(Context context, List<String> groups, Map<String, List<String>> items) {
            this.context =                 context;
            this.groups =                   groups;
            this.items =                     items;
            this.groupSelections = new HashMap<>();
            this.itemSelections =  new HashMap<>();

            //set false to all group and items
            for (String group : groups) {
                groupSelections.put(group, false);
                itemSelections .put(group, new ArrayList<>(Collections.nCopies(items.get(group).size(), false)));
            }
        }



        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return items.get(groups.get(groupPosition)).size();
        }

        @Override
        public String getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public String getChild(int groupPosition, int childPosition) {
            return items.get(groups.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return  false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.ex_lv_group, parent, false);
            }

            //отримати textView
            TextView tvGroup = convertView.findViewById(R.id.tvGroup);


            //отримати текст групи з даних
            String groupName = (String) getGroup(groupPosition);

            //Задати текст групи
            tvGroup.setText(groupName);

            return  convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if( convertView == null ) {
                convertView = LayoutInflater.from(context).inflate(R.layout.ex_lv_item, parent , false);
            }

            //отримати текст з списку даних
            String item = getChild(groupPosition, childPosition);
            //дати текст чекбоксу
            CheckBox checkBoxItem = (CheckBox) convertView.findViewById(R.id.chkItem);
            checkBoxItem.setText(item);



            //задати виділення з списку для чекбоксу
            boolean selection = itemSelections.get(getGroup(groupPosition)).get(childPosition);
            checkBoxItem.setChecked(selection);

            //слухач чекбоксу
            View finalConvertView = convertView;

            checkBoxItem.setOnClickListener(v -> {
                //чи виділений чекбокс
                boolean isChecked = ((CheckBox) v).isChecked();

                //обнулити інші групи і ітеми в них якщо виділений елемент цієї групи
                if(isChecked) {
                    //set false to groups and items
                    for (String group : groups) {
                        if(group.equals(getGroup(groupPosition))) continue;
                        groupSelections.put(group, false);
                        itemSelections .put(group, new ArrayList<>(Collections.nCopies(items.get(group).size(), false)));
                    }
                }

                //Виділити ітем, з яким працюємо зараз
                List<Boolean> booleanListItems = itemSelections.get(getGroup(groupPosition));
                booleanListItems.set(childPosition,isChecked);
                itemSelections.put(getGroup(groupPosition), booleanListItems);


                //Виділити групу, з якою працюємо зараз
                groupSelections.put( getGroup(groupPosition) , true );



                //повідомити адаптер, що дані змінилися
                notifyDataSetChanged();

            });



            return  convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }


}