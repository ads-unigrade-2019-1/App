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

    public SubjectClass findClassesByTimeDay(final String[] initHour, Integer timeIndex,  String day){
        //Log.d("Horários", "ENTROU: " + timeIndex);
        int i=0;
        for(SubjectClass subjectClass : this.getTimetableClass()){
            for(ClassMeeting classMeeting : subjectClass.getSchedules()){
//                Log.d("Horários", "Compare: " +
//                        initHour[(timeIndex*4 + 3)] + "," +
//                        initHour[(timeIndex*4 + 2)] + "," +
//                        initHour[(timeIndex*4 + 1)] + "," +
//                        initHour[(timeIndex*4)] + "," +
//                        day);
//                Log.d("Horários", "BD: " + classMeeting.getInit_hour() + "," + classMeeting.getDay());

                String regex = ":([0-9]?[0-9])";
                int inHour = Integer.parseInt(initHour[timeIndex*4].replaceAll(regex,""));
                int finHour = Integer.parseInt(classMeeting.getFinal_hour().replaceAll(regex,""));

                if((classMeeting.getInit_hour().equals(initHour[(timeIndex*4 + 3)])
                    || classMeeting.getInit_hour().equals(initHour[timeIndex*4 + 2])
                    || classMeeting.getInit_hour().equals(initHour[timeIndex*4 + 1])
                    || classMeeting.getInit_hour().equals(initHour[timeIndex*4])
                    || finHour == inHour || finHour == inHour + 1)
                    && classMeeting.getDay().equals(day)){
//                    Log.d("Horários", "IGUAL");
                    return subjectClass;
                }
            };
        };
        return null;
    }

    public void printTimetable(){
//        test method
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
