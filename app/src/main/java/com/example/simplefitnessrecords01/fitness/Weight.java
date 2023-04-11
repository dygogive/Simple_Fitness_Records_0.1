package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;

public class Weight {

    /* PARAMETERS */
    private int weight;





    /* GETTERS AND SETTERS */
    @NonNull
    @Override
    public String toString() {
        return String.valueOf(weight);
    }

    public int toInt() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    /* CONSTRUCTOR */
    public Weight(int weight) {
        this.weight = weight;
    }
}
