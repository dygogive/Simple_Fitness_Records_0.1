package com.example.simplefitnessrecords01.fitness;

public class MuscleGroup {

    private String bodyPart = "";

    private Muscles muscles;

    public String getBodyPart() {
        return bodyPart;
    }


    public MuscleGroup(String bodyPart , Muscles muscles) {
        this.bodyPart = bodyPart;
        this.muscles = muscles;
    }
}
