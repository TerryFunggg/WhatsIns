package com.terryyessfung.whatsins.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.terryyessfung.whatsins.DB.DBManager;
import com.terryyessfung.whatsins.Fragments.DiscoverFragment;
import com.terryyessfung.whatsins.Fragments.HomeFragment;
import com.terryyessfung.whatsins.Fragments.ProfileFragment;
import com.terryyessfung.whatsins.R;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomBar;
    private FloatingActionButton fab;
    private FragmentTransaction mFragmentTransaction;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian);

        mBottomBar = findViewById(R.id.bottom_nav);
        mBottomBar.setOnNavigationItemSelectedListener(navBarItemOnClick);


        Intent intent = getIntent();
        String token = intent.getStringExtra(LoginActivity.INTENT_TOKEN);
        String uid = intent.getStringExtra(LoginActivity.INTENT_uid);
        // insert token and uid into db
        if(token != null || uid != null)
            DBManager.getInstance(this).insertUserToken(uid,token);

        HomeFragment homeFragment = new HomeFragment();
        showFragment(homeFragment);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navBarItemOnClick
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.bottomNav_home:
                    currentFragment =new HomeFragment();
                    break;
                case R.id.bottomNav_discover:
                    currentFragment = new DiscoverFragment();
                    break;
                case R.id.bottomNav_post:
                    currentFragment = null;
                    startActivity(new Intent(MainActivity.this,PostImgActivity.class));
                    break;
                case R.id.bottomNav_notify:
                   // currentFragment = new NotificationFragment();
                    break;
                case R.id.bottomNav_user:
                    currentFragment = new ProfileFragment();
                    break;
            }
            if(currentFragment != null){
                showFragment(currentFragment);
            }
            return true;
        }
    };

    public void showFragment(Fragment fragment){
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_container,fragment);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }



}
