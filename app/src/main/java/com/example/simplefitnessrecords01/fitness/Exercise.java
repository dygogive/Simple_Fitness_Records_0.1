package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;

public class Exercise {
    String exercise = null;

    public Exercise(String exercise) {
        this.exercise = exercise;
    }

    @NonNull
    @Override
    public String toString() {
        return exercise;
    }

    public void setExeName(String my_new_exeName) {
        exercise = my_new_exeName;
    }
}
