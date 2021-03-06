package com.unigrade.app.View.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.Toast;

import com.unigrade.app.Controller.TimetablesController;
import com.unigrade.app.Model.Timetable;
import com.unigrade.app.R;
import com.unigrade.app.View.Activity.MainActivity;

public class ExpandedTimetableFragment extends Fragment {

    private TableLayout timetableLayout;
    private ActionBar toolbar;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(
                R.layout.fragment_expanded_timetable, container, false
        );

        toolbar = ((MainActivity) getActivity()).getSupportActionBar();

        Bundle bundle = getArguments();
        Timetable timetable = (Timetable) bundle.getSerializable("timetable");
        timetableLayout = v.findViewById(R.id.timetable_layout);

        TimetablesController timetablesController  = TimetablesController.getInstance();

        timetablesController.insertTimetableInView(
                timetableLayout,
                timetable,
                getContext(),
                false
        );

        toolbar.setTitle("");
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setDisplayShowHomeEnabled(true);

        return v;
    }

    @Override
    public void onDetach(){
        super.onDetach();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        toolbar.setDisplayHomeAsUpEnabled(false);
        toolbar.setDisplayShowHomeEnabled(false);
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

        if (TimetablesController.getInstance().isDownloadPermitted(getContext())){
            String downloadingMessage = "Realizando download da grade..";
            Toast.makeText(getContext().getApplicationContext(), downloadingMessage,
                    Toast.LENGTH_SHORT).show();

            Log.d("PERMISSAO", "Com permissao");

            String message = TimetablesController.getInstance().downloadTableLayout(
                    timetableLayout, getContext());
            Toast.makeText(getContext().getApplicationContext(), message,
                    Toast.LENGTH_SHORT).show();
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
