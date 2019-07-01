package com.unigrade.app.Model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Timetable implements Serializable {
    private ArrayList<SubjectClass> timetableClasses;

    public Timetable(){
    }

    public Timetable(ArrayList<SubjectClass> timetableClass){
        this.timetableClasses = timetableClass;
    }

    public ArrayList<SubjectClass> getTimetableClasses() {
        return timetableClasses;
    }

    public ArrayList<SubjectClass> getTimetableClass() {
        return timetableClasses;
    }

    public void printTimetable(){
        try {
            Log.d("TimetablePrint", "-----------------------------------------------");
            for (int i = 0; i < timetableClasses.size(); i++) {
                Log.d("TimetablePrint", "Nome: " +  timetableClasses.get(i).getName());
                Log.d("TimetablePrint", "Horário: " + timetableClasses.get(i).getSchedulesString());
            }
        } catch(RuntimeException e){
            e.printStackTrace();
        }

    }
}
