package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;



public class Exercise {

    /* PARAMETERS */
    private String exercise;


    /* GETTERS AND SETTERS */
    @NonNull
    @Override
    public String toString() {
        return exercise;
    }
    public void setExeName(String my_new_exeName) {
        exercise = my_new_exeName;
    }


    /* CONSTRUCTOR */
    public Exercise(String exercise) {
        this.exercise = exercise;
    }
}
