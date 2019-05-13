package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.view.View;

import com.unigrade.app.Controller.SubjectsController;
import com.unigrade.app.DAO.SubjectDAO;
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
    protected  ArrayList<Subject> doInBackground(String... params) {

        boolean removeSubject;
        SubjectDAO subjectDAO = new SubjectDAO(userSubjectsFragment.getActivity());
        ArrayList<Subject> subjectsListDao = subjectDAO.all();
        try{
            ArrayList<Subject> verifyList = subjectsController.getSubjectsList();

            if(verifyList.isEmpty() == true) {
            }else{
                for (int i = 0; i < subjectsListDao.size(); i++) {
                    removeSubject = true;
                    for (int j = 0; j < verifyList.size(); j++) {
                        if (verifyList.get(j).getCode().contains(subjectsListDao.get(i).getCode())) {
                            subjectDAO.alter(verifyList.get(j));
                            subjectsListDao.set(i, verifyList.get(j));
                            removeSubject = false;
                            break;
                        }
                    }
                    if (removeSubject == true) {
                        subjectDAO.delete(subjectsListDao.get(i));
                        subjectsListDao.remove(i);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return subjectsListDao;
        //return subjectsController.getSubjectsList();
    }

    @Override
    protected void onPostExecute(ArrayList<Subject> subjects) {
        //((MainActivity) userSubjectsFragment.getActivity()).setSubjectsList(subjects);
        userSubjectsFragment.setSubjectsListDao(subjects);
        userSubjectsFragment.getSubjectList()
                .setAdapter(
                        new SubjectListAdapter(userSubjectsFragment.getSubjectsListDao(), userSubjectsFragment.getActivity())
                );
       // userSubjectsFragment.getProgressBar().setVisibility(View.GONE);
    }
}

