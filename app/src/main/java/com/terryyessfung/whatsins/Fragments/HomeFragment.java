package com.terryyessfung.whatsins.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.terryyessfung.whatsins.Adapters.HomeRecyclerAdapter;
import com.terryyessfung.whatsins.Model.Row;
import com.terryyessfung.whatsins.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView staggeredRvView;
    private HomeRecyclerAdapter mHomeRecyclerAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    List<Row> list;


    public HomeFragment(){

    }

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<>();
        list.add(new Row(R.drawable.aa));
        list.add(new Row(R.drawable.ee));
        list.add(new Row(R.drawable.ff));
        list.add(new Row(R.drawable.gg));
        list.add(new Row(R.drawable.bb));
        list.add(new Row(R.drawable.cc));
        list.add(new Row(R.drawable.dd));
        list.add(new Row(R.drawable.hh));
        list.add(new Row(R.drawable.aa));
        list.add(new Row(R.drawable.ee));
        list.add(new Row(R.drawable.ff));
        list.add(new Row(R.drawable.gg));
        list.add(new Row(R.drawable.bb));
        list.add(new Row(R.drawable.cc));
        list.add(new Row(R.drawable.dd));
        list.add(new Row(R.drawable.hh));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);
        staggeredRvView = view.findViewById(R.id.recyclerView_home);

        fetchPost();
        return view;
    }

    public void fetchPost(){
       // mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        staggeredRvView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHomeRecyclerAdapter = new HomeRecyclerAdapter(getContext(),list);
        staggeredRvView.setAdapter(mHomeRecyclerAdapter);
    }
}
