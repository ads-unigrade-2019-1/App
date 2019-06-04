package com.unigrade.app.View.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.unigrade.app.DAO.SubjectDB;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.R;
import com.unigrade.app.View.Activity.MainActivity;
import com.unigrade.app.View.Adapter.SubjectListAdapter;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.navigation.Navigation;


public class UserSubjectsFragment extends Fragment {

    public UserSubjectsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_subjects, container, false);

        ArrayList<Subject> subjectsList = SubjectDB.getInstance(getActivity()).all();

        ListView listSubjectsUser = view.findViewById(R.id.user_subjects_list);
        listSubjectsUser.setAdapter(new SubjectListAdapter(subjectsList, getActivity()));

        listSubjectsUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("caller", "usersubjects");
                bundle.putSerializable("subject", (Serializable) parent.getAdapter().getItem(
                        position));
                Navigation.findNavController(view).navigate(R.id.classesFragment, bundle);
            }
        });

        return view;
    }

}
