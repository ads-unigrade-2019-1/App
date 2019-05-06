package com.unigrade.app.View.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import com.unigrade.app.Model.Subject;
import com.unigrade.app.R;
import com.unigrade.app.View.Fragment.SubjectsFragment;
import com.unigrade.app.View.Fragment.TimetablesFragment;
import com.unigrade.app.View.Fragment.UserSubjectsFragment;
import com.unigrade.app.View.Fragment.ClassesFragment;
import com.unigrade.app.View.Fragment.CourseFragment;
import com.unigrade.app.View.Fragment.FlowFragment;

import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements SubjectsFragment.OnFragmentInteractionListener,
                    CourseFragment.OnFragmentInteractionListener,
                    FlowFragment.OnFragmentInteractionListener,
        UserSubjectsFragment.OnFragmentInteractionListener,
        TimetablesFragment.OnFragmentInteractionListener,
                    ClassesFragment.OnFragmentInteractionListener{

    private ArrayList<Subject> subjectsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView,
                navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {

                if(destination.getId() == R.id.courseFragment){
                    bottomNavigationView.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                } else if (destination.getId() == R.id.classesFragment) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_course) {
            Navigation.findNavController(findViewById(R.id.nav_host_fragment))
                    .navigate(R.id.courseFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }

    public void setSubjectsList(ArrayList<Subject> list){
        this.subjectsList = list;
    }

    public ArrayList<Subject> getSubjectsList(){
        return this.subjectsList;
    }

}
