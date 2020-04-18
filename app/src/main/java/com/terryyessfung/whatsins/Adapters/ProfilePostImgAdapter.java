package com.terryyessfung.whatsins.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.Fragments.PostDetailFragment;
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Post post = mPostList.get(position);
        Picasso.get().load(post.getPostImage()).placeholder(R.drawable.placeholder).into(holder.post_img);

        holder.post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("postID", post.getPostID());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, new PostDetailFragment()).commit();
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
