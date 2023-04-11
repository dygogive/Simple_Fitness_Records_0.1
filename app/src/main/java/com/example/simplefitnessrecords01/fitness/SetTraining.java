package com.example.simplefitnessrecords01.fitness;


public class SetTraining extends EmptySetTraining {

    /* PARAMETERS */
    private int id;
    private ExecutedExercise executedExercise;




    /* GETTERS AND SETTERS */
    public int getId() {
        return id;
    }

    public ExecutedExercise getExecutedExercise() {
        return executedExercise;
    }






    /* CONSTRUCTOR */
    public SetTraining(int id, ExecutedExercise executedExercise, String uniqueFitTraining) {
        super(uniqueFitTraining);
        this.executedExercise = executedExercise;
        this.id = id;
    }


}
