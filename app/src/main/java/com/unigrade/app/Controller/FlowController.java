package com.unigrade.app.Controller;
import android.content.Context;
import android.util.Log;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

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
        switch (campus) {
            case "Gama":
                return 4;
            case "Planaltina":
                return 2;
            case "Darcy Ribeiro":
                return 1;
            case "Ceilândia":
                return 3;
            default:
                return 0;
        }

    }

    public ArrayList<Period> getFlow(String courseCode){

        String result = (new ServerHelper(courseFlowURL(courseCode))).get();
        ArrayList<Period> flow = new ArrayList<>();

        try {
            JSONArray resultJSON = new JSONArray(result);
            JSONObject disciplines = resultJSON.getJSONObject(0);
            JSONArray flowJSON = disciplines.getJSONArray("disciplines");
            Log.d("FLOWJSON", flowJSON.toString());

            for(int i = 0; i < flowJSON.length(); i++){
                JSONArray periodJSON = flowJSON.getJSONArray(i);
                ArrayList<Subject> subjects = new ArrayList<>();

                for(int j = 0; j < periodJSON.length(); j++){
                    JSONArray subjectJSON = periodJSON.getJSONArray(j);

                    String name = subjectJSON.getString(1);
                    String code = subjectJSON.getString(0);

                    //Log.d("PERÍODO -> MATÉRIA", (i + 1) + " -> " + name);

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
        return URL_COURSE_FLOW + code;
    }

}
