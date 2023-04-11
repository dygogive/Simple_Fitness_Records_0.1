package com.example.simplefitnessrecords01.fitness;

public class Training {


    /* PARAMETERS */
    private int id;
    private String day, name, inform;


    

    /* GETTERS AND SETTERS */
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getInfo() {
        return inform;
    }

    
    
    
    

    /* CONSTRUCTOR */
    public Training(int id, String day, String name, String inform) {
        this.id = id;
        this.day = day;
        this.name = name;
        this.inform = inform;
    }
}
