package com.terryyessfung.whatsins.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;
import com.google.android.material.picker.MaterialDatePickerDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.DB.DBManager;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.Model.PostsList;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.R;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostsDetailActivity extends AppCompatActivity {
    public final static String POST_ID = "com.terryyessfung.whatsins.PostsDetailActivity.POST_ID";
    public final static String PUBLISHER_ID = "com.terryyessfung.whatsins.PostsDetailActivity.PUBLISHER_ID";
   private ImageView post_img,avator,close,more;
   private TextView username,desc,label;
//   private CheckBox like;
//   private CheckBox comment;
   private String muid;
    private String mDesc;
   private String postid;
   private String publisherId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_detail);
        muid = DBManager.getInstance(this).getUid();

        post_img = findViewById(R.id.post_detail_postImg);
        avator = findViewById(R.id.post_detail_avator);
        username = findViewById(R.id.post_detail_username);
//        like = findViewById(R.id.post_detail_fav);
//        comment = findViewById(R.id.post_detail_comment);
        desc = findViewById(R.id.post_detail_desc);
        close = findViewById(R.id.post_detail_close);
        more = findViewById(R.id.post_detail_more);
        label = findViewById(R.id.post_detail_label);

        final Intent intent = getIntent();
        postid = intent.getStringExtra(POST_ID);
        publisherId = intent.getStringExtra(PUBLISHER_ID);

        if(!publisherId.equals(DBManager.getInstance(this).getUid())){
            more.setVisibility(View.GONE);
        }

        getUser();
        getPost();

        avator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostsDetailActivity.this, ProfileActivity.class);
                intent.putExtra(ProfileActivity.UID,muid);
                intent.putExtra(ProfileActivity.CURRENT_UID,publisherId);
                startActivity(intent);

            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(getApplicationContext(),v);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.post_detail_more_edit:
                                editPost();
                                return true;
                            case R.id.post_detail_more_delete:
                                deletePost();
                                return true;
                        }
                        return false;
                    }
                });
                menu.inflate(R.menu.post_detail_more_menu);
                menu.show();
            }
        });



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void deletePost() {
        Call<ResponseBody> call = DataManager.getInstance().getAPIService().deletePostByPid(postid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void editPost() {
        // TODO: button color
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this,R.style.AlertDialogTheme);
        // TODO: make it to string key
        builder.setTitle(R.string.post_detail_title);
        final View customAlertDialog = getLayoutInflater().inflate(R.layout.edit_alert_dialog, null);
        builder.setView(customAlertDialog);
        final EditText editText = customAlertDialog.findViewById(R.id.edit_alert_dialog);
        editText.setText(mDesc);

        builder.setPositiveButton(R.string.post_detail_positive_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HashMap<String,String> map = new HashMap<>();
                map.put("desc",editText.getText().toString());
                Call<ResponseBody> call = DataManager.getInstance()
                        .getAPIService().updatePostDesc(postid,map);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            restartActivity();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        builder.setNeutralButton(R.string.post_detail_neutralBtn, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    private void getPost() {
        Call<Post> call = DataManager.getInstance().getAPIService().getPostByPId(postid);
        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(response.code()== 200){
                    Post post = response.body();
                    Picasso.get().load(post.getImage()).placeholder(R.drawable.placeholder).into(post_img);
                    desc.setText(post.getDesc());
                    mDesc = post.getDesc();
                    label.setText(post.getLabel());

                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });

    }

    private void restartActivity() {
        Intent intent = getIntent();
        intent.putExtra(POST_ID,postid);
        intent.putExtra(PUBLISHER_ID,publisherId);
        finish();
        startActivity(intent);
    }

    private void getUser() {
        Call<User> call = DataManager.getInstance().getAPIService().getUserByID(publisherId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    Picasso.get().load(user.getAvatar()).into(avator);
                    user.setName(user.getName());
                    username.setText(user.getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
