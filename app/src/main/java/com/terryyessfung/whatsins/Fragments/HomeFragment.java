package com.terryyessfung.whatsins.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.terryyessfung.whatsins.Adapters.HomeRecyclerAdapter;
import com.terryyessfung.whatsins.Model.Row;
import com.terryyessfung.whatsins.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeFragment extends Fragment {
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
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
        list.add(new Row(R.drawable.aa));
        list.add(new Row(R.drawable.aa));
        list.add(new Row(R.drawable.aa));
        list.add(new Row(R.drawable.aa));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);
        // refresh Layout
        mRefreshLayout = view.findViewById(R.id.refreshLayout_home);
        mRefreshLayout.setOnRefreshListener(swipeOnRefresh);

        //Setting recycler view
        mRecyclerView = view.findViewById(R.id.recyclerView_home);
        // mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHomeRecyclerAdapter = new HomeRecyclerAdapter(getContext(),list);
        mRecyclerView.setAdapter(mHomeRecyclerAdapter);
        testFetchPost();
        return view;
    }

    // TODO Fetch the data from server
    public void fetchPost(){

        mHomeRecyclerAdapter.notifyDataSetChanged();
    }
    SwipeRefreshLayout.OnRefreshListener swipeOnRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            testFetchPost();
            mRefreshLayout.setRefreshing(false);
        }
    };

    public void testFetchPost(){
        Row[] rows = {
                new Row(R.drawable.aa),
                new Row(R.drawable.ee),
                new Row(R.drawable.ff),
                new Row(R.drawable.gg),
                new Row(R.drawable.bb),
                new Row(R.drawable.cc),
                new Row(R.drawable.dd),
                new Row(R.drawable.hh),
                new Row(R.drawable.aa),
                new Row(R.drawable.ee),
                new Row(R.drawable.ff),
                new Row(R.drawable.gg),
                new Row(R.drawable.bb),
                new Row(R.drawable.cc),
                new Row(R.drawable.dd),
                new Row(R.drawable.hh),
        };
        Random random = new Random();
        int r = random.nextInt(rows.length);
        Log.d("random",r+"");
        list.add(0,rows[r]);
        mHomeRecyclerAdapter.notifyDataSetChanged();

       // mHomeRecyclerAdapter.notifyItemRangeInserted(1,5);
    }
}
