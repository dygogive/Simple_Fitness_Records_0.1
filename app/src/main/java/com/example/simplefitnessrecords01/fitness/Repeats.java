package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;

public class Repeats {
    int repeats;

    public int getIntRepeats() {
        return repeats;
    }

    public Repeats(int repeats) {
        this.repeats = repeats;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(repeats);
    }

    public void changeRepeats(String s) {
        repeats = Integer.parseInt(s);
    }
}
