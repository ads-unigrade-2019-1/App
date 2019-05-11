package com.unigrade.app.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.unigrade.app.Model.Subject;
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
    private String priority;

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
        viewHolder.spinner = view.findViewById(R.id.priori);



        viewHolder.classCode.setText(((SubjectClass)this.getItem(position)).getCodeLetter());
        viewHolder.classTeacher.setText(((SubjectClass)this.getItem(position)).getTeacher());
        viewHolder.classCampus.setText(((SubjectClass)this.getItem(position)).getCampus());
        String[] schedules = ((SubjectClass)this.getItem(position)).getSchedules();
        String s = schedules[0] + "\n" + schedules[1];
        viewHolder.classTime.setText(s);
        viewHolder.checkbox.setChecked(((SubjectClass)this.getItem(position)).isSelected());

        viewHolder.spinner.setOnItemSelectedListener(getItemListener());
        //((SubjectClass) this.getItem(position)).setPriority(priority);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(inflater.getContext(),
                R.array.classes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.spinner.setAdapter(adapter);


        return view;
    }

    private AdapterView.OnItemSelectedListener getItemListener(){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                Spinner sp = view.findViewById(R.id.priori);




            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        };
    }

    private class ViewHolder{
        TextView classCode;
        TextView classTeacher;
        TextView classTime;
        TextView classCampus;
        CheckBox checkbox;
        Spinner spinner;
    }

    public void setPriori(String priority) {
        this.priority = priority;
    }
}
