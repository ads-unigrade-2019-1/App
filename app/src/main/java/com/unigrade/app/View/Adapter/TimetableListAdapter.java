package com.unigrade.app.View.Adapter;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
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
            viewHolder.timetableType = convertView.findViewById(R.id.timetable_type);
            viewHolder.btnVisualize = convertView.findViewById(R.id.btn_visualize);
            viewHolder.btnDownload = convertView.findViewById(R.id.btn_download);
            convertView.setTag(viewHolder);

            viewHolder.btnVisualize.setOnClickListener(visualizeListener());
            viewHolder.btnDownload.setOnClickListener(downloadListener());

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btnVisualize.setTag(position);
        viewHolder.btnDownload.setTag(position);

        Timetable timetable = (Timetable) this.getItem(position);

        for (int i=1; i < viewHolder.timetableLayout.getChildCount(); i++){ // iterate times
            TableRow tr = (TableRow) viewHolder.timetableLayout.getChildAt(i);
            for(int j=1; j < tr.getChildCount(); j++){// iterate week days
                TextView classSchedule = (TextView) tr.getChildAt(j);
                if(j%2 == 0 || i%2 != 0)
                    classSchedule.setText("*");
            }

        }

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

    private View.OnClickListener downloadListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Log.d("DOWNLOAD", String.valueOf(position));

            }
        };
    }


}
