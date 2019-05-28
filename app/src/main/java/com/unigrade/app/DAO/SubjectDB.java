package com.unigrade.app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.unigrade.app.Model.Subject;

import java.util.ArrayList;

public class SubjectDB {
    private String table = "subjects";
    private DBHelper dbHelper;

    private static SubjectDB instance;

    public static SubjectDB getInstance(Context context) {
        if(instance == null){
            instance = new SubjectDB(context);
        }
        return instance;
    }

    public SubjectDB(Context context){
        dbHelper = DBHelper.getInstance(context);
    }

    public boolean insert(Subject subject){
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = getSubjectAttributes(subject);
            db.insert(table, null, values);
        } catch(SQLiteException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public ArrayList<Subject> all(){
        String sql = String.format("SELECT * FROM %s", table);
        SQLiteDatabase db;

        try{
            db = dbHelper.getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
            return null;
        }

        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<Subject> subjects = new ArrayList<>();
        while (cursor.moveToNext()){
            Subject subject = new Subject();
            try{
                subject.setCode(cursor.getString(cursor.getColumnIndex("code")));
                subject.setName(cursor.getString(cursor.getColumnIndex("name")));
                subject.setCredits(cursor.getString(cursor.getColumnIndex("credits")));
            } catch (SQLiteException e){
                e.printStackTrace();
                return null;
            }
            subjects.add(subject);
        }

        cursor.close();
        return subjects;
    }

    public boolean delete(String subjectCode){
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            String[] params = {subjectCode};
            db.delete(table, "code=?", params);
        } catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean alter(Subject subject) {
        try {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = getSubjectAttributes(subject);

            String[] params = {subject.getCode()};

            db.update(table, values, "code=?", params);
        } catch (SQLiteException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean isSubjectOnDB(String code) {

        String sql = String.format("SELECT * FROM %s WHERE code=%s", table, code);
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

    public Subject getSubject(String code){
        SQLiteDatabase db;
        try {
            db = dbHelper.getReadableDatabase();
        } catch (SQLiteException e){
            e.printStackTrace();
            return null;
        }

        Cursor cursor = db.query(table, null, "code=?",
                new String[]{code}, null, null, null);
        cursor.moveToFirst();

        Subject subject = new Subject();
        try {
            subject.setCode(cursor.getString(cursor.getColumnIndex("code")));
            subject.setCredits(cursor.getString(cursor.getColumnIndex("credits")));
            subject.setName(cursor.getString(cursor.getColumnIndex("name")));
        } catch (SQLiteException e){
            e.printStackTrace();
            return null;
        }

        cursor.close();
        return subject;
    }

    private ContentValues getSubjectAttributes(Subject subject){
        ContentValues values = new ContentValues();

        values.put("code", subject.getCode());
        values.put("name", subject.getName());
        values.put("credits", subject.getCredits());
        return values;
    }


}
