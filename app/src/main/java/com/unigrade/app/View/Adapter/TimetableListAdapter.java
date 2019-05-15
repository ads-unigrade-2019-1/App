package com.unigrade.app.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.unigrade.app.Model.Timetable;
import com.unigrade.app.R;

import java.util.ArrayList;

public class TimetableListAdapter extends BaseAdapter {
    private ArrayList<Timetable> timetables;
    private Context context;
    private ViewHolder viewHolder = new ViewHolder();

    public TimetableListAdapter(ArrayList<Timetable> timetables, Context context){
        this.timetables = timetables;
        this.context = context;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);

        Timetable timetable = (Timetable) this.getItem(position);

        viewHolder.timetableLayout = view.findViewById(R.id.timetable_layout);

        for (int i=1; i < viewHolder.timetableLayout.getChildCount(); i++){ // iterate times
            TableRow tr = (TableRow) viewHolder.timetableLayout.getChildAt(i);
            for(int j=1; j < tr.getChildCount(); j++){// iterate week days
                TextView classSchedule = (TextView) tr.getChildAt(j);
                if(j%2 == 0 || i%2 != 0)
                    classSchedule.setText("*");
            }


        }

        return view;
    }

    private class ViewHolder{
        TableLayout timetableLayout;
    }
}
