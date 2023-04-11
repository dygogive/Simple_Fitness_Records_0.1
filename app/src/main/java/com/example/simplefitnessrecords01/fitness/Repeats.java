package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;

public class Repeats {
    /* PARAMETERS */
    private int repeats;



    /* GETTERS AND SETTERS */
    @NonNull
    @Override
    public String toString() {
        return String.valueOf(repeats);
    }

    public int toInt() {
        return repeats;
    }

    public void setRepeats(int repeats) {
        this.repeats = repeats;
    }

    /* CONSTRUCTOR */
    public Repeats(int repeats) {
        this.repeats = repeats;
    }
}
