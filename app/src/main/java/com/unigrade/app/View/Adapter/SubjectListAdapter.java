package com.unigrade.app.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.unigrade.app.Model.Subject;
import com.unigrade.app.R;

import java.util.ArrayList;

public class SubjectListAdapter extends BaseAdapter {
    private ArrayList<Subject> subjects;
    private static LayoutInflater inflater = null;
    private ViewHolder viewHolder = new ViewHolder();

    public SubjectListAdapter(ArrayList<Subject> subjects, Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.subjects = subjects;
    }
    @Override
    public int getCount() {
        return subjects.size();
    }

    @Override
    public Object getItem(int position) {
        return subjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_subject, null);

        viewHolder.subjectCode = view.findViewById(R.id.subject_code);
        viewHolder.subjectName = view.findViewById(R.id.subject_name);

        viewHolder.subjectCode.setText(((Subject)this.getItem(position)).getCode());
        viewHolder.subjectName.setText(((Subject)this.getItem(position)).getName());

        return view;
    }

    private class ViewHolder{
        TextView subjectCode;
        TextView subjectName;
    }
}
