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

import com.unigrade.app.Controller.ClassesController;
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
    private ClassesController classesController;
    private ViewHolder viewHolder = new ViewHolder();
    private HashMap<Integer,String> mapSpinner = new HashMap<Integer, String>();

    public ClassListAdapter(ArrayList<SubjectClass> classes, Context context, Subject subject) {
        this.classes = classes;
        this.context = context;
        this.subject = subject;
        this.classesController = ClassesController.getInstance();
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
                    classesController.insertIntoDatabase(subjectClass, context, subject, classes);
                    Log.i("ADDED", subjectClass.getTeacherString(';'));
                }else {
                    classesController.removeFromDatabase(subjectClass, context, classes);
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
                    ClassDB classDB = ClassDB.getInstance(context);
                    classDB.alter(subjectClass);
                    Log.i("SPINNER",subjectClass.getTeacher() + " --- " + subjectClass.getPriority());
                }
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
        Spinner classPriority;
        CheckBox checkbox;
    }

}