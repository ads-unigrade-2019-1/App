package com.unigrade.app.Model;

import android.util.Log;

import java.util.ArrayList;

public class Timetable {
    //TODO Pegar da classe Horário
    ArrayList<SubjectClass> timetableClass;

    public Timetable(){
    }

    public Timetable(ArrayList<SubjectClass> timetableClass){
        this.timetableClass = timetableClass;
    }

    public void printTimetable(){
        //test method

        Log.d("Timetable", timetableClass.toString());
    }


}
