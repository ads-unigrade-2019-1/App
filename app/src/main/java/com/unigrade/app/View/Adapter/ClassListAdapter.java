package com.unigrade.app.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.unigrade.app.Model.ClassMeeting;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ClassListAdapter extends BaseAdapter {

    private HashMap classItem;
    private ArrayList<SubjectClass> classes = new ArrayList<>();

    private static LayoutInflater inflater = null;
    //private View v;
    private Activity act;
    private ViewHolder viewHolder = new ViewHolder();

    public ClassListAdapter(ArrayList<SubjectClass> classes, Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.classes = classes;
    }

    @Override
    public int getCount() {
        return classes.size();
    }

    @Override
    public Object getItem(int position) {
        return classes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_class, null);

        viewHolder.classCampus = view.findViewById(R.id.class_campus);
        viewHolder.classCode = view.findViewById(R.id.class_code);
        viewHolder.classTeacher = view.findViewById(R.id.class_teacher);
        viewHolder.classTime = view.findViewById(R.id.class_time);
        viewHolder.checkbox = view.findViewById(R.id.checkbox);

        viewHolder.classCode.setText(((SubjectClass)this.getItem(position)).getName());

        ArrayList<String> teacherArray = ((SubjectClass)this.getItem(position)).getTeacher();
        StringBuilder teachers = new StringBuilder();

        for(String teacher : teacherArray) {
            teachers.append(teacher + "\n");
        }

        viewHolder.classTeacher.setText(teachers);

        viewHolder.classCampus.setText(((SubjectClass)this.getItem(position)).getCampus());

        ArrayList<ClassMeeting> schedulesArray = ((SubjectClass)this.getItem(position)).getSchedules();
        StringBuilder schedules = new StringBuilder();

        for(ClassMeeting schedule : schedulesArray) {
            schedules.append(schedule + "\n");
        }

        //viewHolder.classTeacher.setText(schedules);

        //String[] schedules = ((SubjectClass)this.getItem(position)).getSchedules();
        //String s = schedules[0] + "\n" + schedules[1];
        //viewHolder.classTime.setText(s);

        viewHolder.checkbox.setChecked(((SubjectClass)this.getItem(position)).isSelected());

        return view;
    }

    private class ViewHolder{
        TextView classCode;
        TextView classTeacher;
        TextView classTime;
        TextView classCampus;
        CheckBox checkbox;
    }
}
