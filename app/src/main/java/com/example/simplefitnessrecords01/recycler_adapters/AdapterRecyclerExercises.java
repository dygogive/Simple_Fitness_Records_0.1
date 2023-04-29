package com.example.simplefitnessrecords01.recycler_adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.UI_activities.ExercisesActivity;
import com.example.simplefitnessrecords01.UI_activities.MainActivity;
import com.example.simplefitnessrecords01.databinding.RecyclerExercisesItemBinding;
import com.example.simplefitnessrecords01.fitness.Exercise;

import java.util.LinkedList;
import java.util.List;

public class AdapterRecyclerExercises extends RecyclerView.Adapter<AdapterRecyclerExercises.ExeHolder> {

    private Context context;

    private boolean selectExercise = false;

    private List<Exercise> exerciseList = new LinkedList<>();

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }

    public void updateList(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    public Exercise getItem(int position) {
        return exerciseList.get(position);
    }








    /********************** CONSTRUCTOR *****************************/
    public AdapterRecyclerExercises(Context context, List<Exercise> exerciseList) {
        this.context      = context;
        this.exerciseList = exerciseList;
    }
    public AdapterRecyclerExercises(Context context, List<Exercise> exerciseList, boolean selectExercise) {
        this.context        = context;
        this.exerciseList   = exerciseList;
        this.selectExercise = selectExercise;
    }






    @NonNull
    @Override
    public ExeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //creating a view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_exercises_item, parent, false);
        //creating a SetsHolder
        return new AdapterRecyclerExercises.ExeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExeHolder exeHolder, int position) {
        exeHolder.onBind(exerciseList.get(position));
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }





    /************************** HOLDER *****************************************/

    public class ExeHolder extends RecyclerView.ViewHolder {

        //Binding
        RecyclerExercisesItemBinding itemBinding = RecyclerExercisesItemBinding.bind(itemView);


        public ExeHolder(@NonNull View itemView) {
            super(itemView);
        }


        private void onBind(Exercise exercise) {
            itemBinding.tvExercise.setText(exercise.toString());
            itemBinding.tvGroup   .setText(exercise.getMuscleGroup().getBodyPart());
            String[] muscles = exercise.getMuscleGroup().getMuscles();
            StringBuilder sbMuscles = new StringBuilder();
            boolean b = false;
            for (String s : muscles ) {
                if(s == null) break;
                if(b) sbMuscles.append("; ");
                sbMuscles.append(s);
                b = true;
            }
            itemBinding.tvMuscles1.setText(sbMuscles.toString());

            //set size of text
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String selectedTextSize = preferences.getString("text_size_preference", context.getString(R.string.default_text_size));
            itemBinding.tvExercise. setTextSize(Float.parseFloat(selectedTextSize));
            itemBinding.tvGroup.    setTextSize(Float.parseFloat(selectedTextSize));
            itemBinding.tvMuscles1. setTextSize(Float.parseFloat(selectedTextSize));


            if(selectExercise) {
                itemBinding.imageSelect.setVisibility(View.VISIBLE);
            }


            //onClick listener
            itemBinding.imgMenuExe.setOnClickListener(v -> {
                Toast.makeText(context, "OnClick", Toast.LENGTH_SHORT).show();

                //position
                int position = getAbsoluteAdapterPosition();
                //context menu registry
                v.setOnCreateContextMenuListener((menu, v1, menuInfo) -> {
                    // We fill the context menu with items
                    ((Activity)context).getMenuInflater().inflate(R.menu.context_exercises_activity, menu);
                });




                //If the context is MainActivity
                if (context instanceof ExercisesActivity) {
                    ExercisesActivity child = (ExercisesActivity) context;
                    //You set a position
                    child.setPositioContextMenu(position);
                } else Toast.makeText(context, "Error! MainActivity renamed!", Toast.LENGTH_SHORT).show();



                //show the context menu
                v.showContextMenu();



            });

            itemBinding.imageSelect.setOnClickListener(v -> {
                Intent intent = new Intent();

                //create data
                String[] txtData = new String[6];
                txtData[0] = exercise.getMuscleGroup().getBodyPart();
                int i = 1;
                for (String s : muscles ) {
                    if(s == null) txtData[i++] = "";
                    else txtData[i++] = s;
                }
                txtData[i] = exercise.getExerciseName();

                for(String s : txtData){
                    Log.d("data" , s);
                }


                //put extra
                intent.putExtra("muscleGroupsExtra" , txtData);
                ((Activity)context).setResult(Activity.RESULT_OK,intent);

                //finish
                ((Activity)context).finish();
            });
        }

    }

}
