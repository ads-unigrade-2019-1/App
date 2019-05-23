package com.unigrade.app.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TableLayout;

import com.unigrade.app.DAO.ClassDB;
import com.unigrade.app.DAO.ServerHelper;
import com.unigrade.app.Model.ClassMeeting;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.Model.Timetable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

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
        ClassDB classDB = ClassDB.getInstance(context);

//        String result = new GetDAO(URL_ALL_TIMETABLES).post(ArrayToJSON(classDAO.allSelecteds()).toString());
//        TODO
//         excluir essa chamada de função
//         descomentar o post
//         excluir string result feita à mão
        arrayToJSON(classDB.allSelecteds());

        String result = "" +
                "[" +
                "[" +
                "{" +
                "\"discipline\":\"128121\"," +
                "\"name\":\"A\"" +
                "}," +
                "{" +
                "\"discipline\":\"103691\"," +
                "\"name\":\"B\"" +
                "}," +
                "{" +
                "\"discipline\":\"129852\"," +
                "\"name\":\"A\"" +
                "}," +
                "{" +
                "\"discipline\":\"103691\"," +
                "\"name\":\"A\"" +
                "}" +
                "]," +
                "[" +
                "{" +
                "\"discipline\":\"128121\"," +
                "\"name\":\"A\"" +
                "}," +
                "{" +
                "\"discipline\":\"103691\"," +
                "\"name\":\"B\"" +
                "}," +
                "{" +
                "\"discipline\":\"129852\"," +
                "\"name\":\"A\"" +
                "}," +
                "{" +
                "\"discipline\":\"103691\"," +
                "\"name\":\"A\"" +
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
                    Log.d("timetable", "Prioridade: " + classDB.getClass(name, discipline).getPriority());
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
                JSONArray teachersJSON = new JSONArray();
                for(String teacher : subjectClass.getTeacher()){
                    teachersJSON.put(teacher);
                }

                JSONArray meetingsJSON = new JSONArray();
                for(ClassMeeting schedule : subjectClass.getSchedules()) {
                    JSONObject scheduleJSON = new JSONObject();

                    scheduleJSON.put("room", schedule.getRoom());
                    scheduleJSON.put("day", schedule.getDay());
                    scheduleJSON.put("init_hour", schedule.getInit_hour());
                    scheduleJSON.put("final_hour", schedule.getFinal_hour());
                    meetingsJSON.put(scheduleJSON);
                }

                subjectJSON.put("teachers", teachersJSON);
                subjectJSON.put("meetings", meetingsJSON);
                subjectJSON.put("name", subjectClass.getName());
                subjectJSON.put("discipline", subjectClass.getSubjectCode());
                subjectJSON.put("campus", subjectClass.getCampus());
                subjectJSON.put("priority", subjectClass.getPriority());

                subjectsJSON.put(subjectJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        Log.d("JSONString", subjectsJSON.toString());
        return subjectsJSON;
    }

    public boolean isDownloadPermitted(Context context){
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        int isPermitted = ContextCompat.checkSelfPermission(context, permission);

        return isPermitted == PackageManager.PERMISSION_GRANTED;
    }

    public boolean shouldShowExplanation(Activity activity){
        String permission = Manifest.permission.READ_CONTACTS;

        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    public void downloadTableLayout(TableLayout tableLayout, Context context){

        tableLayout.setDrawingCacheEnabled(true);
        Bitmap bitmap = tableLayout.getDrawingCache();

        String root = Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                .getAbsolutePath();
        File imageFolder = new File( root + File.separator + "unigrade");

        if(!imageFolder.exists())
            imageFolder.mkdirs();

        String path = imageFolder.getAbsolutePath();
        String date = Calendar.getInstance().getTime().toString();
        File file = new File( path + File.separator + "timetable" + date + ".png");
        Log.d("GRADE", file.getAbsolutePath());

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

            Uri contentUri = Uri.fromFile(file);

            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);

        }


    }

}