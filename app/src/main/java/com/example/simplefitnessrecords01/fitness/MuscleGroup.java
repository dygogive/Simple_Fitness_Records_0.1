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

    public void setBodyPart(String bodyGroup) {
        this.bodyPart = bodyGroup;
    }

    /******************* CONSTRUCTOR ***********************/
    public MuscleGroup(String bodyGroup, String[] muscles) {
        this.muscles = muscles;
        this.bodyPart = bodyGroup;
    }
    public MuscleGroup(String bodyPart) {
        this.muscles = new String[]{"some muscle"};
        this.bodyPart = bodyPart;
    }
}
