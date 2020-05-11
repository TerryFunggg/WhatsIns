package com.terryyessfung.whatsins.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.Fragments.PostDetailFragment;
import com.terryyessfung.whatsins.Model.NotifyItem;
import com.terryyessfung.whatsins.Model.Post;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.R;

import java.util.List;

//public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{
//    private Context mContext;
//    private List<NotifyItem> mNotifyItems;
//
//    public NotificationAdapter(Context context, List<NotifyItem> notifyItems) {
//        mContext = context;
//        mNotifyItems = notifyItems;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(mContext).inflate(R.layout.notification_item,parent,false);
//        return new NotificationAdapter.ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        final NotifyItem notifyItem = mNotifyItems.get(position);
//        holder.postImage.setVisibility(View.INVISIBLE);
//        holder.message.setText(notifyItem.getMessage());
//        getUserInfo(holder.postImage,holder.username,notifyItem.getUserid());
//        if(notifyItem.isIspost()){
//            holder.postImage.setVisibility(View.VISIBLE);
//            getPostImage(holder.postImage,notifyItem.getPostid());
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(notifyItem.isIspost()){
//                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
//                    editor.putString("postID", notifyItem.getPostid());
//                    editor.apply();
//                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.main_container,new PostDetailFragment()).commit();
//
//                }else{
//                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
//                    editor.putString("profileID", notifyItem.getUserid());
//                    editor.apply();
//                    ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.main_container,new PostDetailFragment()).commit();
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return mNotifyItems.size();
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//        public ImageView avatar,postImage;
//        public TextView username, message;
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            avatar = itemView.findViewById(R.id.notification_item_avatar);
//            postImage = itemView.findViewById(R.id.notification_post_img);
//            username = itemView.findViewById(R.id.notification_item_username);
//            message = itemView.findViewById(R.id.notification_item_message);
//        }
//    }
//    private void getUserInfo(final ImageView imageView, final TextView username, String publishid){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(publishid);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user = dataSnapshot.getValue(User.class);
//                Picasso.get().load(user.getAvatar()).into(imageView);
//                username.setText(user.getName());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void getPostImage(final ImageView imageView,String postid){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postid);
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Post post = dataSnapshot.getValue(Post.class);
//                Picasso.get().load(post.getImage()).into(imageView);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//}
