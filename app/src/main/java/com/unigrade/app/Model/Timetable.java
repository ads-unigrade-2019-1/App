package com.unigrade.app.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Timetable {
    //TODO Pegar da classe Horário
    ArrayList<ArrayList<String>> week; //length = 6

    public Timetable(){
    }

    public Timetable(ArrayList<ArrayList<String>> week){
        this.week = week;
    }
    /*
    public HashMap<String, HashMap<String, String>> getFormatedTimetable(){
        HashMap<String, String> monday;
        HashMap<String, String> tuesday;
        HashMap<String, String> wednesday;
        HashMap<String, String> thursday;
        HashMap<String, String> friday;
        HashMap<String, String> saturday;

        HashMap<String, String> first;
        HashMap<String, String> second;
        HashMap<String, String> third;
        HashMap<String, String> fourth;
        HashMap<String, String> fifth;
        HashMap<String, String> sixth;
    }*/


}
