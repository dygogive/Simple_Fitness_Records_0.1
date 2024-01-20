package com.example.simplefitnessrecords01.fitness;


public class ExerciseSet extends EmptyExerciseSet {

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
    public ExerciseSet(int id, ExecutedExercise executedExercise, String uniqueFitTraining) {
        super(uniqueFitTraining);
        this.executedExercise = executedExercise;
        this.id = id;
    }


}
