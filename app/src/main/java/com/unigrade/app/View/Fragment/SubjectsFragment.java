package com.unigrade.app.View.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.unigrade.app.Controller.SubjectsController;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.R;
import com.unigrade.app.View.Activity.MainActivity;
import com.unigrade.app.View.AsyncTask.GetSubjects;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.navigation.Navigation;


public class SubjectsFragment extends Fragment {

    private ArrayList<Subject> subjects = new ArrayList<>();
    private ProgressBar progressBar;
    private LinearLayout noInternet;
    private Button btnReload;
    private ListView subjectList;
    private AsyncTask getSubjectsTask;
    private SearchView searchBar;

    public SubjectsFragment() {
        // Required empty public constructor
    }

    public void setSubjects(ArrayList<Subject> subjects){
        this.subjects = subjects;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public ListView getSubjectList() {
        return subjectList;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
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
        noInternet = v.findViewById(R.id.no_internet);
        btnReload = v.findViewById(R.id.reload);
        searchBar = v.findViewById(R.id.search_bar);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Escolha a matéria");

        subjectList.setOnItemClickListener(itemListener());
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callServer("");
            }
        });
        searchBar.setOnQueryTextListener(searchListener());

        return v;
    }

    private void callServer(String text){

        if(SubjectsController.getInstance().isConnectedToNetwork(getActivity())){
            subjectList.setVisibility(View.GONE);
            noInternet.setVisibility(View.GONE);
            getSubjectsTask = new GetSubjects(this,text).execute();
        } else {
            subjectList.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        }

    }

    private AdapterView.OnItemClickListener itemListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("caller", "subjects");
                bundle.putSerializable("subject", (Serializable) parent.getAdapter().getItem(position));
                Navigation.findNavController(view).navigate(R.id.classesFragment, bundle);
            }
        };
    }

    private SearchView.OnQueryTextListener searchListener(){
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                callServer(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length() >= 3)
                    callServer(newText);

                return false;
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(getSubjectsTask != null && getSubjectsTask.getStatus() != AsyncTask.Status.FINISHED) {
            getSubjectsTask.cancel(true);
        }
    }
}
