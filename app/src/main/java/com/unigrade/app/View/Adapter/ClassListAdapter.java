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

        SubjectClass sc = (SubjectClass)this.getItem(position);

        viewHolder.classCampus = view.findViewById(R.id.class_campus);
        viewHolder.classCode = view.findViewById(R.id.class_code);
        viewHolder.classTeacher = view.findViewById(R.id.class_teacher);
        viewHolder.classTime = view.findViewById(R.id.class_time);
        viewHolder.checkbox = view.findViewById(R.id.class_checkbox);
        viewHolder.classPriority = view.findViewById(R.id.class_priority);

        viewHolder.classCode.setText(sc.getName());
        viewHolder.classTeacher.setText(sc.getTeacherString('\n'));
        viewHolder.classCampus.setText(sc.getCampus());

        ArrayList<ClassMeeting> schedulesArray = sc.getSchedules();
        StringBuilder schedules = new StringBuilder();

        for(ClassMeeting schedule : schedulesArray) {
            schedules.append(schedule.formattedClassMeeting() + "\n");
        }

        viewHolder.classTime.setText(schedules);
        viewHolder.checkbox.setChecked(sc.isSelected());

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.classes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.classPriority.setAdapter(adapter);

        if (mapSpinner.containsKey(position)) {
            viewHolder.classPriority.setSelection(Integer.parseInt(mapSpinner.get(position)) -1,false);
        }

        viewHolder.checkbox.setOnCheckedChangeListener(checkboxListener(position));
        viewHolder.classPriority.setOnItemSelectedListener(spinnerListener(position));

        return view;
    }

    private CompoundButton.OnCheckedChangeListener checkboxListener(final int position){
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ListView lv = (ListView) buttonView.getParent().getParent().getParent(); // Isso vai pro ListView

                for (SubjectClass c : classes)
                    c.setSubjectCode(subject.getCode());

                SubjectClass sc = (SubjectClass) lv.getItemAtPosition(position);
                if(isChecked){
                    sc.setSelected(true);
                    insertIntoDatabase(sc);
                    Log.i("ADDED", sc.getTeacher() + " " + sc.isSelected());
                }else {
                    sc.setSelected(false);
                    removeFromDatabase(sc);
                    Log.i("REMOVED", sc.getTeacher() + " " + sc.isSelected());
                }
            }
        };
    }

    private AdapterView.OnItemSelectedListener spinnerListener(final int position){
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ListView lv = (ListView) view.getParent().getParent().getParent().getParent();

                SubjectClass sc = (SubjectClass) lv.getItemAtPosition(position);

                mapSpinner.put(position,parent.getItemAtPosition(pos).toString());
                System.out.println("Valor da Chave "+position+ " = "+mapSpinner.get(position));

                if(sc.isSelected()){
                    sc.setPriority(parent.getItemAtPosition(pos).toString());
                    classDB.alter(sc);
                    Log.i("SPINNER",sc.getTeacher() + " --- " + sc.getPriority());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
    }

    private void insertIntoDatabase(SubjectClass sc){
        if (!subjectDB.isSubjectOnDB(subject.getCode())){
            subjectDB.insert(subject);
            for (SubjectClass c : classes) {
                if(c.getPriority() == null)
                    c.setPriority("0");
                Log.d("timetable", "PrioridadeAdapter: " + c.getPriority());
                classDB.insert(c);
            }
            Log.i("OUTSIDEDB", subject.getCode() + " "+ sc.getTeacher());
        } else {
            classDB.alter(sc);
            Log.i("ONDB", subject.getCode() + " "+ sc.getTeacher());
        }
    }

    private void removeFromDatabase(SubjectClass sc){
        if (isLonelyAdded(sc)){
            for (SubjectClass c : classes)
                classDB.delete(c);
            subjectDB.delete(subject);
            Log.i("LONELY", subject.getCode() + " "+ sc.getTeacher());
        } else {
            classDB.alter(sc);
            Log.i("NOTLONELY", subject.getCode() + " "+ sc.getTeacher());
        }
    }

    private boolean isLonelyAdded(SubjectClass sc){
        for (SubjectClass c : classes)
            if (c.isSelected() && c != sc)
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