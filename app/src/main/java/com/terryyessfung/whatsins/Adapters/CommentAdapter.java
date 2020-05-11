package com.terryyessfung.whatsins.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.Helper;
import com.terryyessfung.whatsins.Model.Comment;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.R;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>  {
    private Context mContext;
    private List<Comment> mComments;

    public CommentAdapter(Context context, List<Comment> comments) {
        mContext = context;
        mComments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_row_item, parent,false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Comment comment = mComments.get(position);
        Log.d("Comment",comment.getCreatedAt());
        holder.date.setText(Helper.getInstance().formatDate(comment.getCreatedAt()));
        holder.comment.setText(comment.getComment());
        getUserInfo(holder.comment_avatar, holder.usernname, comment.getPublisher());
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView comment_avatar;
        public TextView usernname , comment,date;
        public RelativeLayout mRelativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_avatar = itemView.findViewById(R.id.comment_box_avatar);
            usernname = itemView.findViewById(R.id.comment_title);
            comment = itemView.findViewById(R.id.comment_body);
            mRelativeLayout = itemView.findViewById(R.id.comment_relativelayout);
            date = itemView.findViewById(R.id.comment_date);
        }
    }
    private void getUserInfo(final ImageView avatar, final TextView username, String publisherid){
        Call<User> call = DataManager.getInstance().getAPIService().getUserByID(publisherid);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful()){
                    User user = response.body();
                    Picasso.get().load(user.getAvatar()).into(avatar);
                    username.setText(user.getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }
}
