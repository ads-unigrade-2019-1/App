package com.unigrade.app.Controller;

import com.unigrade.app.View.AsyncTask.GetSubjects;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.unigrade.app.DAO.URLs.URL_ALL_SUBJECTS;

public class SubjectsController {

    public List<String> getSubjects(){
        List<String> subjects = new ArrayList<>();
        GetSubjects getRequest = new GetSubjects();

        try {
            subjects = getRequest.execute(URL_ALL_SUBJECTS).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return subjects;
    }
}
