package com.terryyessfung.whatsins.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.terryyessfung.whatsins.R;

public class EditProfileActivity extends AppCompatActivity {
    ImageView mclose,mImageView,msave;
    TextView  musername,memail,emailpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }
}
