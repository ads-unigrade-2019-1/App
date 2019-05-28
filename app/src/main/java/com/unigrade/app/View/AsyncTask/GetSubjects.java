package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.view.View;

import com.unigrade.app.Controller.SubjectsController;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.View.Activity.MainActivity;
import com.unigrade.app.View.Adapter.SubjectListAdapter;
import com.unigrade.app.View.Fragment.SubjectsFragment;

import java.util.ArrayList;

public class GetSubjects extends AsyncTask<String, Integer, ArrayList<Subject> > {

    private SubjectsController subjectsController;
    private SubjectsFragment subjectsFragment;

    public GetSubjects(SubjectsFragment subjectsFragment){
        this.subjectsFragment = subjectsFragment;
    }

    @Override
    protected void onPreExecute() {
        subjectsController =  SubjectsController.getInstance();
        subjectsFragment.getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<Subject> doInBackground(String... params) {
        return subjectsController.getSubjectsList();
    }

    @Override
    protected void onPostExecute(ArrayList<Subject> subjects) {

        ((MainActivity) subjectsFragment.getActivity()).setSubjectsList(subjects);
        subjectsFragment.setSubjects(subjects);
        subjectsFragment.getSubjectList()
                .setAdapter(
                        new SubjectListAdapter(subjectsFragment.getSubjects(), subjectsFragment.getActivity())
        );
        subjectsFragment.getProgressBar().setVisibility(View.GONE);
    }
}