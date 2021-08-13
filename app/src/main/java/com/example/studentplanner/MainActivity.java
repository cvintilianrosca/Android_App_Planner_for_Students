package com.example.studentplanner;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.fragment_slide.Fragment_Exams;
import com.example.studentplanner.fragment_slide.Fragment_Grades;
import com.example.studentplanner.fragment_slide.Fragment_Info;
import com.example.studentplanner.fragment_slide.Fragment_Settings;
import com.example.studentplanner.fragment_slide.Fragment_Tasks;
import com.example.studentplanner.fragment_slide.Fragment_Teachers;
import com.example.studentplanner.fragment_slide.Fragment_Timetable;
import com.example.studentplanner.fragments_bottom_nav.Fragment_Agenda;
import com.example.studentplanner.fragments_bottom_nav.Fragment_Calendar;
import com.example.studentplanner.fragments_bottom_nav.Fragment_Home;
import com.example.studentplanner.fragments_bottom_nav.Fragment_Subjects;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    BottomNavigationView bottomNavView;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer = findViewById(R.id.draw_layout);
        toolbar = findViewById(R.id.toolbar);
        floatingActionButton = findViewById(R.id.fab);
//        databaseViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(DatabaseViewModel.class);
//        databaseViewModel.getAllSubjects().observe(this, new Observer<List<Subject>>() {
//            @Override
//            public void onChanged(List<Subject> subjects) {
//                // Update RecycleView
//                Toast.makeText(MainActivity.this, "On Changed", Toast.LENGTH_SHORT).show();
//            }
//        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavView = findViewById(R.id.bottomNavView);
        bottomNavView.setBackground(null);
        bottomNavView.getMenu().getItem(2).setEnabled(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Home(toolbar, floatingActionButton)).commit();

        bottomNavView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId()){

                    case R.id.miHome:
                        selectedFragment = new Fragment_Home(toolbar, floatingActionButton);
//                        Toast.makeText(MainActivity.this, "AAAAA", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.miAgenda:
                        selectedFragment = new Fragment_Agenda(toolbar, floatingActionButton);
//                        Toast.makeText(MainActivity.this, "AAAAA", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.miSubjects:
                        selectedFragment = new Fragment_Subjects(toolbar, floatingActionButton);
//                        Toast.makeText(MainActivity.this, "AAAAA", Toast.LENGTH_SHORT).show();
                        break;
                    case  R.id.miCalendar:
                        selectedFragment = new Fragment_Calendar(toolbar, floatingActionButton);
//                        Toast.makeText(MainActivity.this, "AAAAA", Toast.LENGTH_SHORT).show();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
    } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.mi_item_Exam:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Exams(toolbar, floatingActionButton)).commit();
                break;
            case R.id.mi_item_Grades:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Grades(toolbar, floatingActionButton)).commit();
                break;
            case R.id.mi_item_InfoSchool:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Info(toolbar, floatingActionButton)).commit();
                break;
            case R.id.mi_item_Tasks:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Tasks(toolbar, floatingActionButton)).commit();
                break;
            case R.id.mi_item_teachers:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Teachers(toolbar, floatingActionButton)).commit();
                break;
            case R.id.mi_item_Settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Settings(toolbar, floatingActionButton)).commit();
                break;
            case R.id.mi_item_timeTable:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Timetable(toolbar, floatingActionButton)).commit();
                break;
        }
        return true;
    }
}
