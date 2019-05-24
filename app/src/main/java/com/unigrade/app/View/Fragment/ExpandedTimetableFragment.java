package com.unigrade.app.View.Fragment;


import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.unigrade.app.DAO.SubjectDB;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.Model.Timetable;
import com.unigrade.app.R;
import com.unigrade.app.View.Activity.MainActivity;

public class ExpandedTimetableFragment extends Fragment {

    public ExpandedTimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.fragment_expanded_timetable, container, false
        );

        ActionBar toolbar = ((MainActivity) getActivity()).getSupportActionBar();

        Bundle bundle = getArguments();

        insertTimetableInView((TableLayout) v.findViewById(R.id.timetable_layout),
                (Timetable) bundle.getSerializable("timetable"));

        toolbar.setTitle("");
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        return v;
    }

    @Override
    public void onPause(){
        super.onPause();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    private void insertTimetableInView(TableLayout timetableLayout, Timetable timetable){
        String[] weekDays = {"Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sabado"};
        String[] initTimes = {"06:00", "08:00", "10:00", "12:00", "14:00", "16:00", "18:00", "20:00", "22:00"};

        for (int i=1; i <= initTimes.length; i++){
            TableRow tr = (TableRow) timetableLayout.getChildAt(i);

            for (int j=1; j <= weekDays.length; j++){
                TextView classSchedule = (TextView) tr.getChildAt(j);
                SubjectClass subjectClass = timetable.findClassesByTimeDay(initTimes[i-1], weekDays[j-1]);

                if(subjectClass != null){
                    Subject subject = (SubjectDB.getInstance(getContext())).getSubject(subjectClass.getSubjectCode());
                    classSchedule.setText(String.format("%s\nTurma %s\n%s ", subject.getName(), subjectClass.getName(), subjectClass.getTeacherString('\n')));
                }
            }
        }
    }
}
