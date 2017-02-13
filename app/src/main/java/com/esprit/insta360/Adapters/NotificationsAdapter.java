package com.esprit.insta360.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.insta360.Activity.PostActivity;
import com.esprit.insta360.Activity.ProfileActivity;
import com.esprit.insta360.Models.Notification;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by TIBH on 25/01/2017.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Notification> notificationList;
    private String createdAt;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile;
        public TextView description;
        public ImageView picture;

        public MyViewHolder(View view) {
            super(view);
            profile = (ImageView) view.findViewById(R.id.tvUserImage);
            description = (TextView) view.findViewById(R.id.writer);
            picture = (ImageView) view.findViewById(R.id.postImage);
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Notification notification = notificationList.get(position);
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        long actual = myCalendar.getTimeInMillis();
        Calendar userCalendar = Calendar.getInstance();
        userCalendar.setTime(notification.getCreated_at());
        long update = userCalendar.getTimeInMillis();
        long time=actual-update;
        if (TimeUnit.MILLISECONDS.toDays(time)<=0){
            if (TimeUnit.MILLISECONDS.toHours(time)<=0){
                createdAt=String.valueOf(TimeUnit.MILLISECONDS.toMinutes(time))+" minutes";
            }
            else {
               createdAt=String.valueOf(TimeUnit.MILLISECONDS.toHours(time))+" hours";
            }
        }else {
            createdAt=String.valueOf(TimeUnit.MILLISECONDS.toDays(time))+" days";
        }
        if (notification.getType().equals("request")){
            String text = "<font color=#000000><b> "+notification.getName()+
                    "</b></font> <font color=#000000> started following you. </font><font color=#cccccc>"+createdAt+"</font>";
            //textView.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
            holder.description.setText(Html.fromHtml(text),TextView.BufferType.SPANNABLE);
            //holder.description.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
        }
        else if (notification.getType().equals("likeComment")){
            String text = "<font color=#000000><b> "+notification.getName()+
                    "</b></font> <font color=#000000> liked your comment. </font><font color=#bfbfbf>"+createdAt+"</font>";
            holder.description.setText(Html.fromHtml(text),TextView.BufferType.SPANNABLE);
            //holder.description.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
            Picasso.with(mContext).load(notification.getUrl()).into(holder.picture);
        }
        else if (notification.getType().equals("comment")){
            String text = "<font color=#000000><b> "+notification.getName()+
                    "</b></font> <font color=#000000> commented your post. </font><font color=#bfbfbf>"+createdAt+"</font>";
            holder.description.setText(Html.fromHtml(text),TextView.BufferType.SPANNABLE);
            //holder.description.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
            Picasso.with(mContext).load(notification.getUrl()).into(holder.picture);
        }
        else {
            String text = "<font color=#000000><b> "+notification.getName()+
                    "</b></font> <font color=#000000> liked your post. </font><font color=#bfbfbf>"+createdAt+"</font>";
            holder.description.setText(Html.fromHtml(text),TextView.BufferType.SPANNABLE);
            //holder.description.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
            Picasso.with(mContext).load(notification.getUrl()).into(holder.picture);
        }


        if (notification.getProfile().isEmpty()) {
            Picasso.with(mContext)
                    .load(R.drawable.ic_profile)
                    .transform(new RoundedTransformation())
                    .into(holder.profile);
        } else{
            Picasso.with(mContext)
                    .load(notification.getProfile())
                    .transform(new RoundedTransformation())
                    .into(holder.profile);
        }
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",notificationList.get(position).getSender());
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",notificationList.get(position).getLink());
                Intent intent = new Intent(mContext, PostActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",notificationList.get(position).getLink());
                Intent intent = new Intent(mContext, PostActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
