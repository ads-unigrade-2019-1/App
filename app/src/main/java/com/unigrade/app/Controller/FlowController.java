package com.unigrade.app.Controller;
import android.util.Log;

import com.unigrade.app.DAO.ServerHelper;
import com.unigrade.app.Model.Course;
import com.unigrade.app.Model.Period;
import com.unigrade.app.Model.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

import static com.unigrade.app.DAO.URLs.URL_CAMPUS_COURSES;
import static com.unigrade.app.DAO.URLs.URL_COURSE_FLOW;

public class FlowController extends Controller {
    private static FlowController instance;

    private FlowController(){
        //Empty constructor
    }

    synchronized public static FlowController getInstance() {
        if(instance == null){
            instance = new FlowController();
        }
        return instance;
    }

    public ArrayList<Course> getCourses(String campus){
        String result = new ServerHelper(getCampusURL(campus)).get();
        ArrayList<Course> courses = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(result);
            String name;
            String code;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                name = c.getString("name");
                code = c.getString("code");

                Course course = new Course(code, name);
                courses.add(course);
            }

        } catch (
                JSONException e) {
            e.printStackTrace();
        }

        return courses;
    }

    private String getCampusURL(String campus) {
        return URL_CAMPUS_COURSES + campusToInt(campus);
    }

    private int campusToInt(String campus){
        if(campus.equals("Gama"))
            return 4;
        else if(campus.equals("Planaltina"))
            return 2;
        else if(campus.equals("Darcy Ribeiro"))
            return 1;
        else
            return 3;
    }

    private ArrayList<Period> getFlow(String courseCode){

        String result = (new ServerHelper(courseFlowURL(courseCode))).get();
        ArrayList<Period> flow = new ArrayList<>();

        try {
            JSONArray flowJSON = new JSONArray(result);

            for(int i = 0; i < flowJSON.length(); i++){
                JSONArray periodJSON = flowJSON.getJSONArray(i);
                ArrayList<Subject> subjects = new ArrayList<>();

                for(int j = 0; j < periodJSON.length(); j++){
                    JSONArray subjectJSON = periodJSON.getJSONArray(j);

                    String name = subjectJSON.getJSONObject(1).toString();
                    String code = subjectJSON.getJSONObject(0).toString();

                    Log.d("PERÍODO -> MATÉRIA", (i + 1) + " -> " + name);

                    subjects.add(new Subject(code, name));

                }
                Period period = new Period(i + 1, subjects);
                flow.add(period);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return flow;
    }

    private String courseFlowURL(String code){
        return URL_CAMPUS_COURSES + code;
    }

}
