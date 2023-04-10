package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;



public class Exercise {

    /* PARAMETERS */
    private String exerciseName;

    private Muscles muscles;
    private BodyPart bodyPart;


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
    public Exercise(String exerciseName, BodyPart bodyPart, Muscles muscles) {
        this.exerciseName = exerciseName;
        this.bodyPart = bodyPart;
        this.muscles = muscles;
    }

}
