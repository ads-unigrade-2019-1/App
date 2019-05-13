package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.view.View;

import com.unigrade.app.Controller.ClassesController;
import com.unigrade.app.DAO.ClassDAO;
import com.unigrade.app.DAO.SubjectDAO;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.View.Adapter.ClassListAdapter;
import com.unigrade.app.View.Fragment.ClassesFragment;

import java.util.ArrayList;

public class RefreshClassesFragment extends AsyncTask<String, Integer, ArrayList<SubjectClass> > {
    private ClassesController classesController;
    private ClassesFragment classesFragment;

    public RefreshClassesFragment(ClassesFragment classesFragment){
        this.classesFragment = classesFragment;
        //this.classDAO = classDAO;
    }

    @Override
    protected void onPreExecute() {
        classesController =  ClassesController.getInstance();
        //classesFragment.getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<SubjectClass> doInBackground(String... params) {
        ArrayList<SubjectClass> classes = classesController.getSubjectsList();

        boolean removeSubject;
        ClassDAO classDAO = new ClassDAO(ClassesFragment.getActivity());
        ArrayList<SubjectClass> classesListDao = classDAO.all();
        try{
            ArrayList<SubjectClass> verifyList = classesController.getSubjectsList();

            if(verifyList.isEmpty() == true) {
            }else{
                for (int i = 0; i < classesListDao.size(); i++) {
                    removeSubject = true;
                    for (int j = 0; j < verifyList.size(); j++) {
                        if (verifyList.get(j).getCode().contains(classesListDao.get(i).getCode())) {
                            classDAO.alter(verifyList.get(j));
                            classesListDao.set(i, verifyList.get(j));
                            removeSubject = false;
                            break;
                        }
                    }
                    if (removeSubject == true) {
                        classDAO.delete(classesListDao.get(i));
                        classesListDao.remove(i);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return classesListDao;

//        if(subjectDAO.isSubjectOnDB(classes.get(0).getSubjectCode()))
//            for (SubjectClass sc: classes)
//                if(classDAO.isClassOnDB(sc))
//                    sc.setSelected(true);

        //return classes;
    }

    @Override
    protected void onPostExecute(ArrayList<SubjectClass> classes) {

        classesFragment.setClasses(classes);
        classesFragment.getClassesList()
                .setAdapter(
                        new ClassListAdapter(classesFragment.getClasses(), classesFragment.getActivity())
                );
        //classesFragment.getProgressBar().setVisibility(View.GONE);
    }
}
}
