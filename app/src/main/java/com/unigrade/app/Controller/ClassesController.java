package com.unigrade.app.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.unigrade.app.DAO.Server;
import com.unigrade.app.DAO.URLs;
import com.unigrade.app.Model.Subject;
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

    private String getClassURL(Subject subject) {
        String classURL = URL_SUBJECT_CLASSES + subject.getCode();

        return classURL;
    }

    public ArrayList<SubjectClass> getClassesList(Subject subject) {
        // Returns the list of all subject classes from the API

        String result = new Server(getClassURL(subject)).get();
        ArrayList<SubjectClass> classes = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(result);
            String name;
            ArrayList<String> teacher;
            String campus;
            String schedules;
            String subjectCode;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                name = c.getString("name");

                teacher = new ArrayList<>();
                JSONArray teacherArray = c.getJSONArray("teachers");
                for (int j = 0; j < teacherArray.length(); j++) {
                    teacher.add(teacherArray.getString(j));
                }

                campus = c.getString("campus");
                schedules = c.getString("meetings");
                subjectCode = c.getString("discipline");

                SubjectClass subjectClass = new SubjectClass(
                        name,
                        teacher,
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
