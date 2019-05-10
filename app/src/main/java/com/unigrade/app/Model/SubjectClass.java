package com.unigrade.app.Model;
import java.util.ArrayList;

public class SubjectClass {
    private String name;
    private ArrayList<String> teacher = new ArrayList<>();
    private String campus;
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

    public SubjectClass(String name, ArrayList<String> teacher, String campus, String schedules, String subjectCode){
        int campi = Integer.parseInt(campus);
        if (campi == 1) campus = "Darcy Ribeiro";
        else if (campi == 2) {
            campus = "Plantaltina";
        } else if (campi == 3) {
            campus = "Ceilândia";
        } else {
            campus = "Gama";
        }

        this.name = name;
        this.teacher = teacher;
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

    public ArrayList<String> getTeacher() {
        return teacher;
    }

    public void setTeacher(ArrayList<String> teacher) {
        this.teacher = teacher;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }
}
