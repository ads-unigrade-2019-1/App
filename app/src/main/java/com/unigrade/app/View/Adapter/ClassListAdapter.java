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

import com.unigrade.app.DAO.ClassDAO;
import com.unigrade.app.DAO.SubjectDAO;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassListAdapter extends BaseAdapter {

    private HashMap classItem;
    private ArrayList<SubjectClass> classes = new ArrayList<>();
    private Context context;
    private Subject subject;

    private static LayoutInflater inflater = null;
    //private View v;
    private Activity act;
    private ViewHolder viewHolder = new ViewHolder();
    private String priority;
    private ClassDAO classDAO;
    private SubjectDAO subjectDAO;
    private HashMap<Integer,String> mapSpinner = new HashMap<Integer, String>();

        public ClassListAdapter(ArrayList<SubjectClass> classes, Context context, Subject subject) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.classes = classes;
        this.context = context;
        this.subject = subject;

        this.subjectDAO = new SubjectDAO(context);
        this.classDAO = new ClassDAO(context);
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
        public View getView(final int position, View convertView, ViewGroup parent) {
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



        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,R.array.classes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.spinner.setAdapter(adapter);

        if (mapSpinner.containsKey(position)) {
            viewHolder.spinner.setSelection(Integer.parseInt(mapSpinner.get(position)) -1,false);
        }
        viewHolder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                ListView lv = (ListView) view.getParent().getParent().getParent().getParent();

                SubjectClass sc = (SubjectClass) lv.getItemAtPosition(position);

                mapSpinner.put(position,parent.getItemAtPosition(pos).toString());
                System.out.println("Valor da Chave "+position+ " = "+mapSpinner.get(position));


                if(sc.isSelected()){
                    sc.setPriority(parent.getItemAtPosition(pos).toString());
                    Log.i("SPINNER",sc.getTeacher() + " --- " + sc.getPriority());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    private void insertIntoDatabase(SubjectClass sc){
        if (!subjectDAO.isSubjectOnDB(subject.getCode())){
            subjectDAO.insert(subject);
            for (SubjectClass c : classes)
                classDAO.insert(c);
            Log.i("OUTSIDEDB", subject.getCode() + " "+ sc.getTeacher());
        } else {
            classDAO.alter(sc);
            Log.i("ONDB", subject.getCode() + " "+ sc.getTeacher());
        }
    }

    private void removeFromDatabase(SubjectClass sc){
        if (isLonelyAdded(sc)){
            for (SubjectClass c : classes)
                classDAO.delete(c);
            subjectDAO.delete(subject);
            Log.i("LONELY", subject.getCode() + " "+ sc.getTeacher());
        } else {
            classDAO.alter(sc);
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
        CheckBox checkbox;
        Spinner spinner;
    }

    public void setPriori(String priority) {
        this.priority = priority;
    }
}