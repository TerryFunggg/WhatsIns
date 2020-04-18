package com.terryyessfung.whatsins.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.terryyessfung.whatsins.Fragments.DiscoverFragment;
import com.terryyessfung.whatsins.Fragments.HomeFragment;
import com.terryyessfung.whatsins.Fragments.NotificationFragment;
import com.terryyessfung.whatsins.Fragments.ProfileFragment;
import com.terryyessfung.whatsins.Fragments.SearchFragment;
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

        // Floating Action Button
       // fab = findViewById(R.id.fab);
       // fab.setOnClickListener(fabOnClick);
        // Set bottom bar
        mBottomBar = findViewById(R.id.bottom_nav);
        //mBottomBar.inflateMenu(R.menu.main_navigation);

        mBottomBar.setOnNavigationItemSelectedListener(navBarItemOnClick);
        //mBottomBar.setOnMenuItemClickListener(bottomBarOnClick);
        //mBottomBar.setNavigationOnClickListener(navBtnOnClick);


       // bottomBar = findViewById(R.id.bottom_navigation);
       // currentFragmentID = R.id.bottom_navigation;

        HomeFragment homeFragment = new HomeFragment();
        showFragment(homeFragment);

        // set bottom bar selected item onclick listener
       // bottomBar.setOnNavigationItemSelectedListener(btnBarListener);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navBarItemOnClick
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.bottomNav_home:
                    currentFragment = new HomeFragment();
                    break;
                case R.id.bottomNav_discover:
                    // TODO change to Discover
                    currentFragment = new SearchFragment();
                    break;
                case R.id.bottomNav_post:
                    currentFragment = null;
                    startActivity(new Intent(MainActivity.this,PostImgActivity.class));
                    break;
                case R.id.bottomNav_notify:
                    currentFragment = new NotificationFragment();
                    break;
                case R.id.bottomNav_user:
                    SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileID", FirebaseAuth.getInstance().getUid());
                    editor.apply();
                    currentFragment = new ProfileFragment();
                    break;
            }
            if(currentFragment != null){
                showFragment(currentFragment);
            }
            return true;
        }
    };


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.main_navigation, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
////            case R.id.bottomNav_home:
////                showMessage("Home");
////                return true;
//            case R.id.bottomNav_discover:
//                showMessage("Discover");
//                return true;
//            case R.id.bottomNav_notify:
//                showMessage("Notify");
//                return true;
//            case R.id.bottomNav_user:
//                showMessage("User");
//                return true;
//        }
//        return false;
//    }

    public void showFragment(Fragment fragment){
        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.main_container,fragment);
        mFragmentTransaction.addToBackStack(null);
        mFragmentTransaction.commit();
    }

    Toolbar.OnMenuItemClickListener bottomBarOnClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            showMessage(""+item.getItemId());
            switch (item.getItemId()) {
                case R.id.bottomNav_discover:
                    showMessage("Discover");
                    return true;
                case R.id.bottomNav_notify:
                    showMessage("Notify");
                    return true;
                case R.id.bottomNav_user:
                    showMessage("User");
                    return true;
            }
            return false;
        }
    };

    View.OnClickListener navBtnOnClick =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showMessage("Navggggg");
        }
    };

    View.OnClickListener fabOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showMessage("Fab onClick");
        }
    };

//    BottomNavigationView.OnNavigationItemSelectedListener btnBarListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            if (item.getItemId() != currentFragmentID) {
//                switch (item.getItemId()) {
//                    case R.id.bottomNav_home:
//                       // showFragment(HomeFragment.newInstance());
//                        currentFragmentID = item.getItemId();
//                        return true;
//                    case R.id.bottomNav_discover:
//                       // showFragment(HomeFragment.newInstance());
//                        currentFragmentID = item.getItemId();
//                        return true;
//                }
//            }
//            return false;
//        }
//    };

    public void showMessage(String message){
       Toast toast =  Toast.makeText(this,message,Toast.LENGTH_SHORT);
       toast.setGravity(Gravity.BOTTOM,0,300);
       toast.show();
    }



}
