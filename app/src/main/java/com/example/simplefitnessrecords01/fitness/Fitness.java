package com.example.simplefitnessrecords01.fitness;

import java.util.UUID;

public class Fitness {
    int id = 0;
    String day, name, subname;

    private String uniqueID;


    public Fitness(int id, String day, String name, String subname, String unicID) {
        this.id = id;
        this.day = day;
        this.name = name;
        this.subname = subname;
        this.uniqueID = unicID;
    }

    public Fitness(String day, String name, String subname, String unicID) {
        this.id = id;
        this.day = day;
        this.name = name;
        this.subname = subname;
        this.uniqueID = unicID;


    }


    public String getUnicID() {
        return uniqueID;
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
