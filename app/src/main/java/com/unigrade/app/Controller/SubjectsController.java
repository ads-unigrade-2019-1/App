package com.unigrade.app.Controller;

import com.unigrade.app.View.AsyncTask.GetSubjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.unigrade.app.DAO.URLs.URL_ALL_SUBJECTS;

public class SubjectsController {

    private static SubjectsController instance;

    public SubjectsController(){
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

        String result = new GetDao("https://jsonplaceholder.typicode.com/users").get();
        ArrayList<Subject> subjects = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(result);
            JSONObject adress = null;
            String name;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                name = c.getString("name");

                adress = c.getJSONObject("address");
                String code = adress.getString("zipcode");

                Subject subject = new Subject(code,name);
                subjects.add(subject);
            }

        } catch (
            JSONException e) {
            e.printStackTrace();
        }
           
        return subjects;
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
