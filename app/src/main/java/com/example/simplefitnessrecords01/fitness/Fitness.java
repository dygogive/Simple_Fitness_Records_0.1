package com.example.simplefitnessrecords01.fitness;

public class Fitness {
    int id = 0;
    String day, name, fitnessName;



    public Fitness(int id, String day, String name, String fitnessName) {
        this.id = id;
        this.day = day;
        this.name = name;
        this.fitnessName = fitnessName;
    }



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
        this.fitnessName = subname;
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

    public String getFitnessName() {
        return fitnessName;
    }
}
