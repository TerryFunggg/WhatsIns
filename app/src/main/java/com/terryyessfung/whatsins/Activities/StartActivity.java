package com.terryyessfung.whatsins.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.terryyessfung.whatsins.DB.DBManager;
import com.terryyessfung.whatsins.R;

public class StartActivity extends AppCompatActivity {
    private TextView mtitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Animation alphaAnim = new AlphaAnimation(0.0f,1.0f);
        alphaAnim.setDuration(1000);
        alphaAnim.setAnimationListener(mAnimationListener);

        mtitle = findViewById(R.id.start_title);
        mtitle.setAnimation(alphaAnim);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(DBManager.getInstance(this).getUid() != null){
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
        }
    }

    // Title logo Animation listener
 Animation.AnimationListener mAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
            finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };



}
