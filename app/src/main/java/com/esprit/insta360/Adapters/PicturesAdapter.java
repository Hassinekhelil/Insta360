package com.esprit.insta360.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.esprit.insta360.Models.Post;
import com.esprit.insta360.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by TIBH on 28/01/2017.
 */

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.MyViewHolder> {
    private Context mContext;
    private List<Post> postList;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView picture;
        public TextView idPost;
        public MyViewHolder(View view) {
            super(view);
            picture=(ImageView)  view.findViewById(R.id.picture);
            idPost=(TextView) view.findViewById(R.id.idPicture);

            picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }

    public PicturesAdapter(Context context,List<Post> postList){
        this.mContext=context;
        this.postList=postList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.picture_card, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PicturesAdapter.MyViewHolder holder, int position) {
        final Post post=postList.get(position);
        holder.idPost.setText(String.valueOf(post.getId()));
        Picasso.with(mContext).load(post.getUrl()).into(holder.picture);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }


}
