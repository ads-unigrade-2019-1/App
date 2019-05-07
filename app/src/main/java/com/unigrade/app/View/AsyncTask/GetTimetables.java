package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.view.View;

import com.unigrade.app.Controller.TimetablesController;
import com.unigrade.app.View.Fragment.TimetablesFragment;

public class GetTimetables extends AsyncTask<String, Integer, Integer > {

    private TimetablesController timetablesController;
    private TimetablesFragment timetablesFragment;

    public GetTimetables(TimetablesFragment timetablesFragment){
        this.timetablesFragment = timetablesFragment;
    }

    @Override
    protected void onPreExecute() {
        timetablesController =  TimetablesController.getInstance();
        timetablesFragment.getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected Integer doInBackground(String... params) {
        return 0;
    }

    @Override
    protected void onPostExecute(Integer it) {

        timetablesFragment.getProgressBar().setVisibility(View.GONE);
    }
}