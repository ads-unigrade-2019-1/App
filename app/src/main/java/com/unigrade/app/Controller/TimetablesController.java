package com.unigrade.app.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.unigrade.app.DAO.ClassDAO;
import com.unigrade.app.DAO.GetDAO;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.Model.Timetable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.unigrade.app.DAO.URLs.URL_ALL_TIMETABLES;

public class TimetablesController {
    private static TimetablesController instance;

    private TimetablesController(){
        //Empty constructor
    }

    public static TimetablesController getInstance() {
        if(instance == null){
            instance = new TimetablesController();
        }
        return instance;
    }

    public ArrayList<Timetable> getTimetablesList(Context context){
        // Returns the list of all subjects from the API
        //String result = new GetDAO(URL_ALL_TIMETABLES).post("test");

        ArrayList<Timetable> timetables = new ArrayList<>();

        String result = "" +
        "[" +
            "[" +
                "{" +
                    "\"discipline\":\"120642\"," +
                    "\"name\":\"L\"" +
                "}," +
                "{" +
                    "\"discipline\":\"120642\"," +
                    "\"name\":\"B\"" +
                "}," +
                "{" +
                    "\"discipline\":\"113040\"," +
                    "\"name\":\"C\"" +
                "}," +
                "{" +
                    "\"discipline\":\"208213\"," +
                    "\"name\":\"L\"" +
                "}" +
            "]," +
            "[" +
                "{" +
                    "\"discipline\":\"120642\"," +
                    "\"name\":\"L\"" +
                "}," +
                "{" +
                    "\"discipline\":\"120642\"," +
                    "\"name\":\"B\"" +
                "}," +
                "{" +
                    "\"discipline\":\"113040\"," +
                    "\"name\":\"C\"" +
                "}," +
                "{" +
                    "\"discipline\":\"208213\"," +
                    "\"name\":\"L\"" +
                "}" +
            "]" +
        "]";

        try {
            JSONArray timetablesJSON = new JSONArray(result);

            for(int i = 0; i < timetablesJSON.length(); i++){
                JSONArray timetableJSON = timetablesJSON.getJSONArray(i);
                ArrayList<SubjectClass> timetableClass = new ArrayList<>();

                for(int j = 0; j < timetableJSON.length(); j++){
                    JSONObject classJSON = timetableJSON.getJSONObject(j);
                    ClassDAO classDAO = new ClassDAO(context);

                    String name = classJSON.getString("name");
                    String discipline = classJSON.getString("discipline");

                    timetableClass.add(classDAO.getClass(name, discipline));
                }
                Timetable timetable = new Timetable(timetableClass);
                timetables.add(timetable);
                timetable.printTimetable();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return timetables;
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
