package com.terryyessfung.whatsins.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.Adapters.ProfilePostImgAdapter;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.Model.PostsList;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    public final static String UID = "com.terryyessfung.whatsins.ProfileActivities.UID";
    public final static String CURRENT_UID = "com.terryyessfung.whatsins.ProfileActivities.CURRENT_UID";

    ImageView mImage_avatar,mbtn_options;
    TextView mnum_posts,mnum_followers,mnum_following,musername;
    Button medit_profile;

    RecyclerView mRecyclerView;
    ProfilePostImgAdapter mProfilePostImgAdapter;
    List<Post> mPostImgList;

    private String mUid;
    private String mCurrentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        Intent intent = getIntent();
        mUid = intent.getStringExtra(UID);
        mCurrentUid = intent.getStringExtra(CURRENT_UID);

        mImage_avatar = findViewById(R.id.profile_avatar);
        mbtn_options = findViewById(R.id.profile_btn_options);
        mbtn_options.setVisibility(View.GONE);
        mnum_posts = findViewById(R.id.profile_num_posts);
        mnum_followers = findViewById(R.id.profile_num_followers);
        mnum_following = findViewById(R.id.profile_num_following);
        musername = findViewById(R.id.profile_username);
        medit_profile = findViewById(R.id.profile_edit_profile);

        mRecyclerView = findViewById(R.id.profile_post_img_list);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPostImgList = new ArrayList<>();
        mProfilePostImgAdapter = new ProfilePostImgAdapter(this,mPostImgList);
        mRecyclerView.setAdapter(mProfilePostImgAdapter);

        userInfo();
        fetchPostImgeList();
        medit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(medit_profile.getText().toString().equals("follow")){
                    setToFollowing(mUid, mCurrentUid, medit_profile);
                }else{
                    removeFromFollowing(mUid,mCurrentUid, medit_profile);
                }
            }
        });
    }

    /**
     * When an user click the following buttons, that means user want to unfollow the user
     * **/
    private void removeFromFollowing(String uid, String fid, final Button button) {
        Call<ResponseBody> call = DataManager.getInstance().getAPIService().removeFollowing(uid,fid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 201){
                    button.setText("follow");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setToFollowing(String uid, String fid, final Button button) {
        HashMap<String,String> map = new HashMap<>();
        map.put("fid",fid);
        Call<ResponseBody> call = DataManager.getInstance().getAPIService().updateFollowing(uid,map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 201){
                    button.setText("following");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void userInfo(){
        Call<User> call =  DataManager.getInstance().getAPIService().getUserByID(mCurrentUid);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.code() == 200){
                    User user = response.body();
                    Picasso.get()
                            .load(user.getAvatar())
                            .resize(90,90)
                            .placeholder(R.drawable.placeholder)
                            .into(mImage_avatar);
                    musername.setText(user.getName());
                    mnum_posts.setText(user.getPost_num()+"");
                    mnum_followers.setText(user.getFollowers_num()+"");
                    mnum_following.setText(user.getFollowing_num()+"");
                    isFollowing(user.getFollowers(),medit_profile);

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    // Check  item user followers list isn't included current user
    private void isFollowing(final String[] followers , final Button button){
        button.setText("follow");
        if(followers != null) {
            for (String uid : followers) {
                if (uid.equals(mUid)) {
                    button.setText("following");
                    return;
                }
            }
        }

    }

    private void fetchPostImgeList(){
        Call<PostsList> call = DataManager.getInstance().getAPIService().getPostsByUid(mCurrentUid);
        call.enqueue(new Callback<PostsList>() {
            @Override
            public void onResponse(Call<PostsList> call, Response<PostsList> response) {
                if(response.isSuccessful()){
                    PostsList lists = response.body();
                    for(Post post : lists.getPosts()){
                        mPostImgList.add(post);
                    }
                }
                Collections.reverse(mPostImgList);
                mProfilePostImgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PostsList> call, Throwable t) {

            }
        });
    }
}
