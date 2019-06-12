package com.unigrade.app.View.Fragment;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
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
    private Button button;
    private int position;
    private ArrayList<Course> courses;
    private ArrayAdapter<String> spinnerAdapter;
    private ProgressBar progressBar;

    public CourseFragment() {
        // Required empty public constructor
    }

    public Spinner getSpnCourse() {
        return spnCourse;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_course, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Escolha campus e curso");

        spnCampus = view.findViewById(R.id.campus_spinner);
        spnCourse = view.findViewById(R.id.course_spinner);
        button = view.findViewById(R.id.btn_continue);
        progressBar = view.findViewById(R.id.progress_bar);
        spinnerAdapter = new ArrayAdapter<>(
                getActivity(), android.R.layout.simple_spinner_item, android.R.id.text1
        );

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCourse.setAdapter(spinnerAdapter);
        button.setEnabled(false);
        button.setOnClickListener(continueListener(view));
        spnCampus.setOnItemSelectedListener(spnCampusListener());
        spnCourse.setOnItemSelectedListener(spnCourseListener());

        callServer();

        // Inflate the layout for this fragment
        return view;
    }

    private AdapterView.OnItemSelectedListener spnCampusListener(){
        return new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String item = parent.getItemAtPosition(pos).toString();
                Log.d("CAMPUS", item);

                callServer();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
    }

    private AdapterView.OnItemSelectedListener spnCourseListener(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                position = pos;
                button.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
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
        this.courses = courses;

        spinnerAdapter.clear();

        for(Course course: courses)
            spinnerAdapter.add(course.getName());

        spinnerAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener continueListener(final View view){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getActivity().getApplicationContext()
                        .getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = pref.edit();

                String courseCode = courses.get(position).getCode();

                editor.putBoolean("isCourseSelected", true);
                editor.putString("courseCode", courseCode);

                Log.d("CODIGOCURSO", courseCode);
                editor.apply();

                Navigation.findNavController(view).navigate(R.id.timetablesFragment);
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(getCourses != null && getCourses.getStatus() != AsyncTask.Status.FINISHED) {
            getCourses.cancel(true);
        }
    }


}
