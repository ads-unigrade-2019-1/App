package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.view.View;

import com.unigrade.app.Controller.SubjectsController;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.View.Activity.MainActivity;
import com.unigrade.app.View.Adapter.SubjectListAdapter;
import com.unigrade.app.View.Fragment.UserSubjectsFragment;

import java.util.ArrayList;


public class RefreshUserSubjectsFragment extends AsyncTask<String, Integer, ArrayList<Subject> > {

    private SubjectsController subjectsController;
    private UserSubjectsFragment userSubjectsFragment;

    public RefreshUserSubjectsFragment(UserSubjectsFragment userSubjectsFragment){
        this.userSubjectsFragment = userSubjectsFragment;
    }

    @Override
    protected void onPreExecute() {
        subjectsController =  SubjectsController.getInstance();
        //userSubjectsFragment.getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<Subject> doInBackground(String... params) {
        return subjectsController.getSubjectsList();
    }

    @Override
    protected void onPostExecute(ArrayList<Subject> subjects) {

        //((MainActivity) userSubjectsFragment.getActivity()).setSubjectsList(subjects);
        userSubjectsFragment.setSubjects(subjects);
        //userSubjectsFragment.getSubjectList()
          //      .setAdapter(
            //            new SubjectListAdapter(userSubjectsFragment.getSubjects(), userSubjectsFragment.getActivity())
              //  );
        //userSubjectsFragment.getProgressBar().setVisibility(View.GONE);
    }
}

