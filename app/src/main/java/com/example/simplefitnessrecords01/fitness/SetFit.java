package com.example.simplefitnessrecords01.fitness;

public class SetFit {
    Exercise exe;
    RecordSet recordSet;

    public Exercise getExe() {
        return exe;
    }

    public RecordSet getRecordSet() {
        return recordSet;
    }

    public void setExe(Exercise exe) {
        this.exe = exe;
    }

    public void setRecordSet(RecordSet recordSet) {
        this.recordSet = recordSet;
    }

    public SetFit(Exercise exe, RecordSet recordSet) {
        this.exe = exe;
        this.recordSet = recordSet;
    }

}
