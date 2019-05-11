package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.view.View;

import com.unigrade.app.Controller.ClassesController;
import com.unigrade.app.DAO.ClassDAO;
import com.unigrade.app.DAO.SubjectDAO;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.View.Adapter.ClassListAdapter;
import com.unigrade.app.View.Fragment.ClassesFragment;

import java.util.ArrayList;

public class GetClasses extends AsyncTask<String, Integer, ArrayList<SubjectClass> > {

    private ClassesController classesController;
    private ClassesFragment classesFragment;
    private ClassDAO classDAO;
    private SubjectDAO subjectDAO;

    public GetClasses(ClassesFragment classesFragment, ClassDAO classDAO, SubjectDAO subjectDAO){
        this.classesFragment = classesFragment;
        this.classDAO = classDAO;
    }

    @Override
    protected void onPreExecute() {
        classesController =  ClassesController.getInstance();
        classesFragment.getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<SubjectClass> doInBackground(String... params) {
        ArrayList<SubjectClass> classes = classesController.getSubjectsList();

//        if(subjectDAO.isSubjectOnDB(classes.get(0).getSubjectCode()))
//            for (SubjectClass sc: classes)
//                if(classDAO.isClassOnDB(sc))
//                    sc.setSelected(true);

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
