package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;


public class ExerciseGroup {

    /* PARAMETERS */
    private String exerciseGroup;


    /* GETTERS AND SETTERS */
    @NonNull
    @Override
    public String toString() {
        return exerciseGroup;
    }


    public void setExeGroupName(String my_new_exeGroupName) {
        exerciseGroup = my_new_exeGroupName;
    }


    /* CONSTRUCTOR */
    public ExerciseGroup(String exerciseGroup1) {
        exerciseGroup = exerciseGroup1;
    }

}
