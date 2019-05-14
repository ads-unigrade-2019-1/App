package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.util.Log;
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
    }

    @Override
    protected void onPreExecute() {
        classesController =  ClassesController.getInstance();
        //classesFragment.getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<SubjectClass> doInBackground(String... params) {

        ClassDAO classDAO = new ClassDAO(classesFragment.getActivity());
       // Log.i("CALLDATABASE", "New adapter added");
        ArrayList<SubjectClass> classesListDao = classDAO.getSubjectClasses(
                classesFragment.getSubject().getCode());

        try{
            ArrayList<SubjectClass> verifyList = classesController.getSubjectsList();

            if(verifyList.isEmpty() == true) {
            }else{
                for (int i = 0; i < classesListDao.size(); i++) {
                    if (verifyList.get(i) != null) {
                        classDAO.alter(verifyList.get(i));
                        if (classesListDao.get(i).isSelected() == true){
                            classDAO.getSubjectClasses(classesFragment.getSubject().getCode())
                                    .get(i).setSelected(true);
                            classesListDao.set(i, verifyList.get(i));
                            classesListDao.get(i).setSelected(true);
                        }
                        classesListDao.set(i, verifyList.get(i));
                    }else{
                        classDAO.delete(classesListDao.get(i));
                        classesListDao.remove(i);
                    }
                }
                if (verifyList.size() > classesListDao.size()){
                    for (int i = classesListDao.size(); i < verifyList.size(); i++){
                        classDAO.insert(verifyList.get(i));
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
    }

    @Override
    protected void onPostExecute(ArrayList<SubjectClass> classes) {

        classesFragment.setClassesListDao(classes);
        classesFragment.getClassesList()
                .setAdapter(
                        new ClassListAdapter(classesFragment.getClassesListDao(), classesFragment.getActivity())
                );
    }
}
