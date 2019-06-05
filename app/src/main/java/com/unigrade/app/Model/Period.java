package com.unigrade.app.Model;

import java.util.ArrayList;

public class Period {
    private int number;
    private ArrayList<Subject> subjects;

    public Period(int number, ArrayList<Subject> subjects) {
        this.number = number;
        this.subjects = subjects;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }
}
