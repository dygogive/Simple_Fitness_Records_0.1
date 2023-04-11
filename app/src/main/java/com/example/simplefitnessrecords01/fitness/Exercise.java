package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;



public class Exercise {

    /* PARAMETERS */
    private String exerciseName;

    private MuscleGroup[] muscleGroups;

    public MuscleGroup[] getMuscleGroups() {
        return muscleGroups;
    }
    public MuscleGroup getMuscleGroup(int pos) {
        return muscleGroups[pos];
    }






    /* GETTERS AND SETTERS */
    @NonNull
    @Override
    public String toString() {
        return exerciseName;
    }
    public void setExeName(String my_new_exeName) {
        exerciseName = my_new_exeName;
    }







    /* CONSTRUCTOR */
    public Exercise(String exerciseName, MuscleGroup[] muscleGroups) {
        this.exerciseName = exerciseName;
        this.muscleGroups = muscleGroups;
    }

}
