package com.terryyessfung.whatsins.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.terryyessfung.whatsins.Activities.PostImgActivity;
import com.terryyessfung.whatsins.Adapters.HomeRecyclerAdapter;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.R;

import java.util.ArrayList;
import java.util.List;


public class PostDetailFragment extends Fragment {
    String postId;
    private RecyclerView mRecyclerView;
    private HomeRecyclerAdapter mAdapter;
    private List<Post> mPostList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postId = preferences.getString("postID", "none");

        mRecyclerView = view.findViewById(R.id.post_detail_recyc);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayout);

        mPostList = new ArrayList<>();
        mAdapter = new HomeRecyclerAdapter(getContext(),mPostList);
        mRecyclerView.setAdapter(mAdapter);

        readPost();

        return view;
    }

    private void readPost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mPostList.clear();
                Post post = dataSnapshot.getValue(Post.class);
                mPostList.add(post);

                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
