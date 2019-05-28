package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;
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
        this.subjectDB = subjectDB;
    }

    @Override
    protected void onPreExecute() {
        classesController =  ClassesController.getInstance();
        classesFragment.getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<SubjectClass> doInBackground(String... params) {
        ArrayList<SubjectClass> classes = classesController.getClassesList(subject);

        if(subjectDB.isSubjectOnDB(subject.getCode())) {
            for (SubjectClass sc: classes) {
                if(classDB.isClassOnDB(sc)) {
                    boolean a = classDB.getClass(sc.getName(), sc.getSubjectCode()).isSelected();
                    String b = Boolean.toString(a);
                    Log.i("DEBUG MASTER ", b);
                    sc.setSelected(classDB.getClass(sc.getName(), sc.getSubjectCode()).isSelected());
                    sc.setPriority(classDB.getClass(sc.getName(), sc.getSubjectCode()).getPriority());
                }
            }
        }

        return classes;
    }

    @Override
    protected void onPostExecute(ArrayList<SubjectClass> classes) {

        classesFragment.setClasses(classes);
        classesFragment.getClassesList()
                .setAdapter(
                        new ClassListAdapter(classesFragment.getClasses(), classesFragment.getActivity(), classesFragment.getSubject())
                );
        classesFragment.getProgressBar().setVisibility(View.GONE);
    }
}
