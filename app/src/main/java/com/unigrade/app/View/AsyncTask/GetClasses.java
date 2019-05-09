package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.view.View;

import com.unigrade.app.Controller.ClassesController;
import com.unigrade.app.DAO.ClassDB;
import com.unigrade.app.DAO.SubjectDB;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.View.Adapter.ClassListAdapter;
import com.unigrade.app.View.Fragment.ClassesFragment;

import java.util.ArrayList;

public class GetClasses extends AsyncTask<String, Integer, ArrayList<SubjectClass> > {

    private ClassesController classesController;
    private ClassesFragment classesFragment;
    private ClassDB classDB;
    private SubjectDB subjectDB;
    private Subject subject;

    public GetClasses(ClassesFragment classesFragment, ClassDB classDB, SubjectDB subjectDB, Subject subject){
        this.classesFragment = classesFragment;
        this.classDB = classDB;
        this.subject = subject;
    }

    @Override
    protected void onPreExecute() {
        classesController =  ClassesController.getInstance();
        classesFragment.getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<SubjectClass> doInBackground(String... params) {
        ArrayList<SubjectClass> classes = classesController.getClassesList(subject);

//        if(subjectDB.isSubjectOnDB(classes.get(0).getSubjectCode()))
//            for (SubjectClass sc: classes)
//                if(classDB.isClassOnDB(sc))
//                    sc.setSelected(true);

        return classes;
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
