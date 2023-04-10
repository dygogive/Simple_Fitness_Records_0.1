package com.example.simplefitnessrecords01.fitness;

import androidx.annotation.StyleableRes;

import java.util.Arrays;

public class Muscles {

    String[] muscles;

    public String[] getMuscles() {
        return muscles;
    }

    public Muscles(String[] muscles) {
        this.muscles = Arrays.copyOfRange(muscles, 0 , 4);
    }
}
