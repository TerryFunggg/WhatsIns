package com.terryyessfung.whatsins.Adapters;

import android.content.Context;
import android.content.Intent;
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

public class SquareRecyclerAdapter extends RecyclerView.Adapter<SquareRecyclerAdapter.ImageViewHolder> {
    Context mContext;
    List<Post> mdata;

    public SquareRecyclerAdapter(Context context, List<Post> mdata) {
        mContext = context;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.square_row_item,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        // bind image here
        Picasso.get().load(mdata.get(position).getImage())
                .fit()
                .centerInside()
                //.centerCrop()
                .placeholder(R.drawable.placeholder)
                .into(holder.img);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostsDetailActivity.class);
                intent.putExtra(PostsDetailActivity.POST_ID,mdata.get(position).get_id());
                intent.putExtra(PostsDetailActivity.PUBLISHER_ID,mdata.get(position).getPublisher());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.square_row_img);
        }
    }
}
