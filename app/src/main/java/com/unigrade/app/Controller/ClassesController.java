package com.unigrade.app.Controller;

import com.unigrade.app.DAO.ServerHelper;
import com.unigrade.app.Model.ClassMeeting;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.Model.SubjectClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.unigrade.app.DAO.URLs.URL_SUBJECT_CLASSES;

public class ClassesController extends Controller {

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

        String result = new ServerHelper(getClassURL(subject)).get();
        ArrayList<SubjectClass> classes = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(result);
            String name;
            ArrayList<String> teacher;
            String campus;
            ArrayList<ClassMeeting> schedules;
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

                schedules = new ArrayList<>();
                String day;
                String init_hour;
                String final_hour;
                String room;
                JSONArray schedulesArray = c.getJSONArray("meetings");
                for (int z = 0; z < schedulesArray.length(); z++) {
                    JSONObject d = schedulesArray.getJSONObject(z);
                    day = d.getString("day");
                    init_hour = d.getString("init_hour");
                    final_hour = d.getString("final_hour");
                    room = d.getString("room");
                    ClassMeeting classMeeting = new ClassMeeting(day, init_hour, final_hour, room);
                    schedules.add(classMeeting);
                }

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

}
