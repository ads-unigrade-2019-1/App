package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import com.unigrade.app.Controller.FlowController;
import com.unigrade.app.Model.Course;
import com.unigrade.app.View.Fragment.CourseFragment;

import java.util.ArrayList;

public class GetCourses extends AsyncTask<String, Integer, ArrayList<Course> > {

    private CourseFragment courseFragment;
    private String campus;

    public GetCourses(CourseFragment courseFragment, String campus){
        this.courseFragment = courseFragment;
        this.campus = campus;
    }

    @Override
    protected void onPreExecute() {
        courseFragment.getSpnCourse().setEnabled(false);
    }

    @Override
    protected ArrayList<Course> doInBackground(String... params) {
        return FlowController.getInstance().getCourses(campus);
    }

    @Override
    protected void onPostExecute(ArrayList<Course> courses) {
        courseFragment.setCourses(courses);
        courseFragment.getSpnCourse().setEnabled(true);
        //subjectsFragment.setSubjects(subjects);
        //subjectsFragment.getSubjectList()
                //.setAdapter(
                //        new SubjectListAdapter(subjectsFragment.getSubjects(),
                //                subjectsFragment.getActivity())
                //);
        //subjectsFragment.getSubjectList().setVisibility(View.VISIBLE);
        //subjectsFragment.getProgressBar().setVisibility(View.GONE);
    }
}