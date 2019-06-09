package com.unigrade.app.View.Adapter;

import android.content.Context;
import android.util.Log;
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
        Log.d("Repeticao", "VIEW: " + convertView);

        if(convertView == null){
            Log.d("Repeticao", "view null, Position:" + position);
            convertView = LayoutInflater.from(
                    context).inflate(R.layout.item_period, parent, false
            );

            viewHolder = new ViewHolder();
            viewHolder.periodTable = convertView.findViewById(R.id.period_table);

            convertView.setTag(viewHolder);

        } else {
            Log.d("Repeticao", "view nao null, Position:" + position);
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Period period = (Period) this.getItem(position);
        populatePeriodTable(viewHolder, period);

        return convertView;
    }

    private void populatePeriodTable(ViewHolder viewHolder, Period period){
        TableRow firstRow = new TableRow(context);
        TableRow secondRow = new TableRow(context);

        TextView title = new TextView(context);
        title.setText(String.format("PERÍODO %s", period.getNumber()));
        title.setTextSize(17);
        title.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        ((TableRow.MarginLayoutParams) title.getLayoutParams()).setMargins(20, 30, 0, 0);

        TextView code = new TextView(context);
        code.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        ((TableRow.MarginLayoutParams) code.getLayoutParams()).setMargins(20, 10, 0, 5);
        code.setText("Código");

        TextView name = new TextView(context);
        name.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        name.setText("Nome");
        ((TableRow.MarginLayoutParams) name.getLayoutParams()).setMargins(0, 10, 80, 5);


        firstRow.addView(title);

        secondRow.addView(code);
        secondRow.addView(name);

        viewHolder.periodTable.addView(firstRow);
        viewHolder.periodTable.addView(secondRow);

        insertSubjectsInTable(viewHolder, period);
    }

    private void insertSubjectsInTable(ViewHolder viewHolder, Period period){
        for(Subject subject : period.getSubjects()){
            TableRow subjectRow = new TableRow(context);

            TextView subjectCode = new TextView(context);
            subjectCode.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            subjectCode.setText(subject.getCode());
            ((TableRow.MarginLayoutParams) subjectCode.getLayoutParams()).setMargins(20, 5, 10, 0);

            TextView subjectName = new TextView(context);
            subjectName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            subjectName.setText(subject.getName());
            ((TableRow.MarginLayoutParams) subjectName.getLayoutParams()).setMargins(0, 5, 80, 0);

            subjectRow.addView(subjectCode);
            subjectRow.addView(subjectName);

            viewHolder.periodTable.addView(subjectRow);
        }
    }

    static class ViewHolder{
        TableLayout periodTable;
    }
}
