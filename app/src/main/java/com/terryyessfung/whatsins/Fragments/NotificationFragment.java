package com.terryyessfung.whatsins.Fragments;

import android.content.Context;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.terryyessfung.whatsins.Adapters.NotificationAdapter;
import com.terryyessfung.whatsins.Model.NotifyItem;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private NotificationAdapter mAdapter;
    private List<NotifyItem> mNotifyItemList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        mRecyclerView = view.findViewById(R.id.notification_recyc);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNotifyItemList = new ArrayList<>();
        mAdapter = new NotificationAdapter(getContext(),mNotifyItemList);
        mRecyclerView.setAdapter(mAdapter);
        
        readNotifyItem();
        
        return view;
    }

    private void readNotifyItem() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mNotifyItemList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    NotifyItem notifyItem = dataSnapshot.getValue(NotifyItem.class);
                    mNotifyItemList.add(notifyItem);
                }
                Collections.reverse(mNotifyItemList);
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
