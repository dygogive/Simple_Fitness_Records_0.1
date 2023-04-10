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
    public ExecutedExercise(Weight weight, Repeats repeats) {
        super("null" , null , null);
        this.weight = weight;
        this.repeats = repeats;
    }

    public ExecutedExercise(String exeName, BodyPart bodyPart, Muscles muscles, Weight weight, Repeats repeats) {
        super(exeName , bodyPart , muscles);
        this.weight = weight;
        this.repeats = repeats;
    }

}
