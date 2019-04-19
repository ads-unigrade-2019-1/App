package com.unigrade.app.Controller;

import com.unigrade.app.Model.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubjectsParser {

    public List sParser(String json) {
        try {

            JSONArray jsonArray = new JSONArray(json);
            JSONObject adress = null;
            String name;

            ArrayList<Subject> subjects = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                name = c.getString("name");

                adress = c.getJSONObject("address");
                String code = adress.getString("zipcode");

                Subject subject = new Subject(code,name);
                subjects.add(subject);
            }
            return subjects;
        } catch (
                JSONException e) {
            e.printStackTrace();
        }
        return null;
    }



}
