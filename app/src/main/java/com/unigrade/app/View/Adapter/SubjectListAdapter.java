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
    private Context context;

    public SubjectListAdapter(ArrayList<Subject> subjects, Context context){
        this.subjects = subjects;
        this.context = context;
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
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(
                    context).inflate(R.layout.item_subject, parent, false
            );
            viewHolder = new ViewHolder();
            viewHolder.subjectCode = convertView.findViewById(R.id.subject_code);
            viewHolder.subjectName = convertView.findViewById(R.id.subject_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Subject subject = (Subject) this.getItem(position);

        viewHolder.subjectCode.setText(subject.getCode());
        viewHolder.subjectName.setText(subject.getName());
        return convertView;
    }

    private class ViewHolder{
        TextView subjectCode;
        TextView subjectName;
    }
}
