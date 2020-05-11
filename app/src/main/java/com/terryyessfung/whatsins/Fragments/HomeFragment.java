package com.terryyessfung.whatsins.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.terryyessfung.whatsins.Adapters.HomeRecyclerAdapter;
import com.terryyessfung.whatsins.DB.DBManager;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.Model.PostsList;
import com.terryyessfung.whatsins.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private HomeRecyclerAdapter mHomeRecyclerAdapter;
    private List<Post> list;

    private static HomeFragment sFragment;

    public HomeFragment(){

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container,false);
        mRecyclerView = view.findViewById(R.id.recyclerView_home);
        mProgressBar = view.findViewById(R.id.home_progressBar);
        // mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(linearLayout);
        list = new ArrayList<>();
        mHomeRecyclerAdapter = new HomeRecyclerAdapter(getContext(),list);
        mRecyclerView.setAdapter(mHomeRecyclerAdapter);
        fetchPost();
        return view;
    }

    public void fetchPost(){
        Call<PostsList> call = DataManager.getInstance().getAPIService().fecthFollowingPosts(DBManager.getInstance(getContext()).getUid());
        call.enqueue(new Callback<PostsList>() {
            @Override
            public void onResponse(Call<PostsList> call, Response<PostsList> response) {
                if(response.isSuccessful()){
                    for (Post post : response.body().getPosts()){
                        list.add(post);
                    }
                    mHomeRecyclerAdapter.notifyDataSetChanged();
                    mProgressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<PostsList> call, Throwable t) {

            }
        });

    }
    SwipeRefreshLayout.OnRefreshListener swipeOnRefresh = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            mRefreshLayout.setRefreshing(true);
            mRefreshLayout.setRefreshing(false);
        }
    };
}
