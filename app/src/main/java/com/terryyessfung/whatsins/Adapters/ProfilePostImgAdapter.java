package com.terryyessfung.whatsins.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.Activities.PostsDetailActivity;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.R;

import java.util.List;

public class ProfilePostImgAdapter extends RecyclerView.Adapter<ProfilePostImgAdapter.ViewHolder> {
    private Context mContext;
    private List<Post> mPostList;

    public ProfilePostImgAdapter(Context context, List<Post> postList) {
        mContext = context;
        mPostList = postList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.profile_post_row_item, parent,false);
        return new ProfilePostImgAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Post post = mPostList.get(position);
        Picasso.get().load(post.getImage())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.post_img);

        holder.post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostsDetailActivity.class);
                intent.putExtra(PostsDetailActivity.POST_ID,mPostList.get(position).get_id());
                intent.putExtra(PostsDetailActivity.PUBLISHER_ID,mPostList.get(position).getPublisher());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPostList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public  ImageView post_img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_img = itemView.findViewById(R.id.profile_post_image);
        }
    }
}
