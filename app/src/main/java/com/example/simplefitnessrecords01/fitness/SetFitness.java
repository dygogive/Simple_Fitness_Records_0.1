package com.example.simplefitnessrecords01.fitness;


public class SetFitness {
    int id = -1;
    public int getId() {
        return id;
    }



    String fitnessName = "tempFitnessName";

    public String getFitnessName() {
        return fitnessName;
    }

    public void setFitnessName(String fitnessName) {
        this.fitnessName = fitnessName;
    }







    Exercise exe;
    public void setExe(Exercise exe) {
        this.exe = exe;
    }
    public Exercise getExe() {
        return exe;
    }





    RecordExercise recordExercise;
    public RecordExercise getRecordSet() {
        return recordExercise;
    }
    public void setRecordSet(RecordExercise recordExercise) {
        this.recordExercise = recordExercise;
    }





    public SetFitness(int id, Exercise exe, RecordExercise recordExercise, String fitnessName) {
        this.exe = exe;
        this.recordExercise = recordExercise;
        this.id = id;
        this.fitnessName = fitnessName;
    }

    public SetFitness(String fitnessName) {
        this.exe = new Exercise("");
        this.recordExercise = new RecordExercise(new Weight(0),new Repeats(0));
        this.id = -1;
        this.fitnessName = fitnessName;
    }


}
