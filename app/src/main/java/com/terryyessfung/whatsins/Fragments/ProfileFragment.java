package com.terryyessfung.whatsins.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.Activities.OptionActivity;
import com.terryyessfung.whatsins.Adapters.ProfilePostImgAdapter;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class ProfileFragment extends Fragment {

    ImageView mImage_avatar,mbtn_options;
    TextView mnum_posts,mnum_followers,mnum_following,mbio,musername;
    Button medit_profile;

    RecyclerView mRecyclerView;
    ProfilePostImgAdapter mProfilePostImgAdapter;
    List<Post> mPostImgList;

    FirebaseUser mFirebaseUser;
    String profileid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileID", "none");

        mImage_avatar = view.findViewById(R.id.profile_avatar);
        mbtn_options = view.findViewById(R.id.profile_btn_options);
        mnum_posts = view.findViewById(R.id.profile_num_posts);
        mnum_followers = view.findViewById(R.id.profile_num_followers);
        mnum_following = view.findViewById(R.id.profile_num_following);
        //mbio = view.findViewById(R.id.);
        musername = view.findViewById(R.id.profile_username);
        medit_profile = view.findViewById(R.id.profile_edit_profile);

        mRecyclerView = view.findViewById(R.id.profile_post_img_list);
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPostImgList = new ArrayList<>();
        mProfilePostImgAdapter = new ProfilePostImgAdapter(getContext(),mPostImgList);
        mRecyclerView.setAdapter(mProfilePostImgAdapter);

        userInfo();
        getFollowers();
        getNumOfPost();
        fetchPostImgeList();

        if (profileid.equals(mFirebaseUser.getUid())){
            medit_profile.setText("Edit Profile");
        }else {
            checkFollow();
        }

        medit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 String btn = medit_profile.getText().toString();

                 if(btn.equals("Edit Profile")){

                 }else if(btn.equals("follow")){
                     FirebaseDatabase.getInstance().getReference().child("Follow").child(mFirebaseUser.getUid())
                             .child("following").child(profileid).setValue(true);
                     FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                             .child("followers").child(mFirebaseUser.getUid()).setValue(true);
                     // add notification to user
                     addNotifications();
                 }else if(btn.equals("following")){
                     FirebaseDatabase.getInstance().getReference().child("Follow").child(mFirebaseUser.getUid())
                             .child("following").child(profileid).removeValue();
                     FirebaseDatabase.getInstance().getReference().child("Follow").child(profileid)
                             .child("followers").child(mFirebaseUser.getUid()).removeValue();


                 }
            }
        });

        mbtn_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OptionActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }

    private  void addNotifications(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(profileid);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userid", mFirebaseUser.getUid());
        hashMap.put("message", "started follow you");
        hashMap.put("postid", "");
        hashMap.put("ispost", false);

        reference.push().setValue(hashMap);
    }

    private void userInfo(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(getContext() == null){
                    return;
                }
                User user = dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getAvatar()).into(mImage_avatar);
                musername.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkFollow(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follows").child(mFirebaseUser.getUid()).child("following");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(profileid).exists()){
                    medit_profile.setText("following");
                }else{
                    medit_profile.setText("follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getFollowers(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("followers");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mnum_followers.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(profileid).child("following");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mnum_following.setText("" + dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getNumOfPost(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if(post.getPublisher().equals(profileid)){
                        i++;
                    }
                }
                mnum_posts.setText("" + i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchPostImgeList(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPostImgList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Post post = snapshot.getValue(Post.class);
                    if(post.getPublisher().equals(profileid)){
                        mPostImgList.add(post);
                    }
                }
                Collections.reverse(mPostImgList);
                mProfilePostImgAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
