package com.unigrade.app.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.unigrade.app.DAO.GetDAO;
import com.unigrade.app.Model.Timetable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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

    public ArrayList<Timetable> getTimetablesList(){
        // Returns the list of all subjects from the API
        //String result = new GetDAO(URL_ALL_TIMETABLES).post("test");
        ArrayList<Timetable> timetables = new ArrayList<>();
        String result = "[" +
            "[" +
                "{" +
                    "\"campus\":\"Gama\"," +
                    "\"class\":\"L\"," +
                    "\"teacher\":\"Rafael\"," +
                    "\"day\":[\"Seg\", \"Qua\", \"Seg\"]," +
                    "\"time\":[\"10h\", \"10h\", \"12h\"]" +
                "}," +
                "{" +
                    "\"campus\":\"Gama\"," +
                    "\"class\":\"B\"," +
                    "\"teacher\":\"Geovana\"," +
                    "\"day\":[\"Seg\", \"Sex\"]," +
                    "\"time\":[\"8h\", \"8h\"]" +
                "}," +
                "{" +
                    "\"campus\":\"Gama\"," +
                    "\"class\":\"C\"," +
                    "\"teacher\":\"Gabriela\"," +
                    "\"day\":[\"Ter\", \"Qui\"]," +
                    "\"time\":[\"14h\", \"14h\"]" +
                "}," +
                "{" +
                    "\"campus\":\"Gama\"," +
                    "\"class\":\"D\"," +
                    "\"teacher\":\"Guilherme\"," +
                    "\"day\":[\"Ter\", \"Qui\"]," +
                    "\"time\":[\"16h\", \"16h\"]" +
                "}" +
            "]" +
        "]";

        try {
            JSONArray timetablesJSON = new JSONArray(result);

            for (int i = 0; i < timetablesJSON.length(); i++) {
                JSONArray timetableJSON = timetablesJSON.getJSONArray(i);
                HashMap<String, ArrayList<String>> week = new HashMap<>();

                for(int j = 0; j < timetableJSON.length(); j++){
                    JSONObject subjectClass = timetableJSON.getJSONObject(j);
                    JSONArray daysJSON = subjectClass.getJSONArray("day");
                    JSONArray startTimeJSON = subjectClass.getJSONArray("time");

                    for (int k = 0; k < daysJSON.length(); k++){
                        if (week.containsKey(daysJSON.getString(k))){
                            week.get(daysJSON.getString(k)).add(startTimeJSON.getString(k));
                        }
                        else {
                            ArrayList<String> newTime = new ArrayList<>();
                            newTime.add(startTimeJSON.getString(k));
                            week.put(daysJSON.getString(k), newTime);
                        }
                    }
                }
                Timetable timetable = new Timetable(week);
                timetable.printTimetable();
                timetables.add(timetable);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
//
//        ArrayList<ArrayList<String>> week = new ArrayList<>();
//        ArrayList<String> day = new ArrayList<>();
//        day.add("Test");
//        day.add("Test2");
//        week.add(day);
//
//        ArrayList<Timetable> timetables = new ArrayList<>();
//        timetables.add(new Timetable(week));
//        timetables.add(new Timetable(week));
//        timetables.add(new Timetable(week));
//        timetables.add(new Timetable(week));
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
