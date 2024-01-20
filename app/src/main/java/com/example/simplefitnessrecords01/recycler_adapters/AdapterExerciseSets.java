package com.example.simplefitnessrecords01.recycler_adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.UI_activities.ExerciseSetActivity;
import com.example.simplefitnessrecords01.UI_activities.ExercisesActivity;
import com.example.simplefitnessrecords01.databinding.RecyclerSetActivityBinding;
import com.example.simplefitnessrecords01.fitness.ExerciseSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class AdapterExerciseSets extends RecyclerView.Adapter<AdapterExerciseSets.HolderExerciseSetList> {

    ExerciseSetActivity context;


    //LIST of performed approaches in training
    private List<ExerciseSet> exerciseSetList = new ArrayList<>();




    /********  GETTERS-SETTERS ********************/
    public List<ExerciseSet> getExerciseSetList() {
        return exerciseSetList;
    }

    //Задати список підходів
    public void setExerciseSetList(List<ExerciseSet> exerciseSetList) {
        this.exerciseSetList = exerciseSetList;
    }

    //Add another set to the adapter list
    public void addExerciseSet(ExerciseSet exerciseSet) {
        exerciseSetList.add(exerciseSet);
    }
    public ExerciseSet getExerciseSet(int position) {
        return exerciseSetList.get(position);
    }

    //The method returns the number of objects
    @Override
    public int getItemCount() {
        return exerciseSetList.size();
    }




    /************* Constructors ***********************/
    public AdapterExerciseSets(Context context) {
        this.context = (ExerciseSetActivity) context;
    }

    public AdapterExerciseSets(Context context, List<ExerciseSet> exerciseSetList) {
        this.context = (ExerciseSetActivity) context;
        this.exerciseSetList = exerciseSetList;
    }




    /******************** Creation of View and Holder blank ***************/
    @NonNull
    @Override
    public HolderExerciseSetList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Створення В'ю
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_set_activity, parent, false);

        //Створення холдера
        return new HolderExerciseSetList(view);
    }




    /****** Initialize (Animate) the View and the holder with the data of the object from the list **********/
    @Override
    public void onBindViewHolder(@NonNull HolderExerciseSetList holder, int position) {
        //Витягти об'єкт зі списку
        ExerciseSet exerciseSet = exerciseSetList.get(position);
        //Через метод в холдері запихнути дані в В'ю
        holder.initItemView(exerciseSet);
    }




    /*******************  Holder  *****************/
    public class HolderExerciseSetList extends RecyclerView.ViewHolder {


        //text size
        String selectedTextSize = context.getString(R.string.default_text_size);


        //object in holder
        ExerciseSet exerciseSet1 = null;

        //binder
        RecyclerSetActivityBinding binding = RecyclerSetActivityBinding.bind(itemView);

        //constructor
        public HolderExerciseSetList(@NonNull View itemView) {
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
                    ((ExerciseSetActivity)context).getMenuInflater().inflate(R.menu.context_set_activity, menu);
                });

                //If the context is ExerciseSetActivity
                if (context instanceof ExerciseSetActivity) {
                    ExerciseSetActivity exerciseSetActivity = (ExerciseSetActivity) context;
                    //You set a position
                    exerciseSetActivity.setPosition(position);
                } else Toast.makeText(context, "Error! ExerciseSetActivity renamed!", Toast.LENGTH_SHORT).show();


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
                                exerciseSet1.getExecutedExercise().getMuscleGroup().setBodyPart(s.toString());
                            //save change in DataBase
                            ((ExerciseSetActivity)context).updateTableDBFromList(getExerciseSetList());
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
                                exerciseSet1.getExecutedExercise().setExerciseName(s.toString());
                            //save change in DataBase
                            ((ExerciseSetActivity)context).updateTableDBFromList(getExerciseSetList());
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
                                exerciseSet1.getExecutedExercise().getRepeats().setRepeats(Integer.parseInt(s.toString()));
                            //save change in DataBase
                            ((ExerciseSetActivity)context).updateTableDBFromList(getExerciseSetList());
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
                                exerciseSet1.getExecutedExercise().getWeight().setWeight(Integer.parseInt(s.toString()));
                            //save change in DataBase
                            ((ExerciseSetActivity)context).updateTableDBFromList(getExerciseSetList());
                        }
                    } , DELAY);
                }
            });
        }



        //Processing data from the object to animate the view in this holder
        void initItemView(ExerciseSet exerciseSet){
            //object in holder
            exerciseSet1 = exerciseSet;

            //put data to item from object
            String exeGroupName = exerciseSet.getExecutedExercise().getMuscleGroup().getBodyPart();
            binding.tvExerciceGroup.setText(exeGroupName);

            // show muscles in recycler view
            String[] muscles = exerciseSet.getExecutedExercise().getMuscleGroup().getMuscles();
            StringBuilder sb = new StringBuilder();
            for(String muscle : muscles) {
                if(  muscle != null & !Objects.equals(muscle, "")) {
                    sb.append(muscle);
                    sb.append("; ");
                }
            }
            binding.tvMuskles.setText(sb.toString());



            //
            String exeName = exerciseSet.getExecutedExercise().toString();
            binding.tvExerciceName.setText(exeName);

            //
            int weight = exerciseSet.getExecutedExercise().getWeight().toInt();
            binding.etWeight.setText(String.valueOf(weight));
            if (weight == 0) binding.etWeight.setText("");

            //
            int repeats = exerciseSet.getExecutedExercise().getRepeats().toInt();
            binding.etRepeat.setText(String.valueOf(repeats));
            if (repeats == 0) binding.etRepeat.setText("");

            //
            binding.tvExerciceName. setTextSize(Float.parseFloat(selectedTextSize));
            binding.etWeight.       setTextSize(Float.parseFloat(selectedTextSize));
            binding.etRepeat.       setTextSize(Float.parseFloat(selectedTextSize));
            binding.tvExerciceGroup.setTextSize(Float.parseFloat(selectedTextSize));

            //onClick TextViews
            binding.imBtnPlus.setOnClickListener(v -> {
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();

                lunchActivityToChoiseExercise();
            });
            binding.tvExerciceName.setOnClickListener(v -> {
                Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show();

                lunchActivityToChoiseExercise();
            });


        }






        /*********************** ON CLICKS ***************************/
        //lunch Activity To Choise Exercise
        private void lunchActivityToChoiseExercise(){
            //launch the activity with groups of muscles
            Intent intent = new Intent(context, ExercisesActivity.class);
            intent.putExtra("goal_launch", "select_exe");
            intent.putExtra("position", getAbsoluteAdapterPosition());
            intent.putExtra("unicName", exerciseSet1.getNameWorkout());
            context.getActivityChoseExeLauncher().launch(intent);

            //save the position in ExerciseSetActivity
            ((ExerciseSetActivity) context).setPosition(getAbsoluteAdapterPosition());
        }



    }
}
