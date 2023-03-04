package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;

public class Repeats {
    int repeats;

    public Repeats(int repeats) {
        this.repeats = repeats;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(repeats);
    }
}
