package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;

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

    private boolean loading;
    private ProgressBar progressBar;

    public GetSubjects(SubjectsFragment subjectsFragment, ArrayAdapter<Subject> adapter, boolean loading, ProgressBar progressBar){
        this.loading = loading;
        this.subjectsFragment = subjectsFragment;
        this.adapter = adapter;
        this.progressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        subjectsController =  subjectsController.getInstance();

        if(loading){
            progressBar.setVisibility(View.VISIBLE);
        }
        super.onPreExecute();
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

    @Override
    protected void onPostExecute(List list) {

        if(loading){
            progressBar.setVisibility(View.GONE);
        }

        super.onPostExecute(list);
    }
}