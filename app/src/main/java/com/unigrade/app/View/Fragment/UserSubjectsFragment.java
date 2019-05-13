package com.unigrade.app.View.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.unigrade.app.Controller.SubjectsController;
import com.unigrade.app.DAO.ClassDAO;
import com.unigrade.app.DAO.SubjectDAO;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.Model.SubjectClass;
import com.unigrade.app.R;
import com.unigrade.app.View.Activity.MainActivity;
import com.unigrade.app.View.Adapter.SubjectListAdapter;
import com.unigrade.app.View.AsyncTask.GetSubjects;
import com.unigrade.app.View.AsyncTask.RefreshUserSubjectsFragment;

import java.io.Serializable;
import java.util.ArrayList;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserSubjectsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserSubjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserSubjectsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<Subject> subjects = new ArrayList<>();
    private ListView subjectList;
    private AsyncTask getSubjectsTask;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SubjectDAO subjectDAO;

    public UserSubjectsFragment() {
        // Required empty public constructor
    }

    public void setSubjects(ArrayList<Subject> subjects){
        this.subjects = subjects;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public ListView getSubjectList() {
        return subjectList;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserSubjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserSubjectsFragment newInstance(String param1, String param2) {
        UserSubjectsFragment fragment = new UserSubjectsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_subjects, container, false);

        boolean removeSubject;

        callServer();

        subjectDAO = new SubjectDAO(getActivity());
        ArrayList<Subject> subjectsList = subjectDAO.all();

        try{
            ArrayList<Subject> verifyList = this.getSubjects();

            for (int i = 0; i < subjectsList.size(); i++) {
                removeSubject = true;
                for (int j = 0; j < verifyList.size(); j++) {
                    if(verifyList.get(j).getCode().contains(subjectsList.get(i).getCode())) {
                        subjectDAO.alter(verifyList.get(j));
                        subjectsList.set(i, verifyList.get(j));
                        removeSubject = false;
                        break;
                    }
                }
                if (removeSubject == true) {
                    subjectDAO.delete(subjectsList.get(i));
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

    private void callServer(){
        SubjectsController subjectsController = SubjectsController.getInstance();

        if(subjectsController.isConnectedToNetwork(getActivity())){
            //subjectList.setVisibility(View.VISIBLE);
            //noInternet.setVisibility(View.GONE);
            getSubjectsTask = new RefreshUserSubjectsFragment(this).execute();
        } else {
            subjectList.setVisibility(View.GONE);
            //noInternet.setVisibility(View.VISIBLE);
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
