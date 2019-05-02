package com.unigrade.app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.unigrade.app.Model.SubjectClass;

import java.util.ArrayList;
import java.util.List;

public class ClassDAO {
    private String table = "classes";
    private DAO dbHelper;

    public ClassDAO(Context context) {
        dbHelper = new DAO(context);
    }

    public void insert(SubjectClass subjectClass){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = getClassAttribute(subjectClass);
        db.insert(table, null, values);
    }

    public ArrayList<SubjectClass> all(){
        String sql = String.format("SELECT * from %s", table);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<SubjectClass> subjectsClass = new ArrayList<>();

        while (cursor.moveToNext()){
            SubjectClass subjectClass = new SubjectClass();
            subjectClass.setCampus(cursor.getString(cursor.getColumnIndex("campus")));
            subjectClass.setCodeLetter(cursor.getString(cursor.getColumnIndex("codeLetter")));
            subjectClass.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
            subjectClass.setSchedules(cursor.getString(cursor.getColumnIndex("schedules")));

            subjectsClass.add(subjectClass);
        }
        cursor.close();
        return subjectsClass;
    }

    public void delete(SubjectClass subjectClass){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] params = {subjectClass.getCodeLetter(),
                           subjectClass.getSubjectCode()};
        db.delete(table, "codeLetter = ? AND subjectCode = ?", params);
    }

    public void alter(SubjectClass subjectClass) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = getClassAttribute(subjectClass);

        String[] params = {subjectClass.getCodeLetter(), subjectClass.getSubjectCode()};

        db.update(table, values, "codeLetter = ? AND subjectCode = ?", params);
    }
    
    public SubjectClass getClass(String codeLetter, String subjectCode){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(table, null, "subjectCode=? AND codeLetter=?", new String[]{subjectCode, codeLetter}, null, null, null);
        cursor.moveToFirst();

        SubjectClass subjectClass = new SubjectClass();
        subjectClass.setCampus(cursor.getString(cursor.getColumnIndex("campus")));
        subjectClass.setCodeLetter(cursor.getString(cursor.getColumnIndex("codeLetter")));
        subjectClass.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
        subjectClass.setSchedules(cursor.getString(cursor.getColumnIndex("schedules")));

        cursor.close();

        return subjectClass;
    }

    public ArrayList<SubjectClass> getSubjectClasses(String subjectCode){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(table, null, "subjectCode=?", new String[]{subjectCode}, null, null, null);

        ArrayList<SubjectClass> subjectsClass = new ArrayList<>();

        while (cursor.moveToNext()){
            SubjectClass subjectClass = new SubjectClass();
            subjectClass.setCampus(cursor.getString(cursor.getColumnIndex("campus")));
            subjectClass.setCodeLetter(cursor.getString(cursor.getColumnIndex("codeLetter")));
            subjectClass.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
            subjectClass.setSchedules(cursor.getString(cursor.getColumnIndex("schedules")));

            subjectsClass.add(subjectClass);
        }
        cursor.close();

        return subjectsClass;
    }

    public void insertClassesArray(ArrayList<SubjectClass> subjectClassesList){
        for(SubjectClass subjectClass : subjectClassesList){
            insert(subjectClass);
        }
    }

    private ContentValues getClassAttribute(SubjectClass subjectClass){
        ContentValues values = new ContentValues();

        values.put("codeLetter", subjectClass.getCodeLetter());
        values.put("teacher", subjectClass.getTeacher());
        values.put("campus", subjectClass.getCampus());
        values.put("subjectCode", subjectClass.getSubjectCode());
        values.put("schedules", subjectClass.getSchedulesString());
        values.put("added", subjectClass.isSelected());

        Log.d("ClassDAO ", "get(): " + values.toString());

        return values;
    }
}
