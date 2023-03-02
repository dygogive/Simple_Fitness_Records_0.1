package com.example.simplefitnessrecords01.recycler_views;


import com.example.simplefitnessrecords01.fitness.SetTraining;

//інтерфейс слухача натискань на елементи RecyclerView
public interface OnItemRecyclerClickListener {
    void onItemClick(SetTraining setTraining);
}
