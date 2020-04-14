package com.terryyessfung.whatsins.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.terryyessfung.whatsins.Fragments.HomeFragment;
import com.terryyessfung.whatsins.R;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomBar;
    private int currentFragmentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomBar = findViewById(R.id.bottom_navigation);
        currentFragmentID = R.id.bottom_navigation;
        showFragment(HomeFragment.newInstance());

        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() != currentFragmentID) {
                    switch (item.getItemId()) {
                        case R.id.bottomNav_home:
                            showFragment(HomeFragment.newInstance());
                            currentFragmentID = item.getItemId();
                            return true;
                        case R.id.bottomNav_discover:
                            showFragment(HomeFragment.newInstance());
                            currentFragmentID = item.getItemId();
                            return true;
                    }
                }
                return false;
                }

        });

    }

    public void showFragment(Fragment fragment){
        FragmentTransaction ftransaction = getSupportFragmentManager().beginTransaction();
        ftransaction.replace(R.id.main_container,fragment);
        ftransaction.addToBackStack(null);
        ftransaction.commit();
    }


}
