package com.unigrade.app.View.AsyncTask;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.unigrade.app.Controller.SubjectsParser;
import com.unigrade.app.Model.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetSubjects extends AsyncTask<String, Integer, List> {

    private SubjectsController subjectsController;
    private SubjectsFragment subjectsFragment;
    private ArrayAdapter<String> adapter;

    public GetSubjects(SubjectsFragment subjectsFragment, ArrayAdapter<String> adapter){
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
    protected void onProgressUpdate(ArrayList<UserClass> subjects) {
        subjectsFragment.setSubjects(subjects);
        adapter.notifyDataSetChanged(); 
    }

}