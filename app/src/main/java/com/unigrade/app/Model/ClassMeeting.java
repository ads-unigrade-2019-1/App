package com.unigrade.app.Model;

public class ClassMeeting {
    private String day;
    private String init_hour;
    private String final_hour;
    private String room;

    public ClassMeeting() {

    }

    public ClassMeeting(String day, String init_hour, String final_hour, String room) {
        this.day = day;
        this.init_hour = init_hour;
        this.final_hour = final_hour;
        this.room = room;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getInit_hour() {
        return init_hour;
    }

    public void setInit_hour(String init_hour) {
        this.init_hour = init_hour;
    }

    public String getFinal_hour() {
        return final_hour;
    }

    public void setFinal_hour(String final_hour) {
        this.final_hour = final_hour;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String formattedClassMeeting() {
        return day + " " + init_hour + "h - " + final_hour + "h " + room;
    }
}
