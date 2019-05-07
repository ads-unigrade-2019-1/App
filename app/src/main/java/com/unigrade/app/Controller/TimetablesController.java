package com.unigrade.app.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.unigrade.app.Model.Subject;
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

    public ArrayList<Subject> getTimetablesList(){
        // Returns the list of all subjects from the API

        String result = new PostDAO(URL_ALL_TIMETABLES).get();
        ArrayList<Timetable> timetables = new ArrayList<>();

        try {

            JSONArray jsonArray = new JSONArray(result);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                //name = c.getString("name");


                Timetable timetable = new Timetable();
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
