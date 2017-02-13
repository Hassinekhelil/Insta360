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


import com.esprit.insta360.Activity.CommentsActivity;
import com.esprit.insta360.Activity.ProfileActivity;
import com.esprit.insta360.DAO.LikeDao;
import com.esprit.insta360.DAO.NotificationDao;
import com.esprit.insta360.Models.Post;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.RoundedTransformation;
import com.esprit.insta360.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Post> postList;
    public MyViewHolder holder;
    public LikeDao likeDao;
    public SessionManager sessionManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture, image,like,comment;
        public TextView name, date, description, location, likes,comments;

        public MyViewHolder(View view) {
            super(view);
            picture = (ImageView) view.findViewById(R.id.tvUserImage);
            image = (ImageView) view.findViewById(R.id.post);
            like = (ImageView) view.findViewById(R.id.likePicture);
            comment = (ImageView) view.findViewById(R.id.comment);
            name = (TextView) view.findViewById(R.id.writer);
            date = (TextView) view.findViewById(R.id.tvTime);
            location = (TextView) view.findViewById(R.id.tvlocation);
            description = (TextView) view.findViewById(R.id.content);
            likes = (TextView) view.findViewById(R.id.like);
            comments= (TextView) view.findViewById(R.id.nbComment);


        }
    }

    public PostsAdapter(Context mContext, List<Post> postList,LikeDao likeDao,SessionManager sessionManager) {
        this.mContext = mContext;
        this.postList = postList;
        this.likeDao=likeDao;
        this.sessionManager=sessionManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( MyViewHolder mHolder,final int position) {
        this.holder=mHolder;
        final Post post=postList.get(position);
        if (post.getLiked()){
            holder.like.setImageResource(0);
            holder.like.setBackgroundResource(R.drawable.ic_like_pressed);
        }else{
            holder.like.setImageResource(0);
            holder.like.setBackgroundResource(R.drawable.ic_like);
        }
        Picasso.with(mContext).load(post.getUrl()).into(holder.image);
        Picasso.with(mContext).load(post.getPhoto()).transform(new RoundedTransformation()).into(holder.picture);
        holder.name.setText(post.getName());
        holder.description.setText(post.getDescription());
        if (post.getLikes()>0){
            holder.likes.setText(String.valueOf(post.getLikes()));
        }
        if (post.getComments()>0){
            holder.comments.setText(String.valueOf(post.getComments()));
        }
        holder.location.setText(post.getLocation());
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTime(new Date());
        long actual = myCalendar.getTimeInMillis();
        Calendar userCalendar = Calendar.getInstance();
        userCalendar.setTime(post.getDate());
        long update = userCalendar.getTimeInMillis();
        long time=actual-update;
        if (TimeUnit.MILLISECONDS.toDays(time)<=0){
            if (TimeUnit.MILLISECONDS.toHours(time)<=0){
                holder.date.setText(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(time))+" minutes ago");
            }
            else {
                holder.date.setText(String.valueOf(TimeUnit.MILLISECONDS.toHours(time))+" hours ago");
            }
        }else {
            holder.date.setText(String.valueOf(TimeUnit.MILLISECONDS.toDays(time))+" days ago");
        }
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (postList.get(position).getLiked()){
                    likeDao.deleteLike(sessionManager.getUserId(),postList.get(position).getId(),"like");
                    view.setBackgroundResource(R.drawable.ic_like);
                    Toast.makeText(mContext, "id:"+postList.get(position).getId(), Toast.LENGTH_SHORT).show();
                    post.setLikes(post.getLikes()-1);
                    if (post.getLikes()>0){

                        holder.likes.setText(String.valueOf(post.getLikes()));
                    }
                    else{
                        holder.likes.setText("");
                    }

                    post.setLiked(false);
                }else{
                    likeDao.addLike(sessionManager.getUserId(),postList.get(position).getId(),"like");
                    if (sessionManager.getUserId()!=postList.get(position).getOwner()){
                        new NotificationDao().addNotification(sessionManager.getUserId(),postList.get(position).getOwner(),"like"
                                ,String.valueOf(postList.get(position).getId()));
                    }

                    view.setBackgroundResource(0);
                    view.setBackgroundResource(R.drawable.ic_like_pressed);
                    post.setLikes(post.getLikes()+1);
                    holder.likes.setText(String.valueOf(post.getLikes()));
                    post.setLiked(true);
                }

            }
        });
        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",postList.get(position).getOwner());
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",postList.get(position).getOwner());
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",postList.get(position).getId());
                Intent intent = new Intent(mContext, CommentsActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public Post getPost(int position){
        return postList.get(position);
    }



}
