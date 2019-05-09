package com.unigrade.app.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Timetable {
    //TODO Pegar da classe Horário
    HashMap<String, ArrayList<String>> week; //length = 6

    public Timetable(){
    }

    public Timetable(HashMap<String, ArrayList<String>> week){
        this.week = week;
    }

    public void printTimetable(){
        //test method

        Log.d("Timetable", week.toString());
    }


}
