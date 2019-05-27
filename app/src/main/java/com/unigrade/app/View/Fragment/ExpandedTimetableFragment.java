package com.unigrade.app.View.Fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.unigrade.app.Controller.TimetablesController;
import com.unigrade.app.DAO.SubjectDB;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.Model.Timetable;
import com.unigrade.app.R;
import com.unigrade.app.View.Activity.MainActivity;

public class ExpandedTimetableFragment extends Fragment {

    private Timetable timetable;
    private TableLayout timetableLayout;

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
        timetable = (Timetable) bundle.getSerializable("timetable");
        timetableLayout = v.findViewById(R.id.timetable_layout);

        TimetablesController timetablesController  = TimetablesController.getInstance();

        timetablesController.insertTimetableInView(
                timetableLayout,
                timetable,
                getContext(),
                false
        );

        toolbar.setTitle("");
        toolbar.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        return v;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_expanded_fragment, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.item_download){
            downloadTimetable();
            return true;
        } else if (id == android.R.id.home){
            getActivity().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void downloadTimetable(){

        TimetablesController timetablesController = TimetablesController.getInstance();

        if (timetablesController.isDownloadPermitted(getContext())){
            Log.d("PERMISSAO", "Com permissao");
            timetablesController.downloadTableLayout(timetableLayout, getContext());
        } else {
            Log.d("PERMISSAO", "Sem permissao");
            askForPermission();
        }

    }

    private void askForPermission(){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setCancelable(false);
        alertBuilder.setTitle("Permissão necessária");
        alertBuilder.setMessage("Precisamos da sua permissão para salvar sua grade " +
                "em seu dispositivo. Conceder permissão?");

        alertBuilder.setPositiveButton(
                android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                2);
                    }
                });

        AlertDialog alert = alertBuilder.create();
        alert.show();

    }
}
