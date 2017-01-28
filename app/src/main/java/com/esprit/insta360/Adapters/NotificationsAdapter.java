package com.esprit.insta360.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.insta360.Activity.ProfileActivity;
import com.esprit.insta360.Models.Notification;
import com.esprit.insta360.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by TIBH on 25/01/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Notification> notificationList;
    private int user;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile;
        public TextView description,idPost,idUser;
        public ImageView post;

        public MyViewHolder(View view) {
            super(view);
            profile = (ImageView) view.findViewById(R.id.tvUserImage);
            description = (TextView) view.findViewById(R.id.writer);
            post = (ImageView) view.findViewById(R.id.postImage);
            idPost=(TextView) view.findViewById(R.id.idPost);
            idUser=(TextView) view.findViewById(R.id.idUser);
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle =new Bundle();
                    bundle.putInt("id",user);
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });

            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle =new Bundle();
                    bundle.putInt("idPost",0);
                    //Intent intent = new Intent(mContext, ProfileAct.class);
                    //mContext.startActivity(intent);
                }
            });
        }
    }

    public NotificationsAdapter(Context mContext, List<Notification> notificationList) {
        this.mContext = mContext;
        this.notificationList = notificationList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Notification notification = notificationList.get(position);
        user=notification.getSender();
        holder.idUser.setText(String.valueOf(notification.getSender()));
        holder.idPost.setText(String.valueOf(notification.getLink()));
        Picasso.with(mContext).load(notification.getUrl()).into(holder.post);
        //holder.created_at.setText(news.getCreated_at());
        holder.description.setText(notification.getName()+" has liked your post");
       /* if (notification.getType()=="like"){
            holder.description.setText(notification.getSender()+"has liked your post");
        }
        else {
            holder.description.setText("");
        }*/
        if (notification.getProfile().isEmpty()) {
            holder.profile.setImageResource(R.drawable.ic_profile);
        } else{
            Picasso.with(mContext).load(notification.getProfile()).into(holder.profile);
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
