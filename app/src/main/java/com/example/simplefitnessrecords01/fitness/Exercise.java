package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;

public class Exercise {
    String exercise;

    public Exercise(String exercise) {
        this.exercise = exercise;
    }

    @NonNull
    @Override
    public String toString() {
        return exercise;
    }
}
