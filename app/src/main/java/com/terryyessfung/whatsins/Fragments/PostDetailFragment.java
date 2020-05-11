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

import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.R;

import java.util.List;


public class PostDetailFragment extends Fragment {
    String postId;

    private List<Post> mPostList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_detail, container, false);
        SharedPreferences preferences = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        postId = preferences.getString("postID", "none");



        readPost();

        return view;
    }

    private void readPost() {
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                mPostList.clear();
//                Post post = dataSnapshot.getValue(Post.class);
//                mPostList.add(post);
//
//                mAdapter.notifyDataSetChanged();
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

}
