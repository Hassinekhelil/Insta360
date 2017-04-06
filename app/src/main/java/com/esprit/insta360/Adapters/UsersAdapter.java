package com.esprit.insta360.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.insta360.Activity.ProfileActivity;
import com.esprit.insta360.Models.User;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by TIBH on 19/02/2017.
 */

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyViewHolder> {

    private Context mContext;
    private List<User> userList;

    public UsersAdapter(Context context,List<User> userList ){
        this.mContext=context;
        this.userList=userList;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final User user=userList.get(position);
        if (user.getPhoto().isEmpty()) {
            Picasso.with(mContext)
                    .load(R.drawable.ic_profile)
                    .transform(new RoundedTransformation())
                    .into(holder.profile);
        } else{
            Picasso.with(mContext)
                    .load(user.getPhoto())
                    .transform(new RoundedTransformation())
                    .into(holder.profile);
        }
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",userList.get(position).getId());
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        String text = "<font color=#000000><b> "+user.getName()+"</b></font>";
        holder.name.setText(Html.fromHtml(text),TextView.BufferType.SPANNABLE);
        //holder.name.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",userList.get(position).getId());
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button btn =(Button) view;
                if(userList.get(position).getFriendship()){
                    userList.get(position).setFriendship(false);
                    //invitationDao.deleteFriendRequest(sessionManager.getUserId(),idUser);
                    btn.setText("follow");
                    btn.setTextColor(ContextCompat.getColor(mContext,R.color.white));
                    btn.setBackgroundResource(R.drawable.btn_follow);
                }
                else{
                    userList.get(position).setFriendship(true);
                    //invitationDao.sendFriendRequest(sessionManager.getUserId(),idUser);
                    //notificationDao.addNotification(sessionManager.getUserId(),idUser,"request",null);
                    btn.setText("following");
                    btn.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
                    btn.setBackgroundResource(R.drawable.btn_follow_pressed);
                }
            }
        });

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile;
        public TextView name;
        public Button follow;

        public MyViewHolder(View view) {
            super(view);
            profile = (ImageView) view.findViewById(R.id.tvUserImage);
            name = (TextView) view.findViewById(R.id.writer);
            follow = (Button) view.findViewById(R.id.btnFollow);
        }
    }
}
