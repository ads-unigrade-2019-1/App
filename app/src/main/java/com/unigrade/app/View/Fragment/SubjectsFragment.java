package com.unigrade.app.View.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.unigrade.app.Controller.SubjectsController;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.R;
import com.unigrade.app.View.Activity.MainActivity;
import com.unigrade.app.View.AsyncTask.GetSubjects;

import java.util.ArrayList;

import androidx.navigation.Navigation;


public class SubjectsFragment extends Fragment {

    private ArrayList<Subject> subjects = new ArrayList<>();
    private ProgressBar progressBar;
    private LinearLayout noInternet;
    private Button btnReload;
    private ListView subjectList;

    public SubjectsFragment() {
        // Required empty public constructor
    }

    public void setSubjects(ArrayList<Subject> subjects){
        this.subjects = subjects;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_subjects, container, false);

        progressBar = v.findViewById(R.id.progress_bar);
        subjectList = v.findViewById(R.id.subjects_list);
        noInternet = v.findViewById(R.id.subjects_no_internet);
        btnReload = v.findViewById(R.id.subjects_reload);


        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Escolha a matéria");

        subjectList.setAdapter(new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_list_item_1, subjects));

        callServer();

        subjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Navigation.findNavController(view).navigate(R.id.classesFragment);
            }
        });

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callServer();
            }
        });

        return v;
    }

    private void callServer(){
         SubjectsController subjectsController = SubjectsController.getInstance();

        if(subjectsController.isConnectedToNetwork(getActivity())){
            subjectList.setVisibility(View.VISIBLE);
            noInternet.setVisibility(View.GONE);
            new GetSubjects(this).execute();
        } else {
            subjectList.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public ListView getSubjectList() {
        return subjectList;
    }
}
