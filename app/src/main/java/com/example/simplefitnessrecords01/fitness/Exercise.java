package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;



public class Exercise {

    /* PARAMETERS */
    private String exerciseName;

    private MuscleGroup muscleGroup;







    /* GETTERS AND SETTERS */
    @NonNull
    @Override
    public String toString() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public MuscleGroup getMuscleGroup() {
        return muscleGroup;
    }

    /* CONSTRUCTOR */
    public Exercise(String exerciseName, MuscleGroup muscleGroup) {
        this.exerciseName = exerciseName;
        this.muscleGroup = muscleGroup;
    }

}
