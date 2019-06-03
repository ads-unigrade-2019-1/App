package com.unigrade.app.View.Adapter;

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
import android.widget.Toast;

import com.unigrade.app.Controller.ClassesController;
import com.unigrade.app.DAO.ClassDB;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.R;

import java.util.ArrayList;

public class ClassListAdapter extends BaseAdapter {

    private ArrayList<SubjectClass> classes;
    private Context context;
    private Subject subject;

    public ClassListAdapter(ArrayList<SubjectClass> classes, Context context, Subject subject) {
        this.classes = classes;
        this.context = context;
        this.subject = subject;
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
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = LayoutInflater.from(
                    context).inflate(R.layout.item_class, parent, false
            );

            viewHolder = new ViewHolder();
            viewHolder.classCampus = convertView.findViewById(R.id.class_campus);
            viewHolder.classCode = convertView.findViewById(R.id.class_code);
            viewHolder.classTeacher = convertView.findViewById(R.id.class_teacher);
            viewHolder.classTime = convertView.findViewById(R.id.class_time);
            viewHolder.checkbox = convertView.findViewById(R.id.class_checkbox);
            viewHolder.classPriority = convertView.findViewById(R.id.class_priority);
            convertView.setTag(viewHolder);

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    context, R.array.classes_array, android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            viewHolder.classPriority.setAdapter(adapter);

            viewHolder.checkbox.setOnClickListener(checkboxListener());
            viewHolder.classPriority.setOnItemSelectedListener(spinnerListener());

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SubjectClass subjectClass = (SubjectClass)this.getItem(position);

        viewHolder.classCode.setText(subjectClass.getName());
        viewHolder.classTeacher.setText(subjectClass.getTeacherString('\n'));
        viewHolder.classCampus.setText(subjectClass.getCampus());
        viewHolder.classTime.setText(subjectClass.getSchedulesString());
        viewHolder.classPriority.setSelection((Integer.parseInt(subjectClass.getPriority()) - 1));
        viewHolder.classPriority.setTag(position);
        viewHolder.checkbox.setChecked(subjectClass.isSelected());
        viewHolder.checkbox.setTag(position);

        return convertView;
    }

    private View.OnClickListener checkboxListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = ((CheckBox) view).isChecked();
                int position = (Integer) view.getTag();
                Log.d("CHECKBOX", String.valueOf(position));
                SubjectClass subjectClass = (SubjectClass)getItem(position);
                if(isChecked){
                    ClassesController.getInstance().insertIntoDatabase(
                            subjectClass, context, subject, classes);
                    String message = "Turma " + subjectClass.getName() + " adicionada";
                    Toast.makeText(context.getApplicationContext(), message,
                            Toast.LENGTH_SHORT).show();
                    Log.i("ADDED", subjectClass.getTeacherString(';'));
                }else {
                    ClassesController.getInstance().removeFromDatabase(
                            subjectClass, context, classes);
                    String message = "Turma " + subjectClass.getName() + " removida";
                    Toast.makeText(context.getApplicationContext(), message,
                            Toast.LENGTH_SHORT).show();
                    Log.i("REMOVED", subjectClass.getTeacherString(';'));
                }

            }
        };
    }

    private AdapterView.OnItemSelectedListener spinnerListener(){

        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                int position = (Integer) parent.getTag();
                Log.d("SPINNER", String.valueOf(position));
                SubjectClass subjectClass = (SubjectClass)getItem(position);

                subjectClass.setPriority(parent.getItemAtPosition(pos).toString());

                ClassDB.getInstance(context).alter(subjectClass);
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