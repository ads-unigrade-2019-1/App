package com.unigrade.app.Model;
import java.util.ArrayList;

public class SubjectClass {
    private String name;
    private ArrayList<String> teachers = new ArrayList<>();
    private int campus;
    private String schedules;
    private String subjectCode;
    private boolean isSelected;

    public SubjectClass(){
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public SubjectClass(String name, ArrayList<String> teachers, int campus, String schedules, String subjectCode){
        this.name = name;
        this.teachers = teachers;
        this.campus = campus;
        this.schedules = schedules;
        this.subjectCode = subjectCode;
    }

    public String[] getSchedules() {
        String[] parts = schedules.split("[|]|,");
        return parts;
    }

    public String getSchedulesString() {
        return schedules;
    }

    public void setSchedules(String schedules) {
        this.schedules = schedules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getTeachers() {
        return teachers;
    }

    public void setTeachers(ArrayList<String> teachers) {
        this.teachers = teachers;
    }

    public String getCampus() {
        if (campus == 1) {
            return "Darcy Ribeiro";
        } else if (campus == 2) {
            return "Planaltina";
        } else if (campus == 3) {
            return "Ceilândia";
        } else {
            return "Gama";
        }
    }

    public void setCampus(int campus) {
        this.campus = campus;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}
