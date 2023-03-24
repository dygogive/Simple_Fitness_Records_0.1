package com.example.simplefitnessrecords01.fitness;


public class SetFitness {
    int id = -1;
    Exercise exe;
    RecordExercise recordExercise;

    public Exercise getExe() {
        return exe;
    }

    public RecordExercise getRecordSet() {
        return recordExercise;
    }

    public int getId() {
        return id;
    }

    public void setExe(Exercise exe) {
        this.exe = exe;
    }

    public void setRecordSet(RecordExercise recordExercise) {
        this.recordExercise = recordExercise;
    }





    public SetFitness(int id, Exercise exe, RecordExercise recordExercise) {
        this.exe = exe;
        this.recordExercise = recordExercise;
        this.id = id;
    }


    public SetFitness() {
        this.exe = new Exercise("untitled");
        this.recordExercise = new RecordExercise(new Weight(0), new Repeats(0));
    }

}
