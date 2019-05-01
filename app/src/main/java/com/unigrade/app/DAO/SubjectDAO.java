package com.unigrade.app.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.unigrade.app.Model.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectDAO extends SQLiteOpenHelper {
    private String table = "subjects";

    public SubjectDAO(Context context, int version) {
        super(context, "Unigrade", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE %s(" +
                "code VARCHAR(255) NOT NULL PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "credits VARCHAR(255) NOT NULL)", table);
        db.execSQL(sql);
        insert(new Subject("123456", "Algotitmo e Programação de Comp", "002-003-004-005"));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + table;
        db.execSQL(sql);
        onCreate(db);
    }

    public void insert(Subject subject){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = getSubjectAttributes(subject);

        db.insert(table, null, values);
    }

    public List<Subject> all(){
        String sql = String.format("SELECT * from %s", table);
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        List<Subject> subjects = new ArrayList<>();
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
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {subject.getCode()};
        db.delete(table, "code = ?", params);
    }

    public void alter(Subject subject) {
        SQLiteDatabase db = getWritableDatabase();
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
