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

public class ClassDB {
    private final String table = "classes";
    private final Context context;

    private static ClassDB instance;

    synchronized public static ClassDB getInstance(Context context) {
        if(instance == null){
            instance = new ClassDB(context.getApplicationContext());
        }
        return instance;
    }

    private ClassDB(Context context) {
        this.context = context;
    }

    public void insert(SubjectClass subjectClass){
        Log.d("timetable", "PrioridadeDBInsert: " + subjectClass.getPriority());
        try {
            SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
            for(ClassMeeting meeting : subjectClass.getSchedules()) {
                MeetingDB.getInstance(context).insert(meeting, subjectClass);
            }
            ContentValues values = getClassAttribute(subjectClass);
            db.insert(table, null, values);

        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public ArrayList<SubjectClass> allSelected(){
        SQLiteDatabase db = null;
        Cursor cursor;
        try {
            db = DBHelper.getInstance(context).getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
        }

        ArrayList<SubjectClass> subjectsClass = new ArrayList<>();
        cursor = db.query(table, null, "added=?", new String[]{"true"},
                null, null, null);

        while (cursor.moveToNext()){
            SubjectClass subjectClass = new SubjectClass();
            try {
                Log.d("timetable", "PrioridadeDB: " + cursor.getString(
                        cursor.getColumnIndex("priority")));
                subjectClass.setCampus(cursor.getString(cursor.getColumnIndex(
                        "campus")));
                subjectClass.setName(cursor.getString(cursor.getColumnIndex(
                        "name")));
                subjectClass.setSubjectCode(cursor.getString(cursor.getColumnIndex(
                        "subjectCode")));
                subjectClass.setPriority(cursor.getString(cursor.getColumnIndex(
                        "priority")));
                subjectClass.setSelected(Boolean.parseBoolean(cursor.getString(
                        cursor.getColumnIndex("added"))));

                String teachersString = cursor.getString(cursor.getColumnIndex(
                        "teacher"));
                String[] teachersArray = teachersString.split(";");
                ArrayList<String> teachers = new ArrayList<>(Arrays.asList(teachersArray));
                subjectClass.setTeacher(teachers);

                ArrayList<ClassMeeting> schedules = MeetingDB.getInstance(context).getClassMeetings(
                        subjectClass.getName(), subjectClass.getSubjectCode());
                subjectClass.setSchedules(schedules);

            } catch (SQLiteException e){
                e.printStackTrace();
            }
            subjectsClass.add(subjectClass);
        }
        cursor.close();
        return subjectsClass;
    }

    public void delete(SubjectClass subjectClass){
        try{
            SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
            String[] params = {subjectClass.getName(),
                    subjectClass.getSubjectCode()};
            db.delete(table, "name=? AND subjectCode=?", params);

            MeetingDB.getInstance(context).delete(subjectClass);
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public void alter(SubjectClass subjectClass) {
        try{

            for(ClassMeeting schedule : subjectClass.getSchedules()) {
                MeetingDB.getInstance(context).alter(subjectClass, schedule);
            }

            SQLiteDatabase db = DBHelper.getInstance(context).getWritableDatabase();
            ContentValues values = getClassAttribute(subjectClass);

            String[] params = {subjectClass.getName(), subjectClass.getSubjectCode()};

            db.update(table, values, "name=? AND subjectCode=?", params);
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public SubjectClass getClass(String name, String subjectCode){
        SQLiteDatabase db = null;
        try {
            db = DBHelper.getInstance(context).getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
        }

        Cursor cursor = db.query(table, null,
                "subjectCode=? AND name=?", new String[]{subjectCode, name},
                null, null, null);
        cursor.moveToFirst();

        SubjectClass subjectClass = new SubjectClass();
        try {
            subjectClass.setSubjectCode(cursor.getString(cursor.getColumnIndex(
                    "subjectCode")));
            subjectClass.setCampus(cursor.getString(cursor.getColumnIndex("campus")));
            subjectClass.setName(cursor.getString(cursor.getColumnIndex("name")));
            subjectClass.setPriority(cursor.getString(cursor.getColumnIndex(
                    "priority")));
            subjectClass.setSelected(Boolean.parseBoolean(cursor.getString(
                    cursor.getColumnIndex("added"))));

            String teachersString = cursor.getString(cursor.getColumnIndex("teacher"));
            String[] teachersArray = teachersString.split(";");
            subjectClass.setTeacher(new ArrayList<>(Arrays.asList(teachersArray)));

            ArrayList<ClassMeeting> schedules = MeetingDB.getInstance(
                    context).getClassMeetings(name, subjectCode);
            subjectClass.setSchedules(schedules);

        } catch (SQLiteException e){
            e.printStackTrace();

        } catch (CursorIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        cursor.close();
        return subjectClass;
    }

    public ArrayList<SubjectClass> getSubjectClasses(String subjectCode){
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = DBHelper.getInstance(context).getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
        }

        ArrayList<SubjectClass> subjectsClass = new ArrayList<>();

        try{
            cursor = db.query(table, null, "subjectCode=?",
                    new String[]{subjectCode}, null, null, null);
            while (cursor.moveToNext()){
                SubjectClass subjectClass = new SubjectClass();
                subjectClass.setCampus(cursor.getString(cursor.getColumnIndex(
                        "campus")));
                subjectClass.setName(cursor.getString(cursor.getColumnIndex("name")));
                subjectClass.setPriority(cursor.getString(cursor.getColumnIndex(
                        "priority")));
                subjectClass.setSubjectCode(cursor.getString(cursor.getColumnIndex(
                        "subjectCode")));

                String teachersString = cursor.getString(cursor.getColumnIndex(
                        "teacher"));
                String[] teachersArray = teachersString.split(";");
                ArrayList<String> teachers = new ArrayList<>();
                teachers.addAll(Arrays.asList(teachersArray));
                subjectClass.setTeacher(teachers);

                ArrayList<ClassMeeting> schedules = MeetingDB.getInstance(context).getClassMeetings(
                        subjectClass.getName(), subjectCode);
                subjectClass.setSchedules(schedules);

                subjectClass.setSelected(Boolean.parseBoolean(cursor.getString(
                        cursor.getColumnIndex("added"))));
                subjectClass.setSubjectCode(subjectCode);

                subjectsClass.add(subjectClass);

            }
        } catch (SQLiteException e){
            e.printStackTrace();
        }
        cursor.close();
        return subjectsClass;
    }

    public boolean isClassOnDB(SubjectClass sc) {

        String sql = String.format(
                "SELECT * FROM %s WHERE subjectCode='%s' and name='%s'",
                table, sc.getSubjectCode(), sc.getName()
        );
        SQLiteDatabase db = null;

        try{
            db = DBHelper.getInstance(context).getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
        }

        Cursor cursor = db.rawQuery(sql, null);

        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
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

        return values;
    }
}