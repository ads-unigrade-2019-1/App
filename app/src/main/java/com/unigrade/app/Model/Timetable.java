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

    public ArrayList<SubjectClass> getTimetableClass() {
        return timetableClasses;
    }

    public ArrayList<ClassMeeting> getTimetableMeetings(){
        ArrayList<ClassMeeting> classMeetings = new ArrayList<>();

        for (SubjectClass timetableClass : timetableClasses) {
            classMeetings.addAll(timetableClass.getSchedules());
        }

        return classMeetings;
    }

    public ClassMeeting findMeetingByTimeDay(final String initHour, String day){
        ClassMeeting findedClassMeeting = new ClassMeeting();

        for(ClassMeeting classMeeting : this.getTimetableMeetings()){
            if(classMeeting.getInit_hour().equals(initHour) && classMeeting.getDay().equals(day)){
                findedClassMeeting = classMeeting;
                break;
            }
        };

        return findedClassMeeting;
    }

    public void printTimetable(){
//        test method
        try {
            for (int i = 0; i < timetableClasses.size(); i++) {
                Log.d("Timetable", timetableClasses.get(i).getName());
                Log.d("Timetable", timetableClasses.get(i).getSubjectCode());
                Log.d("Timetable", timetableClasses.get(i).getPriority());
            }
        } catch(RuntimeException e){
            e.printStackTrace();
        }

    }
}
