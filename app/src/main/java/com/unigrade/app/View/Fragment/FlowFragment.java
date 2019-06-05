package com.unigrade.app.View.Fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.unigrade.app.Controller.FlowController;
import com.unigrade.app.R;
import com.unigrade.app.View.AsyncTask.GetCourses;
import com.unigrade.app.View.AsyncTask.GetFlow;


public class FlowFragment extends Fragment {
    private ProgressBar progressBar;
    private AsyncTask getFlow;
    private LinearLayout noInternet;
    private Button btnReload;

    public FlowFragment() {
        // Required empty public constructor
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flow, container, false);

        progressBar = v.findViewById(R.id.progress_bar);
        noInternet = v.findViewById(R.id.no_internet);
        btnReload = v.findViewById(R.id.reload);

        callServer();

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callServer();
            }
        });
        return v;
    }


    private void callServer(){

        if(FlowController.getInstance().isConnectedToNetwork(getActivity())){
            noInternet.setVisibility(View.GONE);
            SharedPreferences pref = getActivity().getApplicationContext()
                    .getSharedPreferences("MyPref", 0);
            getFlow = new GetFlow(this,
                    pref.getString("courseCode", null)).execute();
        } else {
            noInternet.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(getFlow != null && getFlow.getStatus() != AsyncTask.Status.FINISHED) {
            getFlow.cancel(true);
        }
    }

}
