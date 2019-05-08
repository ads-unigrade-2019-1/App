package com.unigrade.app.Model;

import java.util.ArrayList;

public class Timetable {
    //TODO Pegar da classe Horário
    ArrayList<ArrayList<String>> week; //length = 6

    public Timetable(){
    }

    public Timetable(ArrayList<ArrayList<String>> week){
        this.week = week;
    }
}
