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
        String result = "{" +
                "\"timetables\":{" +
                    "\"timetable1\":{" +
                        "\"monday\":{" +
                            "\"8-10\": \"Cálculo\"" +
                            "\"10-12\": \"Apc\"" +
                            "\"12-14\": \"Diac\"" +
                            "\"14-16\": \"Desen Software\"" +
                            "\"16-18\": \"Compiladores\"" +
                        "}" +
                        "\"tuesday\":{" +
                            "\"8-10\": \"Cálculo\"" +
                            "\"10-12\": \"Apc\"" +
                            "\"12-14\": \"Diac\"" +
                            "\"14-16\": \"Desen Software\"" +
                            "\"16-18\": \"Compiladores\"" +
                        "}" +
                        "\"wednesday\":{" +
                            "\"8-10\": \"Cálculo\"" +
                            "\"10-12\": \"Apc\"" +
                            "\"12-14\": \"Diac\"" +
                            "\"14-16\": \"Desen Software\"" +
                            "\"16-18\": \"Compiladores\"" +
                        "}" +
                    "}" +
                    "\"timetable2\":{" +
                        "\"monday\":{" +
                            "\"8-10\": \"Cálculo\"" +
                            "\"10-12\": \"Apc\"" +
                            "\"12-14\": \"Diac\"" +
                            "\"14-16\": \"Desen Software\"" +
                            "\"16-18\": \"Compiladores\"" +
                        "}" +
                        "\"tuesday\":{" +
                            "\"8-10\": \"Cálculo\"" +
                            "\"10-12\": \"Apc\"" +
                            "\"12-14\": \"Diac\"" +
                            "\"14-16\": \"Desen Software\"" +
                            "\"16-18\": \"Compiladores\"" +
                        "}" +
                        "\"wednesday\":{" +
                            "\"8-10\": \"Cálculo\"" +
                            "\"10-12\": \"Apc\"" +
                            "\"12-14\": \"Diac\"" +
                            "\"14-16\": \"Desen Software\"" +
                            "\"16-18\": \"Compiladores\"" +
                        "}" +
                    "}" +
                "}";

        try {

            JSONArray timetablesJSON = new JSONArray(result);

            for (int i = 0; i < timetablesJSON.length(); i++) {
                JSONArray timetableJSON = timetablesJSON.getJSONArray(i);
                ArrayList<ArrayList<String>> timetableArray = new ArrayList<>();

                for (int j = 0; j < timetableJSON.length(); j++){
                    //De segunda a sábado
                    ArrayList<String> weekDay = new ArrayList<>();

                    JSONObject weekDayJSON =  timetableJSON.getJSONObject(j);

                    weekDay.add(weekDayJSON.getString("8-10"));
                    weekDay.add(weekDayJSON.getString("10-12"));
                    weekDay.add(weekDayJSON.getString("12-14"));
                    weekDay.add(weekDayJSON.getString("14-16"));
                    weekDay.add(weekDayJSON.getString("16-18"));

                    timetableArray.add(weekDay);
                }
                Timetable timetable = new Timetable(timetableArray);
                timetables.add(timetable);
            }

        } catch (
                JSONException e) {
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
