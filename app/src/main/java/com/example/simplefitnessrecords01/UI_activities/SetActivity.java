package com.example.simplefitnessrecords01.UI_activities;

import static com.example.simplefitnessrecords01.sql.SQLtrainings.COLUMN_INFO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.simplefitnessrecords01.fitness.Exercise;
import com.example.simplefitnessrecords01.fitness.ExecutedExercise;
import com.example.simplefitnessrecords01.fitness.Repeats;
import com.example.simplefitnessrecords01.fitness.OneSet;
import com.example.simplefitnessrecords01.fitness.Weight;
import com.example.simplefitnessrecords01.recycler_views.RecyclerViewOneSetsAdapter;
import com.example.simplefitnessrecords01.sql.SQLSetFits;
import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.ActivitySetBinding;
import com.example.simplefitnessrecords01.sql.SQLtrainings;

import java.util.ArrayList;
import java.util.List;

public class SetActivity extends AppCompatActivity {


    //binding
    private ActivitySetBinding binding;

    //link to the Database Assistant
    private SQLSetFits sqlSetFits;

    //Database
    SQLiteDatabase db;

    //database table name
    private String nameFitness;

    //Adapter RecyclerView
    RecyclerViewOneSetsAdapter adapter;





    /************************ SET GET *******************************/
    //GET DATABASE
    public SQLiteDatabase getDB() {
        return db;
    }

    //GET NAME OF TRAINING
    public String getNameFitness() {
        return nameFitness;
    }





    /************************  Activity Lifecycle **********************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //process info from extra via intent
        processIntentExtra();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //initialization of database links
        sqlSetFits = new SQLSetFits(SetActivity.this);
        db = sqlSetFits.getWritableDatabase();

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
        Cursor c = db.query(sqlSetFits.DATABASE_TABLE, null, selection, selectionArgs, null, null, null);

        //iterate through the cursor lines
        if(c.moveToNext()){
            int id_id = c.getColumnIndex(sqlSetFits.COLUMN_ID);
            int id_fitName = c.getColumnIndex(COLUMN_INFO);
            int id_exe = c.getColumnIndex(sqlSetFits.COLUMN_EXE);
            int id_wei = c.getColumnIndex(sqlSetFits.COLUMN_WEIGHT);
            int id_rep = c.getColumnIndex(sqlSetFits.COLUMN_REPEATS);
            do {
                int id         = c.getInt(id_id);
                String fitName = c.getString(id_fitName);
                String exe     = c.getString(id_exe);
                int wei        = c.getInt(id_wei);
                int rep        = c.getInt(id_rep);

                //add to list
                setsFitness.add(   new OneSet( id, new Exercise(exe) , new ExecutedExercise(new Weight(wei) , new Repeats(rep)) , fitName  )   );

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
        getMenuInflater().inflate(R.menu.menu_training, menu);

        return true;
    }

    //Prepare Menu
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.add);
        item.setVisible(true);
        return super.onPrepareOptionsMenu(menu);
    }

    //listener menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle clicks on menu items
        int id = item.getItemId();
        switch (id) {
            case R.id.add:
                //add Set
                OneSet oneSet = new OneSet(nameFitness);
                //add this set to sql
                sqlSetFits.addRow(oneSet);
                //
                updateRecycler();
                return true;
            case R.id.theend:
                // End workout click processing
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result fitness", true);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                return true;

            case R.id.settngs:
                openSettingsLayout();
                return true;

            case R.id.action_log_sql2:
                //table SQL to log
                sqlSetFits.getTableInLog("MainActSQLog", nameFitness);
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
                String query = "DELETE FROM " + SQLSetFits.DATABASE_TABLE + " WHERE " + SQLSetFits.COLUMN_ID + " = " + oneSet.getId();
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
        String where_clause = SQLtrainings.COLUMN_INFO + "=?";
        String[] where_args = new String[] { nameFitness };

        // get cursor with selection
        Cursor cursor = db.query(sqlSetFits.DATABASE_TABLE, null,where_clause,where_args,null,null,null);
        cursor.moveToFirst();

        //number of elements in database and recycler must be equal
        if(cursor.getCount() == setsFitness.size()) {
            //iteration for OneSet list
            for (OneSet oneSet : setsFitness) {
                //get data from setFitness
                String exe  = oneSet.getExe().toString();
                int weight  = oneSet.getRecordSet().getWeight().getIntWeight();
                int repeats = oneSet.getRecordSet().getRepeats().getIntRepeats();
                int id      = oneSet.getId();

                //put data in content
                cv.put(sqlSetFits.COLUMN_EXE, exe);
                cv.put(sqlSetFits.COLUMN_WEIGHT, weight);
                cv.put(sqlSetFits.COLUMN_REPEATS, repeats);

                //get id from database
                @SuppressLint("Range") int id1 = cursor.getInt(cursor.getColumnIndex(sqlSetFits.COLUMN_ID));

                //if id equals - update database
                if(id == id1)
                    db.update(sqlSetFits.DATABASE_TABLE, cv, sqlSetFits.COLUMN_ID + " = ?" , new String[] {String.valueOf(id)} );

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