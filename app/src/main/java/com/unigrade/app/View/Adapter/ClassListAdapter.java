package com.unigrade.app.View.Adapter;

import android.app.Activity;
import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.unigrade.app.DAO.ClassDB;
import com.unigrade.app.DAO.SubjectDB;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.Model.ClassMeeting;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.R;
import java.util.ArrayList;
import java.util.HashMap;

public class ClassListAdapter extends BaseAdapter {

    private ArrayList<SubjectClass> classes;
    private Context context;
    private Subject subject;
    private ViewHolder viewHolder = new ViewHolder();
    private ClassDB classDB;
    private SubjectDB subjectDB;
    private HashMap<Integer,String> mapSpinner = new HashMap<Integer, String>();

    public ClassListAdapter(ArrayList<SubjectClass> classes, Context context, Subject subject) {
        this.classes = classes;
        this.context = context;
        this.subject = subject;
        this.subjectDB = new SubjectDB(context);
        this.classDB = new ClassDB(context);
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_class, null);

        SubjectClass subjectClass = (SubjectClass)this.getItem(position);

        viewHolder.classCampus = view.findViewById(R.id.class_campus);
        viewHolder.classCode = view.findViewById(R.id.class_code);
        viewHolder.classTeacher = view.findViewById(R.id.class_teacher);
        viewHolder.classTime = view.findViewById(R.id.class_time);
        viewHolder.checkbox = view.findViewById(R.id.class_checkbox);
        viewHolder.classPriority = view.findViewById(R.id.class_priority);

        viewHolder.classCode.setText(subjectClass.getName());
        viewHolder.classTeacher.setText(subjectClass.getTeacherString('\n'));
        viewHolder.classCampus.setText(subjectClass.getCampus());
        viewHolder.classTime.setText(subjectClass.getSchedulesString());
        viewHolder.checkbox.setChecked(subjectClass.isSelected());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                context, R.array.classes_array, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.classPriority.setAdapter(adapter);

        if (mapSpinner.containsKey(position)) {
            viewHolder.classPriority.setSelection(
                    Integer.parseInt(mapSpinner.get(position)) -1,false
            );
        }

        viewHolder.checkbox.setOnCheckedChangeListener(checkboxListener(position));
        viewHolder.classPriority.setOnItemSelectedListener(spinnerListener(position));

        return view;
    }

    private CompoundButton.OnCheckedChangeListener checkboxListener(final int position){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ListView listView = (ListView) buttonView.getParent().getParent().getParent(); // Isso vai pro ListView

                SubjectClass subjectClass = (SubjectClass) listView.getItemAtPosition(position);
                if(isChecked){
                    subjectClass.setSelected(true);
                    insertIntoDatabase(subjectClass);
                    Log.i("ADDED", subjectClass.getTeacherString(';'));
                }else {
                    subjectClass.setSelected(false);
                    removeFromDatabase(subjectClass);
                    Log.i("REMOVED", subjectClass.getTeacherString(';'));
                }
            }
        };
    }

    private AdapterView.OnItemSelectedListener spinnerListener(final int position){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ListView lv = (ListView) view.getParent().getParent().getParent().getParent();

                SubjectClass subjectClass = (SubjectClass) lv.getItemAtPosition(position);
                Log.d("Timetable ", "SC NameCode:" + subjectClass.getName() + " " + subjectClass.getSubjectCode());

                mapSpinner.put(position, parent.getItemAtPosition(pos).toString());
                System.out.println("Valor da Chave "+position+ " = "+mapSpinner.get(position));

                if(subjectClass.isSelected()){
                    subjectClass.setPriority(parent.getItemAtPosition(pos).toString());
                    classDB.alter(subjectClass);
                    Log.i("SPINNER",subjectClass.getTeacher() + " --- " + subjectClass.getPriority());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private void insertIntoDatabase(SubjectClass subjectClass){
        if (!subjectDB.isSubjectOnDB(subject.getCode())){
            subjectDB.insert(subject);
            for (SubjectClass c : classes) {
                if(c.getPriority() == null)
                    c.setPriority("1");
                Log.d("timetable", "PrioridadeAdapter: " + c.getPriority());
                classDB.insert(c);
            }
            Log.i("OUTSIDEDB", subject.getCode() + " "+ subjectClass.getTeacher());
        } else {
            classDB.alter(subjectClass);
            Log.i("ONDB", subject.getCode() + " "+ subjectClass.getTeacher());
        }
    }

    private void removeFromDatabase(SubjectClass subjectClass){
        if (isLonelyAdded(subjectClass)){
            for (SubjectClass c : classes)
                classDB.delete(c);
            subjectDB.delete(subject);
            Log.i("LONELY", subject.getCode() + " "+ subjectClass.getTeacher());
        } else {
            classDB.alter(subjectClass);
            Log.i("NOTLONELY", subject.getCode() + " "+ subjectClass.getTeacher());
        }
    }

    private boolean isLonelyAdded(SubjectClass subjectClass){
        for (SubjectClass c : classes)
            if (c.isSelected() && c != subjectClass)
                return false;

        return true;
    }

    private class ViewHolder{
        TextView classCode;
        TextView classTeacher;
        TextView classTime;
        TextView classCampus;
        Spinner classPriority;
        CheckBox checkbox;
    }

}