package com.terryyessfung.whatsins.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.Activities.CommentActivity;
import com.terryyessfung.whatsins.Activities.PostsDetailActivity;
import com.terryyessfung.whatsins.Activities.ProfileActivity;
import com.terryyessfung.whatsins.DB.DBManager;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.R;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {
    Context mContext;
    List<Post> mdata;
    String muid;

    public HomeRecyclerAdapter(Context context, List<Post> mdata) {
        mContext = context;
        this.mdata = mdata;
        muid = DBManager.getInstance(context).getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Post post = mdata.get(position);
        Picasso.get().load(post.getImage())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.placeholder).into(holder.post_img);

        // show user info - avatar and name
        publisherInfo(holder.avator,holder.username, post.getPublisher());
        // Check photo like or not
        isLike(post.getLike(),holder.like);

        holder.avator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra(ProfileActivity.UID,muid);
                intent.putExtra(ProfileActivity.CURRENT_UID,post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostsDetailActivity.class);
                intent.putExtra(PostsDetailActivity.POST_ID,post.get_id());
                intent.putExtra(PostsDetailActivity.PUBLISHER_ID,post.getPublisher());
                mContext.startActivity(intent);
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.like.isChecked()) {
                    // if unlike
                    HashMap<String,String> map = new HashMap<>();
                    map.put("pid", post.get_id());
                    map.put("isLike", "false");
                    Call<ResponseBody>call = DataManager.getInstance().getAPIService().updatePostsLike(muid,map);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                Log.d("like",response.message());
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });

                }else{
                    //  if click like
                    Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.like_amin);
                    v.startAnimation(animation);
                    HashMap<String,String> map = new HashMap<>();
                    map.put("pid", post.get_id());
                    map.put("isLike", "true");
                    Call<ResponseBody>call = DataManager.getInstance().getAPIService().updatePostsLike(muid,map);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful()){
                                Log.d("like",response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            holder.like.setChecked(false);
                        }
                    });
                }
            }
        });

        // comment checkbox
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra(CommentActivity.POST_ID,post.get_id());
                intent.putExtra(CommentActivity.PUBLISHER_ID,post.getPublisher());
                mContext.startActivity(intent);
                holder.comment.setChecked(false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView post_img,avator;
        TextView username;
        CheckBox like;
        CheckBox comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            post_img = itemView.findViewById(R.id.post_detail_postImg);
            avator = itemView.findViewById(R.id.post_detail_avator);
            username = itemView.findViewById(R.id.post_detail_username);
            like = itemView.findViewById(R.id.post_detail_fav);
            comment = itemView.findViewById(R.id.post_detail_comment);
        }
    }

    private  void addNotifications(String userId, String postId){
        // TODO : notify user click your photo
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userId);
//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("userid", mAuth.getUid());
//        hashMap.put("message", "like you post");
//        hashMap.put("postid", postId);
//        hashMap.put("ispost", true);
//
//        reference.push().setValue(hashMap);
    }

    /**
     * Check the like list isn't include the current user id
     * if yes , that means the photo is liked
     * **/
    private void isLike(String[] likeList, final CheckBox like){
        if(likeList != null){
            for(String id: likeList){
                if(id.equals(muid)){
                    like.setChecked(true);
                    return;
                }
            }
        }
        like.setChecked(false);
    }

    /**
     * To Get the publisher avatar , name
     * **/
    private void publisherInfo(final ImageView avatar, final TextView publisher, String userId ){
        Call<User> call = DataManager.getInstance().getAPIService().getUserByID(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    Picasso.get().load(user.getAvatar()).into(avatar);
                    user.setName(user.getName());
                    publisher.setText(user.getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
