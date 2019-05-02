package com.unigrade.app.View.Fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.unigrade.app.Controller.ClassesController;
import com.unigrade.app.DAO.ClassDAO;
import com.unigrade.app.DAO.SubjectDAO;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.View.Activity.MainActivity;
import com.unigrade.app.View.Adapter.ClassListAdapter;
import com.unigrade.app.R;
import com.unigrade.app.View.AsyncTask.GetClasses;

import java.util.ArrayList;


public class ClassesFragment extends Fragment {

    private ArrayList<SubjectClass> classes = new ArrayList<>();
    private ClassListAdapter adapter;
    private ProgressBar progressBar;
    private LinearLayout noInternet;
    private AsyncTask getClassesTask;
    private ListView classesList;
    private Button btnReload;
    private TextView tvClassCredits;
    private TextView tvClassTitle;

    public ClassesFragment() {
        // Required empty public constructor
    }


    public void setClasses(ArrayList<SubjectClass> classes){
        this.classes = classes;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public ListView getClassesList() {
        return classesList;
    }

    public ArrayList<SubjectClass> getClasses() {
        return classes;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_classes, container, false);

        Bundle bundle = getArguments();
        Subject subject = (Subject) bundle.getSerializable("subject");

        tvClassTitle = v.findViewById(R.id.class_title);
        tvClassCredits = v.findViewById(R.id.class_credits);
        tvClassTitle.setText(subject.getName());
        tvClassCredits.setText("11-11-11-11");

        progressBar = v.findViewById(R.id.progress_bar);
        classesList = v.findViewById(R.id.class_list);
        noInternet = v.findViewById(R.id.no_internet);
        btnReload = v.findViewById(R.id.reload);

        SubjectDAO subjectDAO = new SubjectDAO(getActivity());
        ClassDAO classDAO = new ClassDAO(getActivity());

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Escolha a turma");

        callServer();

        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callServer();
            }
        });

        // Inflate the layout for this fragment
        return v;
    }

    private void callServer(){
        ClassesController classesController = ClassesController.getInstance();

        if(classesController.isConnectedToNetwork(getActivity())){
            classesList.setVisibility(View.VISIBLE);
            noInternet.setVisibility(View.GONE);
            getClassesTask = new GetClasses(this).execute();
        } else {
            classesList.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
