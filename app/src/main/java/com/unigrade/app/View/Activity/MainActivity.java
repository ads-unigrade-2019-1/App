package com.unigrade.app.View.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;

import com.unigrade.app.DAO.ServerHelper;
import com.unigrade.app.DAO.URLs;
import com.unigrade.app.Model.Subject;
import com.unigrade.app.R;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Subject> subjectsList;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_nav);

        NavHostFragment navHostFragment = (NavHostFragment)getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomNavigationView,
                navController);

        navController.addOnDestinationChangedListener(
                new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {

                if(destination.getId() == R.id.courseFragment){
                    bottomNavigationView.setVisibility(View.GONE);
                    toolbar.setVisibility(View.GONE);
                } else if (destination.getId() == R.id.classesFragment) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else if (destination.getId() == R.id.expandedTimetableFragment) {
                    bottomNavigationView.setVisibility(View.GONE);
                } else {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    toolbar.setVisibility(View.VISIBLE);
                }
            }
        });

        // Call to awake api server
        Thread t = new Thread(MainActivity.awakeServer);
        t.start();
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

    public void setSubjectsList(ArrayList<Subject> list){
        this.subjectsList = list;
    }

    public ArrayList<Subject> getSubjectsList(){
        return this.subjectsList;
    }

    private static  Runnable awakeServer = new Runnable() {
        @Override
        public void run() {
            ServerHelper serverHelper = new ServerHelper(URLs.AWAKE_CALL);
            serverHelper.get();
        }
    };

}
