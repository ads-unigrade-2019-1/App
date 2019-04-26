package com.unigrade.app.Model;

public class SubjectClass {
    private String codeLetter;
    private String professor;
    private String campus;
    private String schedules;

    public SubjectClass(String codeLetter, String professor, String campus, String schedules){
        this.codeLetter = codeLetter;
        this.professor = professor;
        this.campus = campus;
        this.schedules = schedules;
    }

    public String[] getSchedules() {
        String[] parts = schedules.split("[|]|,");
        return parts;
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

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

}
