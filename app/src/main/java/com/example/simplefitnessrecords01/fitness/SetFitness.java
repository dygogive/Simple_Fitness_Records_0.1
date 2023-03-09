package com.example.simplefitnessrecords01.fitness;

public class SetFitness {
    Exercise exe;
    RecordExercise recordExercise;

    public Exercise getExe() {
        return exe;
    }

    public RecordExercise getRecordSet() {
        return recordExercise;
    }

    public void setExe(Exercise exe) {
        this.exe = exe;
    }

    public void setRecordSet(RecordExercise recordExercise) {
        this.recordExercise = recordExercise;
    }





    public SetFitness(Exercise exe, RecordExercise recordExercise) {
        this.exe = exe;
        this.recordExercise = recordExercise;
    }


    public SetFitness() {
        this.exe = new Exercise("untitled");
        this.recordExercise = new RecordExercise(new Weight(0), new Repeats(0));
    }

}
