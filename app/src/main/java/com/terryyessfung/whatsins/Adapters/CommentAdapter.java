package com.terryyessfung.whatsins.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.Activities.MainActivity;
import com.terryyessfung.whatsins.Model.Comment;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>  {
    private Context mContext;
    private List<Comment> mComments;

    private FirebaseUser mFirebaseUser;

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

        holder.comment_avatar.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_anim));
        holder.mRelativeLayout.setAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_transition_anim));

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Comment comment = mComments.get(position);

        holder.comment.setText(comment.getComment());
        getUserInfo(holder.comment_avatar, holder.usernname, comment.getPublisher());

        //TODO: onClick comment body
//        holder.comment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MainActivity.class);
//                intent.putExtra("publisherid", comment.getPublisher());
//                mContext.startActivity(intent);
//            }
//        });
        // TODO: onClick comment box avatar
//        holder.comment_avatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MainActivity.class);
//                intent.putExtra("publisherid", comment.getPublisher());
//                mContext.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView comment_avatar;
        public TextView usernname , comment;
        public RelativeLayout mRelativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            comment_avatar = itemView.findViewById(R.id.comment_box_avatar);
            usernname = itemView.findViewById(R.id.comment_title);
            comment = itemView.findViewById(R.id.comment_body);
            mRelativeLayout = itemView.findViewById(R.id.comment_relativelayout);
        }
    }
    private void getUserInfo(final ImageView avatar, final TextView username, String publisherid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(publisherid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getAvatar()).into(avatar);
                username.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
