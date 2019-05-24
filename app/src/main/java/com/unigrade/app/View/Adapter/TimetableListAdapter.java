package com.unigrade.app.View.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.unigrade.app.Controller.TimetablesController;
import com.unigrade.app.Model.ClassMeeting;
import com.unigrade.app.Model.Timetable;
import com.unigrade.app.R;
import com.unigrade.app.View.Fragment.TimetablesFragment;

import java.util.ArrayList;
import java.util.List;

public class TimetableListAdapter extends BaseAdapter {

    private ArrayList<Timetable> timetables;
    private TimetablesFragment fragment;
    private Context context;

    public TimetableListAdapter(ArrayList<Timetable> timetables, TimetablesFragment fragment){
        this.timetables = timetables;
        this.context = fragment.getContext();
        this.fragment = fragment;
    }
    @Override
    public int getCount() {
        return timetables.size();
    }

    @Override
    public Object getItem(int position) {
        return timetables.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(
                    context).inflate(R.layout.item_card, parent, false
            );

            viewHolder = new ViewHolder();
            viewHolder.timetableLayout = convertView.findViewById(R.id.timetable_layout);
            viewHolder.timetableType = convertView.findViewById(R.id.timetable_type);
            viewHolder.btnVisualize = convertView.findViewById(R.id.btn_visualize);
            viewHolder.btnDownload = convertView.findViewById(R.id.btn_download);
            convertView.setTag(viewHolder);

            viewHolder.btnVisualize.setOnClickListener(visualizeListener());
            viewHolder.btnDownload.setOnClickListener(downloadListener(convertView));

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btnVisualize.setTag(position);
        viewHolder.btnDownload.setTag(position);

        Timetable timetable = (Timetable) this.getItem(position);

        TimetablesController timetablesController = TimetablesController.getInstance();
        timetablesController.insertTimetableInView(
                viewHolder.timetableLayout,
                timetable,
                context,
                true
        );
        return convertView;
    }

    static class ViewHolder{
        TableLayout timetableLayout;
        TextView timetableType;
        Button btnVisualize;
        Button btnDownload;
    }

    private View.OnClickListener visualizeListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Log.d("VISUALIZAR", String.valueOf(position));
                Bundle bundle = new Bundle();
                bundle.putSerializable("timetable", (Timetable) getItem(position));
                Navigation.findNavController(fragment.getView())
                        .navigate(R.id.expandedTimetableFragment, bundle);
            }
        };
    }

    private View.OnClickListener downloadListener(final View view){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Log.d("DOWNLOAD", String.valueOf(position));

                Timetable timetable = (Timetable) getItem(position);

                TimetablesController timetablesController = TimetablesController.getInstance();
                TableLayout tableLayout = view.findViewById(R.id.timetable_layout);

                if (timetablesController.isDownloadPermitted(fragment.getContext())){
                    Log.d("PERMISSAO", "Com permissao");
                    timetablesController.insertTimetableInView(
                            tableLayout,
                            timetable,
                            context,
                            false
                    );
                    timetablesController.downloadTableLayout(tableLayout, context);
                } else {
                    Log.d("PERMISSAO", "Sem permissao");
                    askForPermission();
                }
            }
        };
    }

    private void askForPermission(){
        TimetablesController timetablesController = TimetablesController.getInstance();

        if (timetablesController.shouldShowExplanation(fragment.getActivity())) {

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(fragment.getActivity());
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permissão necessária");
            alertBuilder.setMessage("Precisamos da sua permissão para salvar sua grade" +
                    "em seu dispositivo. Conceder permissão?");

            alertBuilder.setPositiveButton(
                    android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(
                            (Activity) context,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
            });

            AlertDialog alert = alertBuilder.create();
            alert.show();

        } else {

            ActivityCompat.requestPermissions(
                    fragment.getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    1
            );
        }
    }
}
