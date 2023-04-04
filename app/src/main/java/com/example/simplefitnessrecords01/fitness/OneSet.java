package com.example.simplefitnessrecords01.fitness;


public class OneSet {

    /* PARAMETERS */
    private int id;
    public int getId() {
        return id;
    }
    private Exercise exe;
    ExerciseGroup exerciseGroup;
    private ExecutedExercise executedExercise;
    private String uniqueFitTraining;




    /* GETTERS AND SETTERS */
    public String getUniqueFitTraining() {
        return uniqueFitTraining;
    }
    public void setUniqueFitTraining(String uniqueFitTraining) {
        this.uniqueFitTraining = uniqueFitTraining;
    }
    public void setExe(Exercise exe) {
        this.exe = exe;
    }
    public Exercise getExe() {
        return exe;
    }

    public void setExerciseGroup(ExerciseGroup exerciseGroup) {
        this.exerciseGroup = exerciseGroup;
    }

    public ExerciseGroup getExerciseGroup() {
        return exerciseGroup;
    }

    public ExecutedExercise getRecordSet() {
        return executedExercise;
    }
    public void setRecordSet(ExecutedExercise executedExercise) {
        this.executedExercise = executedExercise;
    }






    /* CONSTRUCTOR */
    public OneSet(int id, Exercise exe, ExerciseGroup exerciseGroup, ExecutedExercise executedExercise, String uniqueFitTraining) {
        this.exe = exe;
        this.executedExercise = executedExercise;
        this.id = id;
        this.uniqueFitTraining = uniqueFitTraining;
        this.exerciseGroup = exerciseGroup;
    }

    public OneSet(int id, Exercise exe, ExecutedExercise executedExercise, String uniqueFitTraining) {
        this.exe = exe;
        this.executedExercise = executedExercise;
        this.id = id;
        this.uniqueFitTraining = uniqueFitTraining;
    }

    public OneSet(String uniqueFitTraining) {
        this.exe = new Exercise("");
        this.executedExercise = new ExecutedExercise(new Weight(0),new Repeats(0));
        this.id = -1;
        this.uniqueFitTraining = uniqueFitTraining;
    }
}
