package com.example.simplefitnessrecords01.recycler_views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.UI_activities.MusclesGroupsActivity;
import com.example.simplefitnessrecords01.UI_activities.SetActivity;
import com.example.simplefitnessrecords01.databinding.RecyclerSetActivityBinding;
import com.example.simplefitnessrecords01.fitness.OneSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RecyclerViewOneSetsAdapter extends RecyclerView.Adapter<RecyclerViewOneSetsAdapter.HolderSetFitList> {

    SetActivity context;


    //LIST of performed approaches in training
    private List<OneSet> setOneSetList = new ArrayList<>();



    /********  GETTERS-SETTERS ********************/
    public List<OneSet> getSetOneSetList() {
        return setOneSetList;
    }

    //Задати список підходів
    public void setSetOneSetList(List<OneSet> setOneSetList) {
        this.setOneSetList = setOneSetList;
    }

    //Add another set to the adapter list
    public void addOneSet(OneSet set) {
        setOneSetList.add(set);
    }
    public OneSet getOneSet(int position) {
        return setOneSetList.get(position);
    }

    //The method returns the number of objects
    @Override
    public int getItemCount() {
        return setOneSetList.size();
    }










    /************* Constructors ***********************/
    public RecyclerViewOneSetsAdapter(Context context) {
        this.context = (SetActivity) context;
    }

    public RecyclerViewOneSetsAdapter(Context context, List<OneSet> oneSetList) {
        this.context = (SetActivity) context;
        this.setOneSetList = oneSetList;
    }






    // **************   Creation of View and Holder blank   ********
    @NonNull
    @Override
    public HolderSetFitList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Створення В'ю
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_set_activity, parent, false);

        //Створення холдера
        return new HolderSetFitList(view);
    }




    /****** Initialize (Animate) the View and the holder with the data of the object from the list **********/
    @Override
    public void onBindViewHolder(@NonNull HolderSetFitList holder, int position) {
        //Витягти об'єкт зі списку
        OneSet oneSet = setOneSetList.get(position);
        //Через метод в холдері запихнути дані в В'ю
        holder.initItemView(oneSet);
    }







    /*******************  Holder  *****************/
    public class HolderSetFitList extends RecyclerView.ViewHolder {


        //text size
        String selectedTextSize = context.getString(R.string.default_text_size);


        //object in holder
        OneSet oneSet1 = null;

        //binder
        RecyclerSetActivityBinding binding = RecyclerSetActivityBinding.bind(itemView);

        //constructor
        public HolderSetFitList(@NonNull View itemView) {
            super(itemView);


            //set size of text
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            selectedTextSize = preferences.getString("text_size_preference", context.getString(R.string.default_text_size));


            //set the listener for Long clicks on the view element
            itemView.setOnLongClickListener(v -> {
                //position
                int position = getAbsoluteAdapterPosition();

                //context menu registry
                v.setOnCreateContextMenuListener((menu, v1, menuInfo) -> {
                    // We set the title of the context menu
                    //menu.setHeaderTitle("Опції");

                    // We fill the context menu with items
                    ((SetActivity)context).getMenuInflater().inflate(R.menu.context_set_activity, menu);
                });

                //If the context is SetActivity
                if (context instanceof SetActivity) {
                    SetActivity child = (SetActivity) context;
                    //You set a position
                    child.setPosition(position);
                } else Toast.makeText(context, "Error! SetActivity renamed!", Toast.LENGTH_SHORT).show();


                //show the context menu
                v.showContextMenu();


                return true;
            });



            //Text change listeners in items of RecyclerView
            binding.tvExerciceGroup.addTextChangedListener(new TextWatcher() {
                //Create time delaying.
                Timer timer = new Timer();
                final long DELAY = 500; // time delay msec
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    timer.cancel();
                    timer = new Timer();
                    //create new schedule and change object in holder
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //change object in holder
                            if( !(s.toString()).equals("") )
                                oneSet1.getExerciseGroup().setExeGroupName(s.toString());
                            //save change in DataBase
                            ((SetActivity)context).updateTableDBFromList(getSetOneSetList());
                        }
                    } , DELAY);
                }
            });
            binding.tvExerciceName.addTextChangedListener(new TextWatcher() {
                //Create time delaying.
                Timer timer = new Timer();
                final long DELAY = 500; // time delay msec
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(Editable s) {
                    timer.cancel();
                    timer = new Timer();
                    //create new schedule and change object in holder
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //change object in holder
                            if( !(s.toString()).equals("") )
                                oneSet1.getExe().setExeName(s.toString());
                            //save change in DataBase
                            ((SetActivity)context).updateTableDBFromList(getSetOneSetList());
                        }
                    } , DELAY);
                }
            });
            binding.etRepeat.addTextChangedListener(new TextWatcher() {
                //Create time delaying.
                Timer timer = new Timer();
                final long DELAY = 1000; // time delay msec
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    timer.cancel();
                    timer = new Timer();
                    //create new schedule and change object in holder
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //change object in holder
                            if( !(s.toString()).equals("") )
                                oneSet1.getRecordSet().getRepeats().changeRepeats(s.toString());
                            //save change in DataBase
                            ((SetActivity)context).updateTableDBFromList(getSetOneSetList());
                        }
                    } , DELAY);
                }
            });
            binding.etWeight.addTextChangedListener(new TextWatcher() {
                Timer timer = new Timer();
                final long DELAY = 1000; // time delay msec
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    timer.cancel();
                    timer = new Timer();
                    //create new schedule and change object in holder
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            //change object in holder
                            if( !(s.toString()).equals("") )
                                oneSet1.getRecordSet().getWeight().changeWeight(s.toString());
                            //save change in DataBase
                            ((SetActivity)context).updateTableDBFromList(getSetOneSetList());
                        }
                    } , DELAY);
                }
            });
        }



        //Processing data from the object to animate the view in this holder
        void initItemView(OneSet oneSet){
            //object in holder
            oneSet1 = oneSet;

            //put data to item from object
            String exeGroupName = oneSet.getExerciseGroup().toString();
            binding.tvExerciceGroup.setText(exeGroupName);
            //
            String exeName = oneSet.getExe().toString();
            binding.tvExerciceName.setText(exeName);
            //
            int weight = oneSet.getRecordSet().getWeight().getIntWeight();
            binding.etWeight.setText(String.valueOf(weight));
            if (weight == 0) binding.etWeight.setText("");
            //
            int repeats = oneSet.getRecordSet().getRepeats().getIntRepeats();
            binding.etRepeat.setText(String.valueOf(repeats));
            if (repeats == 0) binding.etRepeat.setText("");
            //



            //
            binding.tvExerciceName. setTextSize(Float.parseFloat(selectedTextSize));
            binding.etWeight.       setTextSize(Float.parseFloat(selectedTextSize));
            binding.etRepeat.       setTextSize(Float.parseFloat(selectedTextSize));
            binding.tvExerciceGroup.setTextSize(Float.parseFloat(selectedTextSize));

            //onClick TextViews
            binding.tvExerciceGroup.setOnClickListener(v -> {
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();
                //onClick to get Exercises Groups Activity

                //launch the activity with groups of muscles
                Intent intent = new Intent(context, MusclesGroupsActivity.class);
                context.getActivityResultLauncher().launch(intent);
            });

        }
    }
}
