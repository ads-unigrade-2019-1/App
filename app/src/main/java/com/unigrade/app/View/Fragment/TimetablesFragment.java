package com.unigrade.app.View.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.unigrade.app.Controller.TimetablesController;
import com.unigrade.app.R;
import com.unigrade.app.View.Activity.MainActivity;
import com.unigrade.app.View.AsyncTask.GetTimetables;

public class TimetablesFragment extends Fragment {

    private ProgressBar progressBar;
    private LinearLayout noInternet;
    private LinearLayout noSubjects;
    private AsyncTask getTimetablesTask;
    private ListView timetablesList;

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TimetablesFragment() {
        // Required empty public constructor
    }

    public ListView getSubjectList() {
        return timetablesList;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_timetables, container, false);

        progressBar = v.findViewById(R.id.progress_bar);
        noInternet = v.findViewById(R.id.no_internet);
        Button btnReload = v.findViewById(R.id.reload);
        timetablesList = v.findViewById(R.id.timetables_list);
        noSubjects = v.findViewById(R.id.no_subjects);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Grades montadas");

        if (!isEmpty()) {
            callServer();
        }

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callServer();
            }
        });

        return v;
    }

    private void callServer(){

        if(TimetablesController.getInstance().isConnectedToNetwork(getActivity())){
            timetablesList.setVisibility(View.VISIBLE);
            noInternet.setVisibility(View.GONE);
            getTimetablesTask = new GetTimetables(this).execute();
        } else {
            timetablesList.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        }

    }

    private boolean isEmpty(){

        if(TimetablesController.getInstance().haveSubjects(getActivity())){
            timetablesList.setVisibility(View.VISIBLE);
            noSubjects.setVisibility(View.GONE);
            return false;
        } else {
            timetablesList.setVisibility(View.GONE);
            noSubjects.setVisibility(View.VISIBLE);
            return true;
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(getTimetablesTask != null && getTimetablesTask.getStatus() != AsyncTask.Status.FINISHED){
            getTimetablesTask.cancel(true);
        }
    }

}
