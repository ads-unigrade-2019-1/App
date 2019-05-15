package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.View;

import com.unigrade.app.Controller.TimetablesController;
import com.unigrade.app.Model.Timetable;
import com.unigrade.app.View.Adapter.TimetableListAdapter;
import com.unigrade.app.View.Fragment.TimetablesFragment;

import java.util.ArrayList;

public class GetTimetables extends AsyncTask<String, ArrayList<Timetable>, ArrayList<Timetable> > {

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
    protected ArrayList<Timetable> doInBackground(String... params) {
        return timetablesController.getTimetablesList(timetablesFragment.getContext());
    }

    @Override
    protected void onPostExecute(ArrayList<Timetable> timetables) {
        timetablesFragment.setSubjects(timetables);
        timetablesFragment.getSubjectList()
                .setAdapter(
                        new TimetableListAdapter(timetables, timetablesFragment.getActivity())
                );
        timetablesFragment.getProgressBar().setVisibility(View.GONE);
    }
}