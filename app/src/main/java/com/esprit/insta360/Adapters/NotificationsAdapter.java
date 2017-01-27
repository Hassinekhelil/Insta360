package com.esprit.insta360.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile;
        public TextView description;
        public ImageView post;

        public MyViewHolder(View view) {
            super(view);
            profile = (ImageView) view.findViewById(R.id.tvUserImage);
            description = (TextView) view.findViewById(R.id.writer);
            post = (ImageView) view.findViewById(R.id.postImage);
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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
                .inflate(R.layout.item_notification, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Notification notification = notificationList.get(position);
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
