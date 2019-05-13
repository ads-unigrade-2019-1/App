package com.unigrade.app.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.unigrade.app.DAO.ClassDB;
import com.unigrade.app.DAO.ServerHelper;
import com.unigrade.app.Model.ClassMeeting;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.Model.Timetable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.unigrade.app.DAO.URLs.URL_ALL_TIMETABLES;

public class TimetablesController extends Controller{
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
        ClassDB classDB = new ClassDB(context);

//        String result = new GetDAO(URL_ALL_TIMETABLES).post(ArrayToJSON(classDAO.all()).toString());
//        TODO
//         excluir essa chamada de função
//         descomentar o post
//         excluir string result feita à mão
        arrayToJSON(classDB.allSelecteds());

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

        ArrayList<Timetable> timetables = new ArrayList<>();

        try {
            JSONArray timetablesJSON = new JSONArray(result);

            for(int i = 0; i < timetablesJSON.length(); i++){
                JSONArray timetableJSON = timetablesJSON.getJSONArray(i);
                ArrayList<SubjectClass> timetableClass = new ArrayList<>();

                for(int j = 0; j < timetableJSON.length(); j++){
                    JSONObject classJSON = timetableJSON.getJSONObject(j);

                    String name = classJSON.getString("name");
                    String discipline = classJSON.getString("discipline");

                    timetableClass.add(classDB.getClass(name, discipline));
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

    private JSONArray arrayToJSON(ArrayList<SubjectClass> subjectClasses){
        JSONArray subjectsJSON = new JSONArray();
        for(SubjectClass subjectClass : subjectClasses){
            JSONObject subjectJSON = new JSONObject();
            try {

//              TODO criar JSONArray para os professores
//              JSONArray teachersJSON = new JSONArray()
//              for(String teacher : subjectClass.getTeachers())
//                teachersJSON.put(teacher);
//              timetableJSON.put("teachers", teachersJSON);
                subjectJSON.put("teachers", subjectClass.getTeacher());

//             TODO criar JSONArray para os horários a partir do obj schedule
                JSONArray meetingsJSON = new JSONArray();
                for(ClassMeeting schedule : subjectClass.getSchedules()) {
//                   JSONObject scheduleJSON = new JSONObject()
//
//                   scheduleJSON.put("room", schedule.getRoom());
//                   scheduleJSON.put("day", schedule.getDay());
//                   scheduleJSON.put("init_hour", schedule.getInitHour());
//                   scheduleJSON.put("final_hour", schedule.getFinalHour());
                    meetingsJSON.put(schedule);
                }
                subjectJSON.put("meetings", meetingsJSON);
                subjectJSON.put("name", subjectClass.getName());
                subjectJSON.put("discipline", subjectClass.getSubjectCode());
                subjectJSON.put("campus", subjectClass.getCampus());

                subjectsJSON.put(subjectJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("JSONString", subjectsJSON.toString());
        return subjectsJSON;
    }

}
