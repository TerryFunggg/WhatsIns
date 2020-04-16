package com.terryyessfung.whatsins.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.R;

import java.util.HashMap;

// TODO: Login with firebase or own web server

public class RegisterActivity extends AppCompatActivity {
    public static int PERMISSION_CODE = 1;
    public static int REQUES_GALLERY = 1;
    private ImageView mavatar;
    private Uri avatarURI;
    private EditText mEmail, mPassword, mPassword2, mName;
    private Button registerBtn;
    private ProgressBar loadingProgess;

    //Firebase
    FirebaseAuth mAuth;
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // init value
        mavatar = findViewById(R.id.avatar);
        mEmail = findViewById(R.id.regEmail);
        mName = findViewById(R.id.regName);
        mPassword = findViewById(R.id.regPassword);
        mPassword2 = findViewById(R.id.regPassword2);
        registerBtn = findViewById(R.id.regBtn);
        loadingProgess = findViewById(R.id.regProgressBar);
        buttonIsProgress(false);
        mAuth = FirebaseAuth.getInstance();

        // Register button
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonIsProgress(true);
                User newUser = new User(mEmail.getText().toString(),
                                        mName.getText().toString(),
                                        mPassword.getText().toString());
                final String password2 = mPassword2.getText().toString();
                // Check field
                if(newUser.verifyField() && newUser.comparePassword(password2))
                {
                    showMessage("Fields are not all verify");
                    buttonIsProgress(false);
                }
                else {
                    createUser(newUser);
                }
            }
        });

        mavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check permission
                if(Build.VERSION.SDK_INT >= 22){
                    requestPermission();
                }else{
                    openGallery();
                }
            }
        });

    }

    /**
     * Create user ,send auth request to web server
     **/
    private void createUser(final User newUser) {
        // TODO: Create user function, connect to web server
        mAuth.createUserWithEmailAndPassword(newUser.getEmail(),newUser.getPassword())
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            mReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            HashMap<String,Object> bashMap = new HashMap<>();
                            bashMap.put("id", userID);
                            bashMap.put("username", newUser.getName().toLowerCase());
                            bashMap.put("avator", "https://firebasestorage.googleapis.com/v0/b/whatsins.appspot.com/o/ic_launcher.png?alt=media&token=0f12314e-74ce-4d2e-9eb6-503a167ba380");

                            mReference.setValue(bashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        buttonIsProgress(false);
                                        Intent intent = new Intent(RegisterActivity.this , MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }else{
                            buttonIsProgress(false);
                            showMessage("Can't register....");
                        }
                    }
                });
    }

    /**
     * Using Toast message to notify use
     **/
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    /**
     *  Request system gallery intent
     * */
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");
        startActivityForResult(gallery, REQUES_GALLERY);
    }

    /**
     *  Switch button and loading progress between visible and Invisible
     **/
    private void buttonIsProgress(boolean isProgress){
        if(isProgress){
            registerBtn.setVisibility(View.INVISIBLE);
            loadingProgess.setVisibility(View.VISIBLE);
        }else{
            registerBtn.setVisibility(View.VISIBLE);
            loadingProgess.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Gallery intent
        if(resultCode == RESULT_OK && requestCode == REQUES_GALLERY && data != null)
        {
            avatarURI = data.getData();
            mavatar.setImageURI(avatarURI);
        }
    }

    /**
     * Check the device isn't given permission to access the system storage
     * If not ask user for permission
     **/
    private void requestPermission()
    {
        if(ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                // FIXME: If user cancel the permission , it will not open anything
                // Given message to user that accept required permission
                Toast.makeText(RegisterActivity.this,
                        "Please accept the storage permission",
                        Toast.LENGTH_SHORT);
            }
            else
            {
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_CODE);
            }

        } else
            openGallery();
    }
}
