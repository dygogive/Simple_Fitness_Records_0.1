package com.example.simplefitnessrecords01.fitness;

public class OneFitnessTraining {
    
    /* PARAMETERS */
    private int id;
    private String day, name, uniqueFitTraining;


    

    /* GETTERS AND SETTERS */
    public void setId(int id) {
        this.id = id;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubname(String subname) {
        this.uniqueFitTraining = subname;
    }

    public int getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public String getName() {
        return name;
    }

    public String getUniqueName() {
        return uniqueFitTraining;
    }

    
    
    
    

    /* CONSTRUCTOR */
    public OneFitnessTraining(int id, String day, String name, String uniqueFitTraining) {
        this.id = id;
        this.day = day;
        this.name = name;
        this.uniqueFitTraining = uniqueFitTraining;
    }
}
