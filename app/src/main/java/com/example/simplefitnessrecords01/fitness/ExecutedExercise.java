package com.example.simplefitnessrecords01.fitness;

public class ExecutedExercise extends Exercise {

    /* PARAMETERS */
    private final Weight weight;
    private final Repeats repeats;



    /* GETTERS AND SETTERS */
    public Weight getWeight() {
        return weight;
    }

    public Repeats getRepeats() {
        return repeats;
    }






    /* CONSTRUCTOR */

    public ExecutedExercise(String exeName, MuscleGroup muscleGroup, Weight weight, Repeats repeats) {
        super(exeName , muscleGroup);
        this.weight = weight;
        this.repeats = repeats;
    }

}
