package com.example.simplefitnessrecords01.fitness;

public class SetTraining {
    int id;
    String day, name, subname;

    public SetTraining(int id, String day, String name, String subname) {
        this.id = id;
        this.day = day;
        this.name = name;
        this.subname = subname;
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
        this.subname = subname;
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

    public String getSubname() {
        return subname;
    }
}
