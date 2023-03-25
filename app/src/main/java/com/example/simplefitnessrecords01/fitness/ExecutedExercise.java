package com.example.simplefitnessrecords01.fitness;

public class ExecutedExercise {

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
    public ExecutedExercise(Weight weight, Repeats repeats) {
        this.weight = weight;
        this.repeats = repeats;
    }

}
