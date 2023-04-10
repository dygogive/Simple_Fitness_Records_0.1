package com.example.simplefitnessrecords01.recycler_adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterRecyclerExercises extends RecyclerView.Adapter<AdapterRecyclerExercises.ViewHolder> {


    public AdapterRecyclerExercises() {
    }

    @NonNull
    @Override
    public AdapterRecyclerExercises.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterRecyclerExercises.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



    public class SetTrainingHolder extends RecyclerView.ViewHolder {




        public SetTrainingHolder(@NonNull View itemView) {
            super(itemView);
        }








    }


}
