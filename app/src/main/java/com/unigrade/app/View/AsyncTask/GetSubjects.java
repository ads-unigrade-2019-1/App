package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;

import com.unigrade.app.Controller.SubjectsController;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.View.Fragment.SubjectsFragment;

import java.util.ArrayList;
import java.util.List;

public class GetSubjects extends AsyncTask<String, Integer, List> {

    private SubjectsController subjectsController;
    private SubjectsFragment subjectsFragment;
    private ArrayAdapter<Subject> adapter;
    private ArrayList<Subject> subjects;

    public GetSubjects(SubjectsFragment subjectsFragment, ArrayAdapter<Subject> adapter){
        this.subjectsFragment = subjectsFragment;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        subjectsController =  subjectsController.getInstance();
    }

    @Override
    protected List doInBackground(String... params) {
        return subjectsController.getSubjectsList();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        subjectsFragment.setSubjects(subjects);
        adapter.notifyDataSetChanged();
    }

}