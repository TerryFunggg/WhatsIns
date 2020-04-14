package com.terryyessfung.whatsins.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import com.terryyessfung.whatsins.Adapters.HomeRecyclerAdapter;
import com.terryyessfung.whatsins.Model.Row;
import com.terryyessfung.whatsins.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView staggeredRvView;
    private HomeRecyclerAdapter mHomeRecyclerAdapter;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        staggeredRvView = findViewById(R.id.recyclerView_home);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL);
        staggeredRvView.setLayoutManager(mStaggeredGridLayoutManager);

        List<Row> list = new ArrayList<>();
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

        mHomeRecyclerAdapter = new HomeRecyclerAdapter(this,list);
        staggeredRvView.setAdapter(mHomeRecyclerAdapter);

    }
}
