package com.unigrade.app.View.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.unigrade.app.Controller.TimetablesController;
import com.unigrade.app.Model.Timetable;
import com.unigrade.app.R;
import com.unigrade.app.View.Fragment.TimetablesFragment;

import java.util.ArrayList;

public class TimetableListAdapter extends BaseAdapter {

    private ArrayList<Timetable> timetables;
    private TimetablesFragment fragment;
    private Context context;

    public TimetableListAdapter(ArrayList<Timetable> timetables, TimetablesFragment fragment){
        this.timetables = timetables;
        this.context = fragment.getContext();
        this.fragment = fragment;
    }
    @Override
    public int getCount() {
        return timetables.size();
    }

    @Override
    public Object getItem(int position) {
        return timetables.get(position);
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
                    context).inflate(R.layout.item_card, parent, false
            );

            viewHolder = new ViewHolder();
            viewHolder.timetableLayout = convertView.findViewById(R.id.timetable_layout);
            viewHolder.timetableType = convertView.findViewById(R.id.timetable_type);
            viewHolder.btnVisualize = convertView.findViewById(R.id.btn_visualize);
            viewHolder.btnDownload = convertView.findViewById(R.id.btn_download);
            convertView.setTag(viewHolder);

            viewHolder.btnVisualize.setOnClickListener(visualizeListener());
            viewHolder.btnDownload.setOnClickListener(downloadListener(convertView));

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.btnVisualize.setTag(position);
        viewHolder.btnDownload.setTag(position);

        Timetable timetable = (Timetable) this.getItem(position);

        TimetablesController timetablesController = TimetablesController.getInstance();
        timetablesController.insertTimetableInView(
                viewHolder.timetableLayout,
                timetable,
                context,
                true
        );
        return convertView;
    }

    static class ViewHolder{
        TableLayout timetableLayout;
        TextView timetableType;
        Button btnVisualize;
        Button btnDownload;
    }

    private View.OnClickListener visualizeListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Log.d("VISUALIZAR", String.valueOf(position));
                Bundle bundle = new Bundle();
                bundle.putSerializable("timetable", (Timetable) getItem(position));
                Navigation.findNavController(fragment.getView())
                        .navigate(R.id.expandedTimetableFragment, bundle);
            }
        };
    }

    private View.OnClickListener downloadListener(final View view){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                Log.d("DOWNLOAD", String.valueOf(position));

//                // custom dialog
//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.dialog);
//                dialog.setTitle("Title...");
//
//                // set the custom dialog components - text, image and button
//                TextView text = (TextView) dialog.findViewById(R.id.text);
//                text.setText("Android custom dialog example!");
//                ImageView image = (ImageView) dialog.findViewById(R.id.image);
//                image.setImageResource(R.drawable.ic_launcher);
//
//                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
//                // if button is clicked, close the custom dialog
//                dialogButton.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
            }
        };
    }
}
