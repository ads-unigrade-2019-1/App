package com.unigrade.app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.unigrade.app.Model.ClassMeeting;
import com.unigrade.app.Model.SubjectClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassDB {
    private String table = "classes";
    private DBHelper dbHelper;
    private Context context;

    private static ClassDB instance;

    public static ClassDB getInstance(Context context) {
        if(instance == null){
            instance = new ClassDB(context.getApplicationContext());
        }
        return instance;
    }

    public ClassDB(Context context) {
        this.context = context;
        dbHelper = DBHelper.getInstance(this.context);
    }

    public boolean insert(SubjectClass subjectClass){
        Log.d("timetable", "PrioridadeDBInsert: " + subjectClass.getPriority());
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = getClassAttribute(subjectClass);
            db.insert(table, null, values);

        } catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<SubjectClass> allSelecteds(){
        SQLiteDatabase db;
        Cursor cursor;
        try {
            db = dbHelper.getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
            return null;
        }

        ArrayList<SubjectClass> subjectsClass = new ArrayList<>();
        cursor = db.query(table, null, "added=?", new String[]{"true"}, null, null, null);

        while (cursor.moveToNext()){
            SubjectClass subjectClass = new SubjectClass();
            try {
                Log.d("timetable", "PrioridadeDB: " + cursor.getString(cursor.getColumnIndex("priority")));
                subjectClass.setCampus(cursor.getString(cursor.getColumnIndex("campus")));
                subjectClass.setName(cursor.getString(cursor.getColumnIndex("name")));
                subjectClass.setSubjectCode(cursor.getString(cursor.getColumnIndex("subjectCode")));
                subjectClass.setPriority(cursor.getString(cursor.getColumnIndex("priority")));

                String teachersString = cursor.getString(cursor.getColumnIndex("teacher"));
                String[] teachersArray = teachersString.split(";");
                ArrayList<String> teachers = new ArrayList<>(Arrays.asList(teachersArray));
                subjectClass.setTeacher(teachers);

                MeetingDB meetingDB = MeetingDB.getInstance(this.context);
                ArrayList<ClassMeeting> schedules = meetingDB.getClassMeetings(subjectClass.getName(), subjectClass.getSubjectCode());

                subjectClass.setSchedules(schedules);

            } catch (SQLiteException e){
                e.printStackTrace();
                return null;
            }
            subjectsClass.add(subjectClass);
        }
        cursor.close();
        return subjectsClass;
    }

    public boolean delete(SubjectClass subjectClass){
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] params = {subjectClass.getName(),
                    subjectClass.getSubjectCode()};
            db.delete(table, "name=? AND subjectCode=?", params);

            MeetingDB meetingDB = MeetingDB.getInstance(this.context);
            meetingDB.delete(subjectClass);
        } catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean alter(SubjectClass subjectClass) {
        try{
            MeetingDB meetingDB = MeetingDB.getInstance(this.context);
            for(ClassMeeting schedule : subjectClass.getSchedules()) {
                meetingDB.alter(subjectClass, schedule);
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = getClassAttribute(subjectClass);

            String[] params = {subjectClass.getName(), subjectClass.getSubjectCode()};

            db.update(table, values, "name=? AND subjectCode=?", params);
        } catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public SubjectClass getClass(String name, String subjectCode){
        SQLiteDatabase db;
        try {
            db = dbHelper.getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
            return null;
        }

        Cursor cursor = db.query(table, null, "subjectCode=? AND name=?", new String[]{subjectCode, name}, null, null, null);
        cursor.moveToFirst();

        SubjectClass subjectClass = new SubjectClass();
        try {
            subjectClass.setSubjectCode(cursor.getString(cursor.getColumnIndex("subjectCode")));
            subjectClass.setCampus(cursor.getString(cursor.getColumnIndex("campus")));
            subjectClass.setName(cursor.getString(cursor.getColumnIndex("name")));
            subjectClass.setPriority(cursor.getString(cursor.getColumnIndex("priority")));

            String teachersString = cursor.getString(cursor.getColumnIndex("teacher"));
            String[] teachersArray = teachersString.split(";");
            subjectClass.setTeacher(new ArrayList<>(Arrays.asList(teachersArray)));

            MeetingDB meetingDB = MeetingDB.getInstance(this.context);
            ArrayList<ClassMeeting> schedules = meetingDB.getClassMeetings(name, subjectCode);

            subjectClass.setSchedules(schedules);

        } catch (SQLiteException e){
            e.printStackTrace();
            return null;

        } catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        cursor.close();
        return subjectClass;
    }

    public ArrayList<SubjectClass> getSubjectClasses(String subjectCode){
        SQLiteDatabase db;
        Cursor cursor;
        try {
            db = dbHelper.getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
            return null;
        }

        ArrayList<SubjectClass> subjectsClass = new ArrayList<>();

        try{
            cursor = db.query(table, null, "subjectCode=?", new String[]{subjectCode}, null, null, null);
            while (cursor.moveToNext()){
                SubjectClass subjectClass = new SubjectClass();
                subjectClass.setCampus(cursor.getString(cursor.getColumnIndex("campus")));
                subjectClass.setName(cursor.getString(cursor.getColumnIndex("name")));

                String teachersString = cursor.getString(cursor.getColumnIndex("teacher"));
                String[] teachersArray = teachersString.split(";");
                ArrayList<String> teachers = new ArrayList<>();
                teachers.addAll(Arrays.asList(teachersArray));

                subjectClass.setTeacher(teachers);

                MeetingDB meetingDB = MeetingDB.getInstance(this.context);
                ArrayList<ClassMeeting> schedules = meetingDB.getClassMeetings(subjectClass.getName(), subjectCode);

                subjectClass.setSchedules(schedules);

                subjectClass.setSelected(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("added"))));

                subjectsClass.add(subjectClass);
            }
        } catch (SQLiteException e){
            e.printStackTrace();
            return null;
        }
        cursor.close();
        return subjectsClass;
    }

    public boolean isClassOnDB(SubjectClass sc) {

        String sql = String.format(
                "SELECT * FROM %s WHERE subjectCode=%s and name='%s'",
                table, sc.getSubjectCode(), sc.getName()
        );
        SQLiteDatabase db;

        try{
            db = dbHelper.getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;

    }

    public boolean insertClassesArray(ArrayList<SubjectClass> subjectClassesList){
        for(SubjectClass subjectClass : subjectClassesList){
            try {
                insert(subjectClass);
            } catch (SQLiteException e){
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private ContentValues getClassAttribute(SubjectClass subjectClass){
        ContentValues values = new ContentValues();

        values.put("name", subjectClass.getName());
        values.put("teacher", subjectClass.getTeacherString(';'));
        values.put("campus", subjectClass.getCampus());
        values.put("subjectCode", subjectClass.getSubjectCode());
        values.put("priority", subjectClass.getPriority());
        values.put("added", String.valueOf(subjectClass.isSelected()));

        MeetingDB meetingDB = MeetingDB.getInstance(this.context);
        for(ClassMeeting meeting : subjectClass.getSchedules()) {
            meetingDB.insert(meeting, subjectClass);
        }

        return values;
    }
}