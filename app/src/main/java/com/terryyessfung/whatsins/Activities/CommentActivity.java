package com.terryyessfung.whatsins.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.appbar.MaterialToolbar;
import com.terryyessfung.whatsins.Adapters.CommentAdapter;
import com.terryyessfung.whatsins.DB.DBManager;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.Model.Comment;
import com.terryyessfung.whatsins.Model.CommentList;
import com.terryyessfung.whatsins.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    public final static String POST_ID = "com.terryyessfung.whatsins.CommentActivities.POST_ID";
    public final static String PUBLISHER_ID = "com.terryyessfung.whatsins.CommentActivities.PUBLISHER_ID";

    private RecyclerView mRecyclerView;
    private CommentAdapter mCommentAdapter;
    private List<Comment> mCommentList;

    private EditText addComment;
    private TextView postbtn;

    String postid; // current post id
    String publisherId; // user id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        // Tools bar
        MaterialToolbar toolbar = findViewById(R.id.comment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Comments");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //recycler View
        mRecyclerView = findViewById(R.id.comment_rcyc);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mCommentList = new ArrayList<>();
        mCommentAdapter = new CommentAdapter(this,mCommentList);
        mRecyclerView.setAdapter(mCommentAdapter);

        addComment = findViewById(R.id.comment_etbox);
        postbtn = findViewById(R.id.comment_post);

        Intent intent = getIntent();
        postid = intent.getStringExtra(POST_ID);
        publisherId = intent.getStringExtra(PUBLISHER_ID);
        
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addComment.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this , "empty string",Toast.LENGTH_SHORT).show();
                }else{
                    addComments();
                }
            }
        });
        getComment();
    }

    private void addComments() {
        HashMap<String,String> map = new HashMap<>();
        map.put("uid", DBManager.getInstance(this).getUid());
        map.put("pid",postid);
        map.put("comment",addComment.getText().toString());
        Call<ResponseBody> call = DataManager.getInstance().getAPIService().addComment(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    addComment.setText("");
                    getComment();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    private void getComment(){
        Call<CommentList> call = DataManager.getInstance().getAPIService().getCommentByPostId(postid);
        call.enqueue(new Callback<CommentList>() {
            @Override
            public void onResponse(Call<CommentList> call, Response<CommentList> response) {
                if(response.isSuccessful()){
                    mCommentList.clear();
                    for(Comment comment : response.body().getComments()){
                        mCommentList.add(comment);
                    }
                    mCommentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CommentList> call, Throwable t) {

            }
        });
    }
}
