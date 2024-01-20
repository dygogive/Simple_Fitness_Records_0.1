package com.example.simplefitnessrecords01.fitness;

import java.util.Arrays;

public class MuscleGroup {

    String bodyPart;
    String[] muscles;


    public String getBodyPart() {
        return bodyPart;
    }

    public String[] getMuscles() {
        return muscles;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    /******************* CONSTRUCTOR ***********************/
    public MuscleGroup(String bodyPart, String[] muscles) {
        this.muscles = muscles;
        this.bodyPart = bodyPart;
    }
    public MuscleGroup(String bodyPart) {
        this.muscles = new String[]{"some muscle"};
        this.bodyPart = bodyPart;
    }
}
