package com.unigrade.app.View.AsyncTask;

import android.os.AsyncTask;
import android.view.View;

import com.unigrade.app.Controller.FlowController;
import com.unigrade.app.Model.Period;
import com.unigrade.app.View.Adapter.PeriodListAdapter;
import com.unigrade.app.View.Fragment.FlowFragment;

import java.util.ArrayList;

import static java.security.AccessController.getContext;

public class GetFlow extends AsyncTask<String, Integer, ArrayList<Period> > {

    private FlowFragment flowFragment;
    private String courseCode;

    public GetFlow(FlowFragment flowFragment, String courseCode){
        this.flowFragment = flowFragment;
        this.courseCode = courseCode;
    }

    @Override
    protected void onPreExecute() {
        flowFragment.getProgressBar().setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<Period> doInBackground(String... params) {
        return FlowController.getInstance().getFlow(courseCode);
    }

    @Override
    protected void onPostExecute(ArrayList<Period> flow) {
        flowFragment.getProgressBar().setVisibility(View.GONE);
        flowFragment.getPeriodList().setAdapter(
                new PeriodListAdapter(flow, flowFragment.getContext())
        );
    }
}