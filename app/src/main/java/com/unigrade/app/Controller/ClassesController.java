package com.unigrade.app.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
            String subjectCode;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                codeLetter = c.getString("class");
                professor = c.getString("teacher");
                campus = c.getString("campus");
                schedules = c.getString("time");
                subjectCode = "123456";

                SubjectClass subjectClass = new SubjectClass(
                        codeLetter,
                        professor,
                        campus,
                        schedules,
                        subjectCode
                );
                classes.add(subjectClass);
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        return classes;
    }

    public boolean isConnectedToNetwork(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean isConnected = false;
        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            isConnected = (activeNetwork != null) && (activeNetwork.isConnectedOrConnecting());
        }

        return isConnected;
    }

}
