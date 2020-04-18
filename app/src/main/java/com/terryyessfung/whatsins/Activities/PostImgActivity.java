package com.terryyessfung.whatsins.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.terryyessfung.whatsins.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostImgActivity extends AppCompatActivity {
    Uri imageUri;
    String myUri;
    StorageTask uploadTask;
    StorageReference mStorageReference;

    ImageView close,post_image;
    TextView post;
    EditText descript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_img);

        close = findViewById(R.id.post_close);
        post_image = findViewById(R.id.post_img);
        post = findViewById(R.id.post_txtpost);
        descript = findViewById(R.id.post_description);

        mStorageReference = FirebaseStorage.getInstance().getReference("posts");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostImgActivity.this,MainActivity.class));
                finish();
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImg();
            }
        });

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(PostImgActivity.this);
    }

    private void uploadImg() {
       if(imageUri != null){
           final StorageReference reference = mStorageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
           uploadTask = reference.putFile(imageUri);
           uploadTask.continueWithTask(new Continuation() {
               @Override
               public Object then(@NonNull Task task) throws Exception {
                   if(!task.isSuccessful()){
                       throw task.getException();
                   }
                   return reference.getDownloadUrl();
               }
           }).addOnCompleteListener(new OnCompleteListener<Uri>() {
               @Override
               public void onComplete(@NonNull Task<Uri> task) {
                   if(task.isSuccessful()){
                       Uri downUri = task.getResult();
                       myUri = downUri.toString();

                       DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

                       String postid = databaseReference.push().getKey();

                       HashMap<String,Object> hashMap = new HashMap<>();
                       hashMap.put("postID", postid);
                       hashMap.put("postImage",myUri);
                       //hashMap.put("description",descript.getText().toString());
                       hashMap.put("publisher", FirebaseAuth.getInstance().getCurrentUser().getUid());

                       databaseReference.child(postid).setValue(hashMap);

                       startActivity(new Intent(PostImgActivity.this, MainActivity.class));
                       finish();
                   }else{
                       Toast.makeText(PostImgActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                   }
               }
           });
       }else{
           Toast.makeText(this, "No image Selected", Toast.LENGTH_SHORT).show();
       }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            post_image.setImageURI(imageUri);

        }else{
            Toast.makeText(this," Something gone wrong", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostImgActivity.this,MainActivity.class));
            finish();
        }
    }
}
