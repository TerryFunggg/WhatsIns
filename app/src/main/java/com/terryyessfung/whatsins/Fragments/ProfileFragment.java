package com.terryyessfung.whatsins.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.Activities.MainActivity;
import com.terryyessfung.whatsins.Activities.StartActivity;
import com.terryyessfung.whatsins.Adapters.ProfilePostImgAdapter;
import com.terryyessfung.whatsins.DB.DBManager;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.Model.PostsList;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    ImageView mImage_avatar,mbtn_options;
    TextView mnum_posts,mnum_followers,mnum_following,musername;
    Button medit_profile;

    RecyclerView mRecyclerView;
    ProfilePostImgAdapter mProfilePostImgAdapter;
    List<Post> mPostImgList;

    private String mUid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mUid = DBManager.getInstance(getContext()).getUid();

        mImage_avatar = view.findViewById(R.id.profile_avatar);
        mbtn_options = view.findViewById(R.id.profile_btn_options);
        mnum_posts = view.findViewById(R.id.profile_num_posts);
        mnum_followers = view.findViewById(R.id.profile_num_followers);
        mnum_following = view.findViewById(R.id.profile_num_following);
        musername = view.findViewById(R.id.profile_username);
        medit_profile = view.findViewById(R.id.profile_edit_profile);
        medit_profile.setVisibility(View.GONE);

        mRecyclerView = view.findViewById(R.id.profile_post_img_list);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPostImgList = new ArrayList<>();
        mProfilePostImgAdapter = new ProfilePostImgAdapter(getContext(),mPostImgList);
        mRecyclerView.setAdapter(mProfilePostImgAdapter);
        medit_profile.setText(R.string.profile_edit_profile_btn);

        userInfo();
        fetchPostImgeList();

        // edit profile
        medit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Edit profile

            }
        });

        mbtn_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu menu = new PopupMenu(getContext(),v);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.profile_menu_logout:
                                DBManager.getInstance(getContext()).deleteUserToken(mUid);
                                startActivity(new Intent(getActivity(), StartActivity.class));
                                getActivity().finish();
                                return true;

                        }
                        return false;
                    }
                });
                menu.inflate(R.menu.profile_menu);
                menu.show();
            }
        });


        return view;
    }




    private void userInfo(){
        Call<User> call =  DataManager.getInstance().getAPIService().getUserByID(mUid);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.code() == 200){
                   User user = response.body();
                    Log.d("ProfileFragment", user.getAvatar());
                   Picasso.get()
                           .load(user.getAvatar())
                           .resize(90,90)
                           .placeholder(R.drawable.placeholder)
                           .into(mImage_avatar);
                   musername.setText(user.getName());
                   mnum_posts.setText(user.getPost_num()+"");
                   mnum_followers.setText(user.getFollowers_num()+"");
                   mnum_following.setText(user.getFollowing_num()+"");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    private void fetchPostImgeList(){
        Log.d("ProfileFragment","fetch image");
        mPostImgList.clear();
        mProfilePostImgAdapter.notifyDataSetChanged();
        Call<PostsList> call = DataManager.getInstance().getAPIService().getPostsByUid(mUid);
        call.enqueue(new Callback<PostsList>() {
            @Override
            public void onResponse(Call<PostsList> call, Response<PostsList> response) {
                mPostImgList.clear();
                mProfilePostImgAdapter.notifyDataSetChanged();
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

    @Override
    public void onResume() {
        super.onResume();
        userInfo();
        fetchPostImgeList();
    }
}
