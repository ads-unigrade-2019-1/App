package com.unigrade.app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unigrade.app.Model.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectDAO {
    private String table = "subjects";
    private DAO dbHelper;

    public SubjectDAO(Context context){
        dbHelper = new DAO(context);
    }

    public void insert(Subject subject){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = getSubjectAttributes(subject);
        db.insert(table, null, values);
    }

    public ArrayList<Subject> all(){
        String sql = String.format("SELECT * from %s", table);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<Subject> subjects = new ArrayList<>();
        while (cursor.moveToNext()){
            Subject subject = new Subject();
            subject.setCode(cursor.getString(cursor.getColumnIndex("code")));
            subject.setName(cursor.getString(cursor.getColumnIndex("name")));
            subject.setCredits(cursor.getString(cursor.getColumnIndex("credits")));

            subjects.add(subject);
        }

        cursor.close();
        return subjects;
    }

    public void delete(Subject subject){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String[] params = {subject.getCode()};
        db.delete(table, "code = ?", params);
    }

    public void alter(Subject subject) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = getSubjectAttributes(subject);

        String[] params = {subject.getCode()};

        db.update(table, values, "code = ?", params);
    }

    private ContentValues getSubjectAttributes(Subject subject){
        ContentValues values = new ContentValues();

        values.put("code", subject.getCode());
        values.put("name", subject.getName());
        values.put("credits", subject.getCredits());
        return values;
    }


}
