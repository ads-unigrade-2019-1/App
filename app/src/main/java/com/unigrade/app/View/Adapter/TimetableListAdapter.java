package com.unigrade.app.View.Adapter;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TableLayout;

import androidx.navigation.Navigation;

import com.unigrade.app.Controller.TimetablesController;
import com.unigrade.app.Model.Timetable;
import com.unigrade.app.R;
import com.unigrade.app.View.Fragment.TimetablesFragment;

import java.util.ArrayList;

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
            viewHolder.btnVisualize = convertView.findViewById(R.id.btn_visualize);
            viewHolder.btnDownload = convertView.findViewById(R.id.btn_download);
            convertView.setTag(viewHolder);

            viewHolder.btnVisualize.setOnClickListener(visualizeListener());
            viewHolder.btnDownload.setOnClickListener(detailsListener());

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btnVisualize.setTag(position);
        viewHolder.btnDownload.setTag(position);

        Timetable timetable = (Timetable) this.getItem(position);
        Log.d("VISUALIZAR", "pequena " + position);

        TimetablesController.getInstance().insertTimetableInView(
                viewHolder.timetableLayout,
                timetable,
                context,
                true
        );
        return convertView;
    }

    static class ViewHolder{
        TableLayout timetableLayout;
        Button btnVisualize;
        Button btnDownload;
    }

    private View.OnClickListener visualizeListener(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Log.d("VISUALIZAR", "grande" + position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("timetable", (Timetable) getItem(position));

                Navigation.findNavController(fragment.getView())
                        .navigate(R.id.expandedTimetableFragment, bundle);
            }
        };
    }

    private View.OnClickListener detailsListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Log.d("DETALHES", String.valueOf(position));
                Timetable timetable = (Timetable) getItem(position);
                TimetablesController timetablesController = TimetablesController.getInstance();
                String classes = timetablesController.getTimetableClasses(timetable);

                new AlertDialog.Builder(context)
                        .setTitle("Turmas da Grade " + (position + 1) )
                        .setMessage(classes)
                        .setPositiveButton(android.R.string.yes, null)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .show();
            }
        };
    }
}
