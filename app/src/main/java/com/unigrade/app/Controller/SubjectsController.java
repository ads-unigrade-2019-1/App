package com.unigrade.app.Controller;

import com.unigrade.app.DAO.ServerHelper;
import com.unigrade.app.Model.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.unigrade.app.DAO.URLs.URL_ALL_SUBJECTS;

public class SubjectsController extends Controller {

    private static SubjectsController instance;

    private SubjectsController(){
        //Empty constructor
    }

    public static SubjectsController getInstance() {
        if(instance == null){
            instance = new SubjectsController();
        }
        return instance;
    }

    public ArrayList<Subject> getSubjectsList(){
        // Returns the list of all subjects from the API

        String result = new ServerHelper(URL_ALL_SUBJECTS).get();
        ArrayList<Subject> subjects = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(result);
            String name;
            String code;
            String credits;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                name = c.getString("name");
                code = c.getString("code");
                credits = c.getString("credits");

                Subject subject = new Subject(code, name, credits);
                subjects.add(subject);
            }

        } catch (
            JSONException e) {
            e.printStackTrace();
        }
           
        return subjects;
    }

}
