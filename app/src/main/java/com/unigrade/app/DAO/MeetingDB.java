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

    public void insert(ClassMeeting classMeeting, SubjectClass subjectClass){
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = getMeetingAttribute(classMeeting, subjectClass);
            db.insert(table, null, values);
        } catch (SQLiteException e){
            e.printStackTrace();
        }
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
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
        }

        ArrayList<ClassMeeting> classMeetings = new ArrayList<>();

        String select = String.format("SELECT * FROM %s", table);
        String where = String.format(
                "WHERE subjectCode='%s' AND className='%s'", subjectCode, className);
        String case1 = "WHEN 'Domingo' THEN 1 WHEN 'Segunda' THEN 2 WHEN 'Terça' THEN 3";
        String case2 = "WHEN 'Quarta' THEN 4 WHEN 'Quinta' THEN 5 WHEN 'Sexta' THEN 6";
        String case3 = "WHEN 'Sábado' THEN 7 ELSE 100 END";
        String cases = case1 + " " + case2 + " " + case3;
        String orderBy = "ORDER BY (CASE day" + " "+ cases + ") ASC, initHour ASC";
        String selectQuery = select + " " + where + " " + orderBy;

        try{
            cursor = db.rawQuery(selectQuery, null);
            while (cursor.moveToNext()){
                ClassMeeting classMeeting = new ClassMeeting();
                classMeeting.setDay(cursor.getString(cursor.getColumnIndex("day")));
                classMeeting.setInit_hour(cursor.getString(cursor.getColumnIndex(
                        "initHour")));
                classMeeting.setFinal_hour(cursor.getString(cursor.getColumnIndex(
                        "finalHour")));
                classMeeting.setRoom(cursor.getString(cursor.getColumnIndex("room")));

                classMeetings.add(classMeeting);
            }
        } catch (SQLiteException e){
            e.printStackTrace();
        }
        cursor.close();
        return classMeetings;
    }

    public void delete(SubjectClass subjectClass){
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] params = {subjectClass.getName(),
                    subjectClass.getSubjectCode()};
            db.delete(table, "className=? AND subjectCode=?", params);
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }

    public void alter(SubjectClass subjectClass, ClassMeeting classMeeting) {
        try{
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = getMeetingAttribute(classMeeting, subjectClass);

            String[] params = {subjectClass.getSubjectCode(), subjectClass.getName(),
                    classMeeting.getDay(), classMeeting.getInit_hour()};
            db.update(table, values,
                    "subjectCode=? AND className=? AND day=? AND initHour=?", params);
        } catch (SQLiteException e){
            e.printStackTrace();
        }
    }
}
