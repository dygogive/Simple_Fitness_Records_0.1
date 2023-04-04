package com.example.simplefitnessrecords01.UI_activities;

import static com.example.simplefitnessrecords01.sql.SQLhelper.COLUMN_INFO;

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

import com.example.simplefitnessrecords01.activityResultContracts.MyActivityResultContract;
import com.example.simplefitnessrecords01.fitness.Exercise;
import com.example.simplefitnessrecords01.fitness.ExecutedExercise;
import com.example.simplefitnessrecords01.fitness.ExerciseGroup;
import com.example.simplefitnessrecords01.fitness.Repeats;
import com.example.simplefitnessrecords01.fitness.OneSet;
import com.example.simplefitnessrecords01.fitness.Weight;
import com.example.simplefitnessrecords01.recycler_views.RecyclerViewOneSetsAdapter;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivitySetBinding;
import com.example.simplefitnessrecords01.sql.SQLhelper;

import java.util.ArrayList;
import java.util.List;

public class SetActivity extends AppCompatActivity {


    //binding
    private ActivitySetBinding binding;

    //link to the Database Assistant
    private SQLhelper sqLhelper;

    //Database
    SQLiteDatabase db;

    //database table name
    private String nameFitness;

    //Adapter RecyclerView
    RecyclerViewOneSetsAdapter adapter;

    //Triggers other activities from this activity
    private ActivityResultLauncher activityResultLauncher;



    /************************ SET GET *******************************/
    //GET DATABASE
    public SQLiteDatabase getDB() {
        return db;
    }

    //GET NAME OF TRAINING
    public String getNameFitness() {
        return nameFitness;
    }

    public ActivityResultLauncher getActivityResultLauncher() {
        return activityResultLauncher;
    }

    /************************  Activity Lifecycle **********************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        //launcher for activity to chose group of muscles
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result -> {
            if(result.getResultCode() == Activity.RESULT_OK) {
                //
                //String[] stringArrayExtra = result.getData().getStringArrayExtra("key1");
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SetActivity.this);
        String selectedTextSize = preferences.getString("text_size_preference", getResources().getString(R.string.default_text_size));
        binding.tvSubName.setTextSize(Float.parseFloat(selectedTextSize));
        binding.tvName.setTextSize(Float.parseFloat(selectedTextSize));
        binding.tvDay.setTextSize(Float.parseFloat(selectedTextSize));

        //initialization of database links
        sqLhelper = new SQLhelper(SetActivity.this);
        db = sqLhelper.getWritableDatabase();

        //initialization of the Recycler view
        recycleViewInit();
    }

    @Override
    protected void onPause() {
        //save all to db
        updateTableDBFromList(adapter.getSetOneSetList());

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
        List<OneSet> setsFitness = getSetsFitness();
        //Adapter for recycler
        adapter = new RecyclerViewOneSetsAdapter(SetActivity.this, setsFitness);
        //manager for RECYCLER
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //set Adapter for recycler
        binding.recyclerView.setAdapter(adapter);
    }

    private List<OneSet> getSetsFitness() {
        //empty list for SetTraining from base for recycler
        List<OneSet> setsFitness = new ArrayList<>();

        String selection = COLUMN_INFO + " = ?";
        String[] selectionArgs = new String[] {getNameFitness()};
        Cursor c = db.query(sqLhelper.TABLE_SETS, null, selection, selectionArgs, null, null, null);

        //iterate through the cursor lines
        if(c.moveToNext()){
            int id_id = c.getColumnIndex(sqLhelper.COLUMN_ID);
            int id_fitName = c.getColumnIndex(COLUMN_INFO);
            int id_group = c.getColumnIndex(sqLhelper.COLUMN_GROUP);
            int id_exe = c.getColumnIndex(sqLhelper.COLUMN_EXE);
            int id_wei = c.getColumnIndex(sqLhelper.COLUMN_WEIGHT);
            int id_rep = c.getColumnIndex(sqLhelper.COLUMN_REPEATS);
            do {
                int id         = c.getInt(id_id);
                String fitName = c.getString(id_fitName);
                String group     = c.getString(id_group);
                String exe     = c.getString(id_exe);
                int wei        = c.getInt(id_wei);
                int rep        = c.getInt(id_rep);

                //add to list
                setsFitness.add(   new OneSet( id, new ExerciseGroup(group), new Exercise(exe) , new ExecutedExercise(new Weight(wei) , new Repeats(rep)) , fitName  )   );

            } while (c.moveToNext());
        } else {
            Toast.makeText(this, "Press the plus, write a new set.", Toast.LENGTH_SHORT).show();
        }
        //return a list of training objects
        return setsFitness;
    }

    //update Recycler View
    private void updateRecycler() {
        // get List<OneSet>
        List<OneSet> setsFitness = getSetsFitness();
        //put into adapter
        adapter.setSetOneSetList(setsFitness);
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
                OneSet oneSet = new OneSet(nameFitness);
                //add this set to sql
                sqLhelper.addRowSets(oneSet);
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

            case R.id.action_log_sql:
                //table SQL to log
                sqLhelper.getTableInLogSets("MainActSQLog", nameFitness);
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

    // position For ContextMenu
    private int positionForContextMenu = -1;

    // Implementation of a method for processing clicks on context menu items
    @Override
    public boolean onContextItemSelected(MenuItem item)       {
        // We process clicking on the context menu items
        switch (item.getItemId()) {

            case R.id.delete_set:
                //get OneSet for deleting
                OneSet oneSet = adapter.getOneSet(positionForContextMenu);
                //delete from DB
                String query = "DELETE FROM " + sqLhelper.TABLE_SETS + " WHERE " + sqLhelper.COLUMN_ID + " = " + oneSet.getId();
                db.execSQL(query);
                //update OneSetList in adapter
                adapter.setSetOneSetList(getSetsFitness());
                //renew display
                adapter.notifyDataSetChanged();

                return true;

            default:
                return super.onContextItemSelected(item);
    }
}

    public void setPosition(int position) {
        positionForContextMenu = position;
    }






    /********************  Save DATA TO DB **********************/
    public void updateTableDBFromList(List<OneSet> setsFitness1){
        //
        ContentValues cv = new ContentValues();

        // get OneSet list from recycler
        List<OneSet> setsFitness = setsFitness1;

        // create selection from database
        String where_clause = SQLhelper.COLUMN_INFO + "=?";
        String[] where_args = new String[] { nameFitness };

        // get cursor with selection
        Cursor cursor = db.query(sqLhelper.TABLE_SETS, null,where_clause,where_args,null,null,null);
        cursor.moveToFirst();

        //number of elements in database and recycler must be equal
        if(cursor.getCount() == setsFitness.size()) {
            //iteration for OneSet list
            for (OneSet oneSet : setsFitness) {
                //get data from setFitness
                String exe       = oneSet.getExe().toString();
                String exeGroup  = oneSet.getExerciseGroup().toString();
                int weight       = oneSet.getRecordSet().getWeight().getIntWeight();
                int repeats      = oneSet.getRecordSet().getRepeats().getIntRepeats();
                int id           = oneSet.getId();

                //put data in content
                cv.put(sqLhelper.COLUMN_GROUP, exeGroup);
                cv.put(sqLhelper.COLUMN_EXE, exe);
                cv.put(sqLhelper.COLUMN_WEIGHT, weight);
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
        Intent intent = new Intent(SetActivity.this,  SettingsActivity.class);
        startActivity(intent);
    }
}