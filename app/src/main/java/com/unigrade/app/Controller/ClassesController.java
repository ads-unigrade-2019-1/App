package com.unigrade.app.Controller;

import com.unigrade.app.DAO.GetDAO;
import com.unigrade.app.Model.SubjectClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.unigrade.app.DAO.URLs.URL_SUBJECT_CLASSES;

public class ClassesController {

    private static ClassesController instance;

    public ClassesController(){
        //Empty constructor
    }

    public static ClassesController getInstance() {
        if(instance == null){
            instance = new ClassesController();
        }
        return instance;
    }

    public ArrayList<SubjectClass> getSubjectsList(){
        // Returns the list of all subject classes from the API

        String result = new GetDAO(URL_SUBJECT_CLASSES).get();
        ArrayList<SubjectClass> classes = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(result);
            String codeLetter;
            String professor;
            String campus;
            String schedules;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                codeLetter = c.getString("class");
                professor = c.getString("teacher");
                campus = c.getString("campus");
                schedules = c.getString("time");

                SubjectClass subjectClass = new SubjectClass(
                        codeLetter,
                        professor,
                        campus,
                        schedules
                );
                classes.add(subjectClass);
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        return classes;
    }

}
