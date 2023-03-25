package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.NonNull;

public class Weight {

    /* PARAMETERS */
    private int weight;



    /* GETTERS AND SETTERS */
    public int getIntWeight() {
        return weight;
    }
    @NonNull
    @Override
    public String toString() {
        return String.valueOf(weight);
    }

    public void changeWeight(String w) {
        weight = Integer.parseInt(w);
    }




    /* CONSTRUCTOR */
    public Weight(int weight) {
        this.weight = weight;
    }
}
