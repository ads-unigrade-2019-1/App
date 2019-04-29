package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.view.View;

import com.unigrade.app.Controller.ClassesController;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.View.Adapter.ClassListAdapter;
import com.unigrade.app.View.Fragment.ClassesFragment;

import java.util.ArrayList;

public class GetClasses extends AsyncTask<String, Integer, ArrayList<SubjectClass> > {

    private ClassesController classesController;
    private ClassesFragment classesFragment;

    public GetClasses(ClassesFragment classesFragment){
        this.classesFragment = classesFragment;
    }

    @Override
    protected void onPreExecute() {
        classesController =  ClassesController.getInstance();
        classesFragment.getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<SubjectClass> doInBackground(String... params) {
        return classesController.getSubjectsList();
    }

    @Override
    protected void onPostExecute(ArrayList<SubjectClass> classes) {

        classesFragment.setClasses(classes);
        classesFragment.getClassesList()
                .setAdapter(
                        new ClassListAdapter(classesFragment.getClasses(), classesFragment.getActivity())
                );
        classesFragment.getProgressBar().setVisibility(View.GONE);
    }
}
