package com.terryyessfung.whatsins.Activities;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceImageLabelerOptions;
import com.terryyessfung.whatsins.DB.DBManager;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostImgActivity extends AppCompatActivity {
    private final String TAG_PostImg = "PostImgActivity";
   private Uri imageUri;

   private ImageView close,post_image;
   private TextView post,desc_num,image_label;
   private EditText descript;
   private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_img);

        close = findViewById(R.id.post_close);
        post_image = findViewById(R.id.post_img);
        post = findViewById(R.id.post_txtpost);
        descript = findViewById(R.id.post_description);
        desc_num = findViewById(R.id.post_img_desc_num);
        image_label = findViewById(R.id.post_img_label);
        mProgressBar = findViewById(R.id.post_img_progressBar);

        close.setOnClickListener(closeBtnOnClick);

        post.setOnClickListener(postBtnOnClick);

        descript.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               int i =  descript.getText().length();
               desc_num.setText(i+"/20");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropImage();
            }
        });

       cropImage();
    }

    View.OnClickListener closeBtnOnClick =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            closeActivity();
        }
    };
   View.OnClickListener postBtnOnClick =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            post.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            uploadImg(DBManager.getInstance(getApplicationContext()).getUid(),
                    descript.getText().toString());
        }
    };

   private void cropImage (){
       CropImage.activity()
               .start(PostImgActivity.this);
   }

   /**
    * Upload image to server side
    * **/
    private void uploadImg(String uid, String description) {
       if(imageUri != null){
           File img = new File(imageUri.getPath());
           Log.d(TAG_PostImg,img.getName());
           RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),img);
           RequestBody publisher = RequestBody.create(null,uid);
           RequestBody desc = RequestBody.create(null,description);
           RequestBody label = RequestBody.create(null,image_label.getText().toString());
           MultipartBody.Part imgBody = MultipartBody.Part.createFormData("post",img.getName(),requestBody);
           Call<ResponseBody> call = DataManager.getInstance().getAPIService().postImage(publisher,desc,label,imgBody);
           call.enqueue(new Callback<ResponseBody>() {
               @Override
               public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                   if(response.isSuccessful() && response.code() == 201){
                       Log.d(TAG_PostImg,response.message());
                       closeActivity();
                   }
                   post.setVisibility(View.VISIBLE);
                   mProgressBar.setVisibility(View.INVISIBLE);
               }
               @Override
               public void onFailure(Call<ResponseBody> call, Throwable t) {
                   post.setVisibility(View.VISIBLE);
                   mProgressBar.setVisibility(View.INVISIBLE);
               }
           });
       }else{
           Toast.makeText(this, R.string.post_img_wrong, Toast.LENGTH_SHORT).show();
           mProgressBar.setVisibility(View.INVISIBLE);
           post.setVisibility(View.VISIBLE);
       }
    }

    private void closeActivity(){
        startActivity(new Intent(PostImgActivity.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            post_image.setImageURI(imageUri);
            image_label.setText("");
            // fire base label
            try {
                FirebaseVisionImage image = FirebaseVisionImage.fromFilePath(this, result.getUri());
                FirebaseVisionOnDeviceImageLabelerOptions options =
                        new FirebaseVisionOnDeviceImageLabelerOptions.Builder().setConfidenceThreshold(0.7f).build();
                FirebaseVisionImageLabeler labeler = FirebaseVision.getInstance().getOnDeviceImageLabeler(options);
                labeler.processImage(image).addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionImageLabel> firebaseVisionImageLabels) {
                        String message = "";
                        image_label.setText(message);
                        int i = 0; // label limit
                        for (FirebaseVisionImageLabel label : firebaseVisionImageLabels) {
                            if (i >= 5) break;
                            message += "#" + label.getText() + " ";
                            i++;
                        }
                        image_label.setText(message);
                    }
                });
            } catch (IOException e) {
                Log.d(TAG_PostImg, e.getMessage());
            }

        }
    }
}
