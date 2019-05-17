package com.unigrade.app.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Timetable implements Serializable {
    ArrayList<SubjectClass> timetableClass;

    public Timetable(){
    }

    public Timetable(ArrayList<SubjectClass> timetableClass){
        this.timetableClass = timetableClass;
    }

    public ArrayList<SubjectClass> getTimetableClass() {
        return timetableClass;
    }

    public void printTimetable(){
//        test method
        try {
            for (int i = 0; i < timetableClass.size(); i++) {
                Log.d("Timetable", timetableClass.get(i).getName());
                Log.d("Timetable", timetableClass.get(i).getSubjectCode());
                Log.d("Timetable", timetableClass.get(i).getPriority());
            }
        } catch(RuntimeException e){
            e.printStackTrace();
        }

    }
}
