package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;

public class Weight {
    int weight;

    public Weight(int weight) {
        this.weight = weight;
    }

    @NonNull
    @Override
    public String toString() {
        return String.valueOf(weight);
    }
}
