package com.example.simplefitnessrecords01.fitness;

public class RecordExercise {
    Weight weight;
    Repeats repeats;

    public Weight getWeight() {
        return weight;
    }

    public Repeats getRepeats() {
        return repeats;
    }

    public RecordExercise(Weight weight, Repeats repeats) {
        this.weight = weight;
        this.repeats = repeats;
    }

}
