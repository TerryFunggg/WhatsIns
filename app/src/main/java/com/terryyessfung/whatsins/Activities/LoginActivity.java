package com.terryyessfung.whatsins.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.terryyessfung.whatsins.R;

import java.util.EventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText memail,mpassword;
    private Button loginBtn;
    private ProgressBar mProgressBar;
    private TextView mtxt_toRegister;

    // Firebase
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        memail = findViewById(R.id.login_Email);
        mpassword = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        mProgressBar = findViewById(R.id.login_ProgressBar);
        buttonIsProgress(false);
        mtxt_toRegister = findViewById(R.id.login_hint_to_register);

        mAuth = FirebaseAuth.getInstance();

        mtxt_toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonIsProgress(true);
                String email = memail.getText().toString();
                String password = mpassword.getText().toString();

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    showMessage("fields wrong");
                }else{
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                                .child(mAuth.getCurrentUser().getUid());
                                        reference.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                buttonIsProgress(false);
                                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                buttonIsProgress(false);
                                            }
                                        });
                                    }else{
                                        buttonIsProgress(false);
                                        showMessage("Authentication failed");
                                    }
                                }
                            });
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
     *  Switch button and loading progress between visible and Invisible
     **/
    private void buttonIsProgress(boolean isProgress){
        if(isProgress){
            loginBtn.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            loginBtn.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
