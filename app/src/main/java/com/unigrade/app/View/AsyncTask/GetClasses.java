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

    private ClassesFragment classesFragment;
    private Subject subject;

    public GetClasses(ClassesFragment classesFragment, Subject subject){
        this.classesFragment = classesFragment;
        this.subject = subject;
    }

    @Override
    protected void onPreExecute() {
        classesFragment.getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<SubjectClass> doInBackground(String... params) {
        ArrayList<SubjectClass> classes = ClassesController.getInstance().getClassesList(subject);

        if(SubjectDB.getInstance(classesFragment.getContext()).isSubjectOnDB(subject.getCode())) {
            for (SubjectClass sc: classes) {
                if(ClassDB.getInstance(classesFragment.getContext()).isClassOnDB(sc)) {
                    sc.setSelected(ClassDB.getInstance(
                            classesFragment.getContext()).getClass(sc.getName(),
                            sc.getSubjectCode()).isSelected());
                    sc.setPriority(ClassDB.getInstance(
                            classesFragment.getContext()).getClass(sc.getName(),
                            sc.getSubjectCode()).getPriority());
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
                        new ClassListAdapter(classesFragment.getClasses(),
                                classesFragment.getActivity(), classesFragment.getSubject())
                );
        classesFragment.getProgressBar().setVisibility(View.GONE);
    }
}
