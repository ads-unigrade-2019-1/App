package com.unigrade.app.View.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
    private SubjectDB subjectDB;

    public UserSubjectsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_subjects, container, false);

        boolean removeSubject;

        subjectDB = SubjectDB.getInstance(getActivity());
        ArrayList<Subject> subjectsList = subjectDB.all();

        try{
            ArrayList<Subject> verifyList = ((MainActivity) getActivity()).getSubjectsList();

            for (int i = 0; i < subjectsList.size(); i++) {
                removeSubject = true;
                for (int j = 0; j < verifyList.size(); j++) {
                    if(verifyList.get(j).getCode().contains(subjectsList.get(i).getCode())) {
                        subjectDB.alter(verifyList.get(j));
                        subjectsList.set(i, verifyList.get(j));
                        removeSubject = false;
                        break;
                    }
                }
                if (removeSubject) {
                    subjectDB.delete(subjectsList.get(i).getCode());
                    subjectsList.remove(i);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        ListView listSubjectsUser = view.findViewById(R.id.user_subjects_list);
        listSubjectsUser.setAdapter(new SubjectListAdapter(subjectsList, getActivity()));

        listSubjectsUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("caller", "usersubjects");
                bundle.putSerializable("subject", (Serializable) parent.getAdapter().getItem(position));
                Navigation.findNavController(view).navigate(R.id.classesFragment, bundle);
            }
        });

        return view;
    }

}
