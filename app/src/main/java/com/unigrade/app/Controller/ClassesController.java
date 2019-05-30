package com.unigrade.app.Controller;

import android.content.Context;
import android.util.Log;

import com.unigrade.app.DAO.ClassDB;
import com.unigrade.app.DAO.ServerHelper;
import com.unigrade.app.DAO.SubjectDB;
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

    synchronized public static ClassesController getInstance() {
        if(instance == null){
            instance = new ClassesController();
        }
        return instance;
    }

    private String getClassURL(Subject subject) {
        return URL_SUBJECT_CLASSES + subject.getCode();
    }

    public ArrayList<SubjectClass> getClassesList(Subject subject) {
        // Returns the list of all subject classes from the API

        String result = new ServerHelper (getClassURL(subject)).get();
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

    public void insertIntoDatabase(
        SubjectClass subjectClass, Context context,
            Subject subject, ArrayList<SubjectClass> classes){

        String subjectCode = subjectClass.getSubjectCode();

        subjectClass.setSelected(true);

        if (!SubjectDB.getInstance(context).isSubjectOnDB(subjectCode)){
            SubjectDB.getInstance(context).insert(subject);
            for (SubjectClass c : classes) {
                if(c.getPriority() == null)
                    c.setPriority("1");
                Log.d("timetable", "PrioridadeAdapter: " + c.getPriority());
                ClassDB.getInstance(context).insert(c);
            }
            Log.i("OUTSIDEDB", subjectCode + " "+ subjectClass.getTeacher());
        } else {
            ClassDB.getInstance(context).alter(subjectClass);
            Log.i("ONDB", subjectCode + " "+ subjectClass.getTeacher());
        }
    }

    public void removeFromDatabase(
            SubjectClass subjectClass, Context context, ArrayList<SubjectClass> classes){

        String subjectCode = subjectClass.getSubjectCode();

        subjectClass.setSelected(false);

        if (isLonelyAdded(subjectClass, classes)){
            for (SubjectClass c : classes)
                ClassDB.getInstance(context).delete(c);
            SubjectDB.getInstance(context).delete(subjectCode);
            Log.i("LONELY", subjectCode + " "+ subjectClass.getTeacher());
        } else {
            ClassDB.getInstance(context).alter(subjectClass);
            Log.i("NOTLONELY", subjectCode + " "+ subjectClass.getTeacher());
        }
    }

    private boolean isLonelyAdded(SubjectClass subjectClass, ArrayList<SubjectClass> classes){
        for (SubjectClass sc : classes)
            if (sc.isSelected() && sc != subjectClass)
                return false;

        return true;
    }

}
