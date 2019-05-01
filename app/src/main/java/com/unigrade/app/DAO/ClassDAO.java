package com.unigrade.app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unigrade.app.Model.SubjectClass;

import java.util.ArrayList;
import java.util.List;

public class ClassDAO extends SQLiteOpenHelper {
    private String table = "classes";

    public ClassDAO(Context context, int version) {
        super(context, "Unigrade", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s(" +
                "codeLetter VARCHAR(255) NOT NULL, " +
                "teacher VARCHAR(255) NOT NULL, " +
                "campus VARCHAR(255) NOT NULL," +
                "subjectCode VARCHAR(255) NOT NULL" +
                "schedules VARCHAR(255) NOT NULL" +
                "CONSTRAINT PRIMARY KEY (codeLetter, subjectCode)" +
                "CONSTRAINT FOREIGN KEY (subjectCode) " +
                "REFERENCES subject(code)" +
                ")", table);
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = String.format("DROP TABLE IF EXISTS %s", table);
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert(SubjectClass subjectClass){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getClassAttribute(subjectClass);

        db.insert(table, null, values);
    }

    public List<SubjectClass> all(){
        String sql = String.format("SELECT * from %s", table);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        List<SubjectClass> subjectsClass = new ArrayList<>();

        while (cursor.moveToNext()){
            SubjectClass subjectClass = new SubjectClass();
            subjectClass.setCampus(cursor.getString(cursor.getColumnIndex("campus")));
            subjectClass.setCodeLetter(cursor.getString(cursor.getColumnIndex("codeLetter")));
            subjectClass.setTeacher(cursor.getString(cursor.getColumnIndex("teacher")));
            subjectClass.setSchedules(cursor.getString(cursor.getColumnIndex("schedules")));
        }
        cursor.close();
        return subjectsClass;
    }

    public void delete(SubjectClass subjectClass){
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {subjectClass.getCodeLetter(),
                           subjectClass.getSubjectCode()};
        db.delete(table, "codeLetter = ? AND subjectCode = ?", params);
    }

    public void alter(SubjectClass subjectClass) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getClassAttribute(subjectClass);

        String[] params = {subjectClass.getCodeLetter(),
                subjectClass.getSubjectCode()};

        db.update(table, values, "codeLetter = ? AND subjectCode = ?", params);
    }

    private ContentValues getClassAttribute(SubjectClass subjectClass){
        ContentValues values = new ContentValues();

        values.put("codeLetter", subjectClass.getCodeLetter());
        values.put("teacher", subjectClass.getTeacher());
        values.put("campus", subjectClass.getCampus());
        values.put("subjectCode", subjectClass.getSubjectCode());
        values.put("schedules", subjectClass.getSchedulesString());

        return values;
    }
}
