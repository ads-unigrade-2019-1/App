package com.unigrade.app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.unigrade.app.Model.ClassMeeting;
import com.unigrade.app.Model.SubjectClass;

import java.util.ArrayList;

public class MeetingDB {
    private String table = "meetings";
    private DBHelper dbHelper;

    private static MeetingDB instance;

    public static MeetingDB getInstance(Context context) {
        if(instance == null){
            instance = new MeetingDB(context);
        }
        return instance;
    }

    public MeetingDB(Context context) {
        dbHelper = DBHelper.getInstance(context);
    }

    public boolean insert(ClassMeeting classMeeting, SubjectClass subjectClass){
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = getMeetingAttribute(classMeeting, subjectClass);
            db.insert(table, null, values);
        } catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private ContentValues getMeetingAttribute(ClassMeeting classMeeting, SubjectClass subjectClass){
        ContentValues values = new ContentValues();

        values.put("day", classMeeting.getDay());

        values.put("initHour", classMeeting.getInit_hour());

        values.put("finalHour", classMeeting.getFinal_hour());

        values.put("room", classMeeting.getRoom());

        values.put("className", subjectClass.getName());

        values.put("subjectCode", subjectClass.getSubjectCode());

        Log.d("MeetingDB ", "get(): " + values.toString());

        return values;
    }

    public ArrayList<ClassMeeting> getClassMeetings(String className, String subjectCode){
        SQLiteDatabase db;
        Cursor cursor;
        try {
            db = dbHelper.getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
            return null;
        }

        ArrayList<ClassMeeting> classMeetings = new ArrayList<>();

        try{
            String[] params = {className, subjectCode};
            cursor = db.query(table, null, "className=? AND subjectCode=?", params, null, null, null);
            while (cursor.moveToNext()){
                ClassMeeting classMeeting = new ClassMeeting();
                classMeeting.setDay(cursor.getString(cursor.getColumnIndex("day")));
                classMeeting.setInit_hour(cursor.getString(cursor.getColumnIndex("initHour")));
                classMeeting.setFinal_hour(cursor.getString(cursor.getColumnIndex("finalHour")));
                classMeeting.setRoom(cursor.getString(cursor.getColumnIndex("room")));

                classMeetings.add(classMeeting);
            }
        } catch (SQLiteException e){
            e.printStackTrace();
            return null;
        }
        cursor.close();
        return classMeetings;
    }

    public boolean delete(SubjectClass subjectClass){
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] params = {subjectClass.getName(),
                    subjectClass.getSubjectCode()};
            db.delete(table, "className = ? AND subjectCode = ?", params);
        } catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean alter(SubjectClass subjectClass, ClassMeeting classMeeting) {
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = getMeetingAttribute(classMeeting, subjectClass);

            String[] params = {subjectClass.getName(), subjectClass.getSubjectCode()};

            db.update(table, values, "name = ? AND subjectCode = ?", params);
        } catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
