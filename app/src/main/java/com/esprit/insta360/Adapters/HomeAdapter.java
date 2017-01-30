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

import com.esprit.insta360.Activity.PostActivity;
import com.esprit.insta360.Activity.ProfileActivity;
import com.esprit.insta360.DAO.LikeDao;
import com.esprit.insta360.Models.Like;
import com.esprit.insta360.Models.Post;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by TIBH on 30/01/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private Context mContext;
    private List<Post> postList;
    private LikeDao likeDao;
    private SessionManager sessionManager;

    public HomeAdapter (Context context, List<Post> postList,LikeDao likeDao,SessionManager sessionManager){
        this.mContext=context;
        this.postList=postList;
        this.likeDao=likeDao;
        this.sessionManager=sessionManager;
        Toast.makeText(mContext, "const"+postList.size(), Toast.LENGTH_SHORT).show();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Post post=postList.get(position);
        Toast.makeText(mContext, "onBind"+post.getId(), Toast.LENGTH_SHORT).show();
        if (post.getLiked()){
            holder.like.setBackgroundResource(R.drawable.ic_like_pressed);
        }
        Picasso.with(mContext).load(post.getUrl()).into(holder.image);
        Picasso.with(mContext).load(post.getPhoto()).into(holder.picture);
        holder.name.setText(post.getName());
        holder.description.setText(post.getDescription());
        holder.likes.setText(String.valueOf(post.getLikes()));
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
                if (post.getLiked()){
                    likeDao.deleteLike(sessionManager.getUserId(),post.getId());
                    holder.like.setBackgroundResource(R.drawable.ic_like);
                    holder.likes.setText(String.valueOf(post.getLikes()-1));
                    post.setLiked(false);
                }else{
                    likeDao.addLike(sessionManager.getUserId(),post.getId());
                    holder.like.setBackgroundResource(R.drawable.ic_like_pressed);
                    holder.likes.setText(String.valueOf(post.getLikes()+1));
                    post.setLiked(true);
                }

            }
        });
        holder.picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",getPost(position).getOwner());
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public Post getPost(int position){
        return postList.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture, image,like;
        public TextView name, date, description, location, likes;


        public MyViewHolder(View view) {
            super(view);
            Toast.makeText(mContext, "holder", Toast.LENGTH_SHORT).show();
            picture = (ImageView) view.findViewById(R.id.tvUserImage);
            image = (ImageView) view.findViewById(R.id.post);
            like = (ImageView) view.findViewById(R.id.likePicture);
            name = (TextView) view.findViewById(R.id.writer);
            date = (TextView) view.findViewById(R.id.tvTime);
            location = (TextView) view.findViewById(R.id.tvlocation);
            description = (TextView) view.findViewById(R.id.content);
            likes = (TextView) view.findViewById(R.id.like);



        }
    }
}
