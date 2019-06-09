package com.unigrade.app.View.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.unigrade.app.Model.Period;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.R;

import java.util.ArrayList;

public class PeriodListAdapter extends BaseAdapter {

    private ArrayList<Period> periods;
    private Context context;

    public PeriodListAdapter(ArrayList<Period> periods, Context context) {
        this.periods = periods;
        this.context = context;
    }

    @Override
    public int getCount() {
        return periods.size();
    }

    @Override
    public Object getItem(int position) {
        return periods.get(position);
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
                    context).inflate(R.layout.item_period, parent, false
            );

            viewHolder = new ViewHolder();
            viewHolder.periodTable = convertView.findViewById(R.id.period_table);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Period period = (Period)getItem(position);
        populatePeriodTable(viewHolder, period);

        return convertView;
    }

    void populatePeriodTable(ViewHolder viewHolder, Period period){
        TableRow firstRow = new TableRow(context);
        TableRow secondRow = new TableRow(context);

        TextView title = new TextView(context);
        toString();
        title.setText(String.format("PERÍODO %s", period.getNumber()));
//        title.getLayoutParams().width = TableRow.LayoutParams.WRAP_CONTENT;
//        title.getLayoutParams().height = TableRow.LayoutParams.WRAP_CONTENT;

        TextView code = new TextView(context);
//        code.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
//        code.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        code.setText("Código");

        TextView name = new TextView(context);
//        name.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
//        name.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        name.setText("Nome");
//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) name.getLayoutParams();
//        params.rightMargin = 80;

        firstRow.addView(title);

        secondRow.addView(code);
        secondRow.addView(name);

        viewHolder.periodTable.addView(firstRow);
        viewHolder.periodTable.addView(secondRow);

        for(Subject subject : period.getSubjects()){
            insertSubjectRowInTable(subject, viewHolder);
        }
    }

    private void insertSubjectRowInTable(Subject subject, ViewHolder viewHolder){
        TableRow subjectRow = new TableRow(context);

        TextView subjectCode = new TextView(context);
//        subjectCode.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
//        subjectCode.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        subjectCode.setText(subject.getCode());


//        ViewGroup.MarginLayoutParams subjectCodeParams = (ViewGroup.MarginLayoutParams) subjectCode.getLayoutParams();
//        subjectCodeParams.rightMargin = 10;

        TextView subjectName = new TextView(context);
//        subjectName.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
//        subjectName.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        subjectName.setText(subject.getCode());
//        ViewGroup.MarginLayoutParams subjectNameParams = (ViewGroup.MarginLayoutParams) subjectName.getLayoutParams();
//        subjectNameParams.rightMargin = 80;

        subjectRow.addView(subjectCode);
        subjectRow.addView(subjectName);

        viewHolder.periodTable.addView(subjectRow);
    }

    private class ViewHolder{
        TableLayout periodTable;
    }
}
