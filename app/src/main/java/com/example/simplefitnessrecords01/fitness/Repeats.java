package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;

public class Repeats {
    /* PARAMETERS */
    private int repeats;



    /* GETTERS AND SETTERS */
    public int getIntRepeats() {
        return repeats;
    }
    @NonNull
    @Override
    public String toString() {
        return String.valueOf(repeats);
    }

    public void changeRepeats(String s) {
        repeats = Integer.parseInt(s);
    }




    /* CONSTRUCTOR */
    public Repeats(int repeats) {
        this.repeats = repeats;
    }
}
