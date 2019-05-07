package com.unigrade.app.DAO;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    DBHelper(Context context) {
        super(context, "Unigrade", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createSubjectTableSql = "CREATE TABLE subjects(" +
                    "code VARCHAR(255) NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "credits VARCHAR(255) NOT NULL)";
            db.execSQL(createSubjectTableSql);

            String createClassTableSql = "CREATE TABLE classes (" +
                    "codeLetter VARCHAR(255) NOT NULL, " +
                    "teacher VARCHAR(255) NOT NULL, " +
                    "campus VARCHAR(255) NOT NULL, " +
                    "subjectCode VARCHAR(255) NOT NULL, " +
                    "schedules VARCHAR(255) NOT NULL, " +
                    "added BOOLEAN NOT NULL DEFAULT 0, " +
                    "CONSTRAINT classes_pk PRIMARY KEY (codeLetter, subjectCode), " +
                    "CONSTRAINT classes_subject_FK FOREIGN KEY (subjectCode) " +
                    "REFERENCES subject(code))";
            db.execSQL(createClassTableSql);
        }catch(SQLiteException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            String sqlDropClassTable = "DROP TABLE IF EXISTS classes";
            db.execSQL(sqlDropClassTable);

            String sqlDropSubjectTable = "DROP TABLE IF EXISTS subjects";
            db.execSQL(sqlDropSubjectTable);

            onCreate(db);
        }catch(SQLiteException e){
            e.printStackTrace();
        }
    }
}