package com.unigrade.app.View.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.unigrade.app.Controller.FlowController;
import com.unigrade.app.Model.Course;
import com.unigrade.app.View.Activity.MainActivity;
import com.unigrade.app.R;
import com.unigrade.app.View.AsyncTask.GetCourses;

import androidx.navigation.Navigation;

import java.util.ArrayList;


public class CourseFragment extends Fragment {
    private Spinner spnCampus;
    private Spinner spnCourse;
    private AsyncTask getCourses;
    private ArrayAdapter<String> spinnerAdapter;

    public CourseFragment() {
        // Required empty public constructor
    }

    public Spinner getSpnCourse() {
        return spnCourse;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_course, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Escolha campus e curso");

        spnCampus = view.findViewById(R.id.campus_spinner);
        spnCourse = view.findViewById(R.id.course_spinner);
        spinnerAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCourse.setAdapter(spinnerAdapter);

        callServer();

        final Button button = view.findViewById(R.id.btn_continue);
        button.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.timetablesFragment, null)
        );

        spnCampus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = parent.getItemAtPosition(pos).toString();
                Log.d("CAMPUS", item);
                callServer();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private void callServer(){

        if(FlowController.getInstance().isConnectedToNetwork(getActivity())){
            String campus = spnCampus.getSelectedItem().toString();

            if(!campus.equals("Selecione")) {
                getCourses = new GetCourses(
                        this,
                        spnCampus.getSelectedItem().toString()
                ).execute();
            }
        }
    }

    public void setCourses(ArrayList<Course> courses){
        spinnerAdapter.clear();

        for(Course course: courses)
            spinnerAdapter.add(course.getName());

        spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(getCourses != null && getCourses.getStatus() != AsyncTask.Status.FINISHED) {
            getCourses.cancel(true);
        }
    }


}
