package com.unigrade.app.View.Fragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.unigrade.app.View.Activity.MainActivity;
import com.unigrade.app.R;

import androidx.navigation.Navigation;


public class CourseFragment extends Fragment {

    public CourseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_course, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Escolha campus e curso");

        final Button button = view.findViewById(R.id.btn_continue);
        button.setOnClickListener(
                Navigation.createNavigateOnClickListener(R.id.timetablesFragment, null)
        );

        // Inflate the layout for this fragment
        return view;
    }


}
