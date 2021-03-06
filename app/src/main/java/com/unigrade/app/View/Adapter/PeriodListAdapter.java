package com.unigrade.app.View.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.unigrade.app.Controller.FlowController;
import com.unigrade.app.Model.Period;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.R;
import com.unigrade.app.View.Fragment.FlowFragment;

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

        convertView = LayoutInflater.from(
                context).inflate(R.layout.item_period, parent, false
        );
        viewHolder = new ViewHolder();
        viewHolder.periodTable = convertView.findViewById(R.id.period_table);

        Period period = (Period) this.getItem(position);
        populatePeriodTable(viewHolder.periodTable, period);

        return convertView;
    }

    private void populatePeriodTable(TableLayout periodTable, Period period){
        TableRow firstRow = new TableRow(context);
        TableRow secondRow = new TableRow(context);

        TextView title = new TextView(context);
        title.setText(String.format("PERÍODO %s", period.getNumber()));
        title.setTextSize(17);
        title.setTextColor(Color.WHITE);
        title.setTypeface(null, Typeface.BOLD);
        title.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        ((TableRow.MarginLayoutParams) title.getLayoutParams())
                .setMargins(20, 20, 0, 20);

        TextView code = new TextView(context);
        code.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        ((TableRow.MarginLayoutParams) code.getLayoutParams())
                .setMargins(20, 10, 0, 5);
        code.setText("Código");

        TextView name = new TextView(context);
        name.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        name.setText("Nome");
        ((TableRow.MarginLayoutParams) name.getLayoutParams())
                .setMargins(0, 10, 80, 5);


        firstRow.addView(title);
        firstRow.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));

        secondRow.addView(code);
        secondRow.addView(name);

        periodTable.addView(firstRow);
        periodTable.addView(secondRow);

        insertSubjectsInTable(periodTable, period);
    }

    private void insertSubjectsInTable(TableLayout periodTable, Period period){
        for(Subject subject : period.getSubjects()){
            TableRow subjectRow = new TableRow(context);

            TextView subjectCode = new TextView(context);
            subjectCode.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            subjectCode.setText(subject.getCode());
            ((TableRow.MarginLayoutParams) subjectCode.getLayoutParams())
                    .setMargins(20, 5, 10, 0);

            TextView subjectName = new TextView(context);
            subjectName.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            subjectName.setText(subject.getName());
            ((TableRow.MarginLayoutParams) subjectName.getLayoutParams())
                    .setMargins(0, 5, 80, 0);

            subjectRow.addView(subjectCode);
            subjectRow.addView(subjectName);

            periodTable.addView(subjectRow);
        }
    }

    private class ViewHolder{
        TableLayout periodTable;
    }
}
