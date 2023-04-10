package com.example.simplefitnessrecords01.recycler_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.RecyclerExercisesItemBinding;
import com.example.simplefitnessrecords01.fitness.Exercise;

import java.util.LinkedList;
import java.util.List;

public class AdapterRecyclerExercises extends RecyclerView.Adapter<AdapterRecyclerExercises.ExeHolder> {

    private Context context;

    private List<Exercise> exerciseList = new LinkedList<>();

    public List<Exercise> getExerciseList() {
        return exerciseList;
    }









    /********************** CONSTRUCTOR *****************************/
    public AdapterRecyclerExercises(Context context, List<Exercise> exerciseList) {
        this.context      = context;
        this.exerciseList = exerciseList;
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
            itemBinding.textView2.setText(exercise.toString());
        }

    }


}
