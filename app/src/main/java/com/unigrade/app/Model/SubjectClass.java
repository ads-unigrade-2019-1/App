package com.unigrade.app.Model;

public class SubjectClass {
    private String codeLetter;
    private String teacher;
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
        isSelected = selected;
    }

    public SubjectClass(String codeLetter, String teacher, String campus, String schedules, String subjectCode){
        this.codeLetter = codeLetter;
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

    public String getCodeLetter() {
        return codeLetter;
    }

    public void setCodeLetter(String codeLetter) {
        this.codeLetter = codeLetter;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
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
