package com.terryyessfung.whatsins.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.shape.CornerTreatment;
import com.google.android.material.shape.RoundedCornerTreatment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.Activities.CommentActivity;
import com.terryyessfung.whatsins.Fragments.PostDetailFragment;
import com.terryyessfung.whatsins.Fragments.ProfileFragment;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.R;

import java.util.HashMap;
import java.util.List;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {
    Context mContext;
    List<Post> mdata;

    private FirebaseUser mAuth;

    public HomeRecyclerAdapter(Context context, List<Post> mdata) {
        mContext = context;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_row_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        mAuth = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mdata.get(position);

//        if(post.getDescription.equals("")){
//            holder.description.setVisiility(View.GONE);
//        }else{
//            holder.description.setVisibility(View.VISIBLE);
//            holder.description.setText(post.getdescription);
//        }

        Picasso.get().load(post.getPostImage()).placeholder(R.drawable.placeholder).into(holder.post_img);
        publisherInfo(holder.avator,holder.username, post.getPublisher());
        isLike(post.getPostID(),holder.like);

        holder.avator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                editor.putString("profileID", post.getPublisher());
                editor.apply();

                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_container, new ProfileFragment()).commit();
            }
        });

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

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.like.isChecked()) {

                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostID())
                            .child(mAuth.getUid()).removeValue();
                    addNotifications(post.getPublisher(), post.getPostID());
                }else{
                    Animation animation = AnimationUtils.loadAnimation(v.getContext(), R.anim.like_amin);
                    v.startAnimation(animation);
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(post.getPostID())
                            .child(mAuth.getUid()).setValue(true);
                }
            }
        });

        // comment checkbox
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra(CommentActivity.POST_ID,post.getPostID());
                intent.putExtra(CommentActivity.PUBLISHER_ID,post.getPublisher());
                mContext.startActivity(intent);
                holder.comment.setChecked(false);
            }
        });

        // binding data
        //holder.post_img.setImageResource(mdata.get(position).getImg());
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
            post_img = itemView.findViewById(R.id.row_postImg);
            avator = itemView.findViewById(R.id.row_avator);
            username = itemView.findViewById(R.id.row_username);
            like = itemView.findViewById(R.id.row_fav);
            comment = itemView.findViewById(R.id.row_comment);
//            like.setOnClickListener(likeOnClick);
//            comment.setOnClickListener(commentOnClick);
        }

//        View.OnClickListener likeOnClick = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Animation animation = AnimationUtils.loadAnimation(v.getContext(),R.anim.like_amin);
//                v.startAnimation(animation);
//                FirebaseDatabase.getInstance().getReference().child("Likes").child(post.get)
//            }
//        };

//        View.OnClickListener commentOnClick = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Animation animation = AnimationUtils.loadAnimation(v.getContext(),R.anim.like_amin);
//                v.startAnimation(animation);
//            }
//        };
    }

    private  void addNotifications(String userId, String postId){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userId);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("userid", mAuth.getUid());
        hashMap.put("message", "like you post");
        hashMap.put("postid", postId);
        hashMap.put("ispost", true);

        reference.push().setValue(hashMap);
    }

    private void isLike(String postid, final CheckBox like){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Likes")
                .child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(firebaseUser.getUid()).exists()){
                    like.setChecked(true);
                }else{
                    like.setChecked(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void publisherInfo(final ImageView avatar, final TextView publisher, String userId ){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getAvatar()).into(avatar);
                user.setUsername(user.getUsername());
                publisher.setText(user.getUsername());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
