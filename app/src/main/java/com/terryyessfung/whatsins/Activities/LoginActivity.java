package com.terryyessfung.whatsins.Activities;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.terryyessfung.whatsins.API.LoginResult;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    public static String INTENT_TOKEN = "com.terryyessfung.whatsins.LoginActivity.TOKEN";
    public static String INTENT_uid = "com.terryyessfung.whatsins.LoginActivity.UID";
    private final String TAG_LOGIN = "LoginActivity";
    public static int PERMISSION_CODE = 1;
    // use for button tag
    private final String btn_tag_reg = "register";
    private final String btn_tag_login = "login";
    private EditText memail,mpassword,mname;
    private ImageView mavatar,mcancelAvatar;
    private Uri avatarURI = null;
    private Button mButton;
    private ProgressBar mProgressBar;
    private TextView mHintText,mtitle,mhint;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        memail = findViewById(R.id.login_Email);
        mpassword = findViewById(R.id.login_password);
        mname = findViewById(R.id.login_name);
        mavatar = findViewById(R.id.login_avatar);
        mavatar.setImageResource(R.drawable.ic_person_black_24dp);
        mavatar.setVisibility(View.INVISIBLE);
        mcancelAvatar = findViewById(R.id.login_cancelAvatar);
        mButton = findViewById(R.id.login_btn);
        mButton.setTag(btn_tag_login);
        mtitle = findViewById(R.id.login_title);
        mProgressBar = findViewById(R.id.login_ProgressBar);
        buttonIsProgress(false);
        mHintText = findViewById(R.id.login_hint_to_register);
        mhint = findViewById(R.id.login_wrong_hint);

        // Onclick
        mHintText.setOnClickListener(hintTextOnclick);
        mButton.setOnClickListener(btnOnClick);
        mavatar.setOnClickListener(avatarOnClick);
        mcancelAvatar.setOnClickListener(cancelAvatarOnClick);

    }
  /**
   * Login/Register button onClick
   **/
  View.OnClickListener btnOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            buttonIsProgress(true);
            final String email = memail.getText().toString();
            final String password = mpassword.getText().toString();
            if(mButton.getTag() == btn_tag_login){
                // If current is login button
                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    showMessage("fields wrong");
                }else {
                    userLogin(email,password);
                }
            }else if(mButton.getTag() == btn_tag_reg){
                // if current btn is register
                String name = mname.getText().toString();
                userRegister(email,password,name,avatarURI);
            }
        }
    };

  /**
   * Avatar onClick - help user to choose photo to be a avatar
   * **/
   private View.OnClickListener avatarOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Check permission
            if(Build.VERSION.SDK_INT >= 22){
                requestPermission();
            }else{
                openGallery();
            }
        }
    };

   /**
    * cancel button OnClick - undo the photo selected
    * **/
   private View.OnClickListener cancelAvatarOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mavatar.setImageResource(R.drawable.ic_person_black_24dp);
            avatarURI = null;
            mcancelAvatar.setVisibility(View.INVISIBLE);
        }
    };

   /**
    * User Register function
    * It will send all the element to api server.
    * It will receive the created message by ResponseBody
    * **/
    private void userRegister(String email, String password, String name, Uri avatarURI) {
        MultipartBody.Part imgBody = null;
        RequestBody rname = RequestBody.create(null,name);
        RequestBody remail = RequestBody.create(null,email);
        RequestBody rpassword = RequestBody.create(null,password);
        // if avatar is null , that means user don't want to have a avatar,
        // server will given a default avatar for user
        if(avatarURI != null){
            File img = new File(avatarURI.getPath());
            Log.d(TAG_LOGIN,img.getName());
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),img);
            imgBody = MultipartBody.Part.createFormData("avatar",img.getName(),requestBody);
        }
        Call<ResponseBody> call;
        // different options api call for register ,
        // one is have avatar , another the avatar is null
        call = avatarURI == null ? DataManager.getInstance().getAPIService().register(rname,remail,rpassword)
                : DataManager.getInstance().getAPIService().registerWithImg(rname,remail,rpassword,imgBody);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // user created
                if(response.isSuccessful() && response.code() == 201){
                    Log.d(TAG_LOGIN,response.message());
                    // restart the activity
                    // so it will go back to login UI
                    restartActivity();
                }
                Log.d(TAG_LOGIN,response.code() + "");
                // conflict error response - email already register
                if(response.code() == 409){
                    mhint.setVisibility(View.VISIBLE);
                    mhint.setText(R.string.login_error_email);

                }
                if(response.code() == 400){
                    mhint.setText(R.string.register_email_not_correct);
                }
                buttonIsProgress(false);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    /**
     * User login function
     * It will receive the user Token from server side
     * if response successful, the app will redirect to home page
     * **/
    public void userLogin(String email, String password){
        HashMap<String,String> map = new HashMap<>();
        map.put("email",email);
        map.put("password",password);
        Call<LoginResult> call = DataManager.getInstance().getAPIService().login(map);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if(response.isSuccessful()){
                    LoginResult result = response.body();
                    Log.d(TAG_LOGIN, result.getToken());
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra(INTENT_TOKEN,result.getToken());
                    intent.putExtra(INTENT_uid,result.getUid());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
                // Valiadation error
                if(response.code() == 400){
                    mhint.setText(R.string.login_error_not_correct);
                    mhint.setVisibility(View.VISIBLE);

                }
                buttonIsProgress(false);

            }
            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                buttonIsProgress(false);
            }
        });
    }

    View.OnClickListener hintTextOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(mButton.getTag() == btn_tag_login){
                mButton.setTag(btn_tag_reg);
                mButton.setText(R.string.start_register);
                mtitle.setVisibility(View.INVISIBLE);
                mavatar.setVisibility(View.VISIBLE);
                mname.setVisibility(View.VISIBLE);
                mHintText.setText(R.string.login_hint_1);
            }else if(mButton.getTag() == btn_tag_reg){
                mButton.setTag(btn_tag_login);
                mButton.setText(R.string.start_login);
                mtitle.setVisibility(View.VISIBLE);
                mavatar.setVisibility(View.INVISIBLE);
                mname.setVisibility(View.GONE);
                mHintText.setText(R.string.login_hint_2);
            }
        }
    };

    /**
     *  Request system gallery intent
     * */
    private void openGallery() {
        CropImage.activity()
                .setFixAspectRatio(true)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1,1)
                .start(LoginActivity.this);
    }

    /**
     * Check the device isn't given permission to access the system storage
     * If not ask user for permission
     **/
    private void requestPermission() {
        if(ContextCompat.checkSelfPermission(LoginActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                // FIXME: If user cancel the permission , it will not open anything
                // Given message to user that accept required permission
                Toast.makeText(LoginActivity.this,
                        "Please accept the storage permission",
                        Toast.LENGTH_SHORT);
            }
            else
            {
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_CODE);
            }

        } else
            openGallery();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            avatarURI = result.getUri();
            mavatar.setImageURI(avatarURI);
        }
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
    private void buttonIsProgress(final boolean isProgress){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isProgress){
                    mButton.setVisibility(View.INVISIBLE);
                    mProgressBar.setVisibility(View.VISIBLE);
                }else{
                    mButton.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

    }
}
