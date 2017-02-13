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

import com.esprit.insta360.Activity.ProfileActivity;
import com.esprit.insta360.DAO.LikeDao;
import com.esprit.insta360.DAO.NotificationDao;
import com.esprit.insta360.Models.Comment;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.RoundedTransformation;
import com.esprit.insta360.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Comment> commentList;
    public MyViewHolder holder;
    public LikeDao likeDao;
    public SessionManager sessionManager;
    public int post;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView profile;
        public TextView content,name,created_at,likes;
        public ImageView like;

        public MyViewHolder(View view) {
            super(view);
            profile = (ImageView) view.findViewById(R.id.tvUserImage);
            name = (TextView) view.findViewById(R.id.writer);
            created_at=(TextView) view.findViewById(R.id.cTime);
            content=(TextView) view.findViewById(R.id.content);
            likes=(TextView) view.findViewById(R.id.c_likes);
            like = (ImageView) view.findViewById(R.id.likeComment);
        }
    }

    public CommentsAdapter(Context mContext, List<Comment> commentList,LikeDao likeDao,SessionManager sessionManager,int post){
        this.mContext=mContext;
        this.commentList=commentList;
        this.likeDao=likeDao;
        this.sessionManager=sessionManager;
        this.post=post;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( MyViewHolder mHolder,final int position) {
        this.holder=mHolder;
        final Comment comment = commentList.get(position);
        holder.content.setText(comment.getContent());
        String text = "<font color=#000000><b> "+comment.getName()+"</b></font>";
        holder.name.setText(Html.fromHtml(text),TextView.BufferType.SPANNABLE);
        //holder.name.setText(Html.fromHtml(text,Html.FROM_HTML_MODE_LEGACY));
        if (comment.getLikes()>0){
            holder.likes.setText(String.valueOf(comment.getLikes())+" likes");
        }
        if (comment.getLiked()){
            holder.like.setImageResource(0);
            holder.like.setBackgroundResource(R.drawable.ic_like_pressed);
        }
        else {
            holder.like.setImageResource(0);
            holder.like.setBackgroundResource(R.drawable.ic_like);
        }
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        long actual = myCalendar.getTimeInMillis();
        Calendar userCalendar = Calendar.getInstance();
        userCalendar.setTime(comment.getCreated_at());
        long update = userCalendar.getTimeInMillis();
        long time=actual-update;
        if (TimeUnit.MILLISECONDS.toDays(time)<=0){
            if (TimeUnit.MILLISECONDS.toHours(time)<=0){
                holder.created_at.setText(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(time))+" m");
            }
            else {
                holder.created_at.setText(String.valueOf(TimeUnit.MILLISECONDS.toHours(time))+" h");
            }
        }else {
            holder.created_at.setText(String.valueOf(TimeUnit.MILLISECONDS.toHours(time))+" d");
        }



        if (comment.getPicture().isEmpty()) {
            Picasso.with(mContext)
                    .load(R.drawable.ic_profile)
                    .transform(new RoundedTransformation())
                    .into(holder.profile);
        } else{
            Picasso.with(mContext)
                    .load(comment.getPicture())
                    .transform(new RoundedTransformation())
                    .into(holder.profile);
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commentList.get(position).getLiked()){
                    likeDao.deleteLike(sessionManager.getUserId(),commentList.get(position).getId(),"comment");
                    view.setBackgroundResource(R.drawable.ic_like);
                    commentList.get(position).setLikes(commentList.get(position).getLikes()-1);
                    if (comment.getLikes()>0){
                        holder.likes.setText(String.valueOf(commentList.get(position).getLikes())+" likes");
                    }
                    else {
                        holder.likes.setText("");
                    }
                    commentList.get(position).setLiked(false);
                }else{
                    likeDao.addLike(sessionManager.getUserId(),comment.getId(),"comment");
                    if (sessionManager.getUserId()!=commentList.get(position).getOwner()){
                        new NotificationDao().addNotification(sessionManager.getUserId(),commentList.get(position).getOwner(),
                        "likeComment",String.valueOf(post));
                    }

                    view.setBackgroundResource(0);
                    view.setBackgroundResource(R.drawable.ic_like_pressed);
                    commentList.get(position).setLikes(commentList.get(position).getLikes()+1);
                    holder.likes.setText(String.valueOf(commentList.get(position).getLikes())+" likes");
                    commentList.get(position).setLiked(true);
                }

            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",commentList.get(position).getUser());
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        holder.profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",commentList.get(position).getUser());
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
