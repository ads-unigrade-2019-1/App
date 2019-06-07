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
import android.widget.TableRow;
import android.widget.TextView;

import com.unigrade.app.DAO.ClassDB;
import com.unigrade.app.DAO.ServerHelper;
import com.unigrade.app.DAO.SubjectDB;
import com.unigrade.app.Model.ClassMeeting;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.Model.Timetable;
import com.unigrade.app.View.Fragment.TimetablesFragment;

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

    synchronized public static TimetablesController getInstance() {
        if(instance == null){
            instance = new TimetablesController();
        }
        return instance;
    }

    public ArrayList<Timetable> getTimetablesList(Context context){
        // Returns the list of all subjects from the API

        String result = (new ServerHelper(URL_ALL_TIMETABLES)).post(arrayToJSON(ClassDB.getInstance(
                context).allSelected()).toString());
        Log.d("JSONString", arrayToJSON(ClassDB.getInstance(context).allSelected()).toString());
        Log.d("Timetable", "Resultado: " + result);
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
                    Log.d("timetable", "Prioridade: " + ClassDB.getInstance(
                            context).getClass(name, discipline).getPriority());
                    timetableClass.add(ClassDB.getInstance(context).getClass(name, discipline));
                }
                Timetable timetable = new Timetable(timetableClass);
                timetables.add(timetable);
                timetable.printTimetable();
            }

        } catch (NullPointerException e){
            //TODO Adicionar mensagem - Sem turmas adicionadas
        } catch (Exception e) {
            e.printStackTrace();

        }
        for(int i =0; i<timetables.size(); i++){
            Timetable timetable = timetables.get(i);
            Log.d("ShowTimetable", "TIMETABLE " + i);
            for (SubjectClass subjectClass : timetable.getTimetableClass()){
                Log.d("ShowTimetable", "---------------------");
                Log.d("ShowTimetable", "Nome: " + subjectClass.getName());
                Log.d("ShowTimetable", "Horário: " + subjectClass.getSchedulesString());
            }
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

        return subjectsJSON;
    }

    public boolean isDownloadPermitted(Context context){
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int isPermitted = ContextCompat.checkSelfPermission(context, permission);

        return isPermitted == PackageManager.PERMISSION_GRANTED;
    }

    public boolean shouldShowExplanation(Activity activity){
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    public String downloadTableLayout(TableLayout tableLayout, Context context){

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

            return "Download realizado em " + file.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return e.toString();
        } finally {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

            Uri contentUri = Uri.fromFile(file);

            mediaScanIntent.setData(contentUri);
            context.sendBroadcast(mediaScanIntent);

        }
    }

    public void insertTimetableInView(
            TableLayout timetableLayout, Timetable timetable, Context context, boolean isMinified){
        // processar
        // escrever
        timetable.printTimetable();

        String[] weekDays = {
                "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"
        };
        String[] initTimes = {
                "06:00", "06:30", "07:00", "07:30",
                "08:00", "08:30", "09:00", "09:30",
                "10:00", "10:30", "11:00", "11:30",
                "12:00", "12:30", "13:00", "13:30",
                "14:00", "14:30", "15:00", "15:30",
                "16:00", "16:30", "17:00", "17:30",
                "18:00", "18:30", "19:00", "19:30",
                "20:00", "20:30", "21:00", "21:30",
                "22:00", "22:30", "23:00", "23:30"
        };

        for (int i=0; i < initTimes.length/4; i++) {
            TableRow tr = (TableRow) timetableLayout.getChildAt(i+1);
            if (!isMinified){
                tr.setMinimumHeight(90);
            }
            for (int j=0; j < weekDays.length; j++){
                TextView classSchedule = (TextView) tr.getChildAt(j+1);

                SubjectClass subjectClass = timetable.findClassesByTimeDay(
                        initTimes,
                        i,
                        weekDays[j]
                );

                if(subjectClass != null){
                    Subject subject = (SubjectDB.getInstance(context)).getSubject(
                            subjectClass.getSubjectCode()
                    );
                    if(isMinified){
                        classSchedule.setText("*");
                    } else {
                        classSchedule.setText(
                                String.format(
                                        "%s\nTurma %s", subject.getName(), subjectClass.getName())
                        );
                        classSchedule.setTextSize(6);
                    }
                } else {
                    classSchedule.setText("");
                }
            }
        }
    }

    public boolean haveSubjects(Context context) {
        ArrayList<SubjectClass> subjects = ClassDB.getInstance(context).allSelected();
        if (subjects.size() == 0) {
            return false;
        } else {
            return true;
        }
    }



}