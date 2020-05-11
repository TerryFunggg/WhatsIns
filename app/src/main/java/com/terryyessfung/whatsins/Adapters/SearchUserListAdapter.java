package com.terryyessfung.whatsins.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.terryyessfung.whatsins.Activities.ProfileActivity;
import com.terryyessfung.whatsins.DB.DBManager;
import com.terryyessfung.whatsins.DataManager;
import com.terryyessfung.whatsins.Model.User;
import com.terryyessfung.whatsins.R;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUserListAdapter extends RecyclerView.Adapter<SearchUserListAdapter.ViewHolder> {
    private Context mContext;
    private List<User> mUserList;
    private String muid;

    public SearchUserListAdapter(Context context, List<User> userList) {
        mContext = context;
        mUserList = userList;
        muid = DBManager.getInstance(context).getUid();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_user_row_item, parent,false);

        return new SearchUserListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final User user = mUserList.get(position);
        holder.btn_follow.setVisibility(View.VISIBLE);
        holder.username.setText(user.getName());
        Picasso.get().load(user.getAvatar()).into(holder.avatar);
        holder.btn_follow.setText("follow");
        isFollowing(user.getFollowers(),holder.btn_follow);


        if(user.get_id().equals(muid)){
            holder.btn_follow.setVisibility(View.GONE);
        }

        // go to profile detail
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // HOLD: not a important function
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra(ProfileActivity.UID,muid);
                intent.putExtra(ProfileActivity.CURRENT_UID,user.get_id());
                mContext.startActivity(intent);
            }
        });

        // Follow button
        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.btn_follow.getText().toString().equals("follow")){
                    setToFollowing(muid,user.get_id(), holder.btn_follow);
                }else{
                    removeFromFollowing(muid,user.get_id(), holder.btn_follow);
                }
            }
        });
    }

    /**
     * When an user click the following buttons, that means user want to unfollow the user
     * **/
    private void removeFromFollowing(String uid, String fid, final Button button) {
        Call<ResponseBody> call = DataManager.getInstance().getAPIService().removeFollowing(uid,fid);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 201){
                    button.setText("follow");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void setToFollowing(String uid, String fid, final Button button) {
        HashMap<String,String> map = new HashMap<>();
        map.put("fid",fid);
        Call<ResponseBody> call = DataManager.getInstance().getAPIService().updateFollowing(uid,map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 201){
                    button.setText("following");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private  void addNotifications(String userId){
        // TODO Notification the user following
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications").child(userId);
//        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("userid", mFirebaseUser.getUid());
//        hashMap.put("message", "Start follow you");
//        hashMap.put("postid", "");
//        hashMap.put("ispost", false);
//
//        reference.push().setValue(hashMap);
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView avatar;
        public Button btn_follow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.search_row_username);
            avatar = itemView.findViewById(R.id.search_row_item_avatar);
            btn_follow = itemView.findViewById(R.id.search_row_follow);
        }
    }

    // Check  item user followers list isn't included current user
    private void isFollowing(final String[] followers , final Button button){
        if(followers != null){
            for(String uid: followers){
                if(uid.equals(muid)){
                    button.setText("following");
                    return;
                }
            }
        }




//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
//                .child("Follow").child(mFirebaseUser.getUid()).child("following");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot.child(userId).exists()){
//                    button.setText("following");
//                }else {
//                    button.setText("follow");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }
}