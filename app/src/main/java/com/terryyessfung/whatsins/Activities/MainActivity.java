package com.terryyessfung.whatsins.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.terryyessfung.whatsins.DB.DBManager;
import com.terryyessfung.whatsins.Fragments.DiscoverFragment;
import com.terryyessfung.whatsins.Fragments.HomeFragment;
import com.terryyessfung.whatsins.Fragments.ProfileFragment;
import com.terryyessfung.whatsins.R;

public class MainActivity extends AppCompatActivity {
    private BottomAppBar mBottomBar;
    private FloatingActionButton fab;
    private FragmentTransaction mFragmentTransaction;
    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mian);
        mBottomBar = findViewById(R.id.bottom_nav);
        mBottomBar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        fab = findViewById(R.id.button_fab);
        fab.setColorFilter(getResources().getColor(R.color.colorPrimary));

        // fab button OnClick
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFragment = null;
                startActivity(new Intent(MainActivity.this,PostImgActivity.class));
            }
        });
        setSupportActionBar(mBottomBar);




        Intent intent = getIntent();
        String token = intent.getStringExtra(LoginActivity.INTENT_TOKEN);
        String uid = intent.getStringExtra(LoginActivity.INTENT_uid);
        // insert token and uid into db
        if(token != null || uid != null)
            DBManager.getInstance(this).insertUserToken(uid,token);

        HomeFragment homeFragment = new HomeFragment();
        showFragment(homeFragment);

    }

  private  View.OnClickListener homeBtnOnClick =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HomeFragment homeFragment = new HomeFragment();
            showFragment(homeFragment);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_navigation,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                currentFragment = new HomeFragment();
                break;
            case R.id.bottomNav_discover:
                currentFragment = new DiscoverFragment();
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

    public void showFragment(Fragment fragment){
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_container,fragment);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }


}
