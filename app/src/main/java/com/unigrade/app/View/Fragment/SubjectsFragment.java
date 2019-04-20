package com.unigrade.app.View.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private OnFragmentInteractionListener mListener;
    private ArrayList<Subject> subjects = new ArrayList<>();
    private ProgressBar progressBar;
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

        progressBar = v.findViewById(R.id.progressBar);
        subjectList = v.findViewById(R.id.subjectsList);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Escolha a matéria");

        subjectList.setAdapter(new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_list_item_1, subjects));

        subjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Navigation. findNavController(view).navigate(R.id.connectionErrorFragment);
            }
        });

        return v;
    }

    private void callServer(){
         SubjectsController subjectsController = SubjectsController.getInstance();

        if(subjectsController.isConnectedToNetwork(getActivity())){
            progressBar.setVisibility(View.VISIBLE);
            new GetSubjects(this).execute();
        }else {
            Toast.makeText(this.getActivity(), "Não há conexão com a internet", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        callServer();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
