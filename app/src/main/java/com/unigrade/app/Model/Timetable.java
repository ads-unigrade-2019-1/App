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
        for (int i=0; i<timetableClass.size(); i++){
            Log.d("Timetable", timetableClass.get(i).getCodeLetter());
            Log.d("Timetable", timetableClass.get(i).getSubjectCode());
        }

    }


}
