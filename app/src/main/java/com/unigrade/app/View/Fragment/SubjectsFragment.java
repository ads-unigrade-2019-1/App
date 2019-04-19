package com.unigrade.app.View.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.unigrade.app.Controller.SubjectsController;
import com.unigrade.app.R;
import com.unigrade.app.View.Activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubjectsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubjectsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SubjectsController subjectsController;
    private SwipeRefreshLayout swipeLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private List<String> subjects = new ArrayList<String>();

    public SubjectsFragment() {
        // Required empty public constructor
    }

    public setSubjects(ArrayList<Subject> subjects){
        this.subjects = subjects
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectsFragment newInstance(String param1, String param2) {
        SubjectsFragment fragment = new SubjectsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_subjects, container, false);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Escolha a matéria");
       
        ListView subjectList = v.findViewById(R.id.subjects_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1, subjects);
        subjectList.setAdapter(adapter);

        callServer();

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

        if(subjectsController.isConnectedToNetwork(getActivity())) {
            new GetSubjects(this, ).execute();
        }else {
            Toast.makeText(this.getActivity(), "Não há conexão com a internet", Toast.LENGTH_SHORT).show();
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

        @Override
    public void onResume() {
        callServer();
        super.onResume();
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
