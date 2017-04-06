package com.esprit.insta360.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.insta360.Activity.EditProfileActivity;
import com.esprit.insta360.Adapters.PicturesAdapter;
import com.esprit.insta360.Models.Post;
import com.esprit.insta360.Models.User;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.AppConfig;
import com.esprit.insta360.Utils.RoundedTransformation;
import com.esprit.insta360.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TIBH on 25/01/2017.
 */

public class ProfileFragment extends Fragment {

    private Button edit;
    private TextView name,alias,bio,posts,followers,followings,idPost,id;
    private ImageView profilePicture;
    private RecyclerView mRecyclerView;
    private List<Post> postList;
    private PicturesAdapter picturesAdapter;
    private SessionManager sessionManager;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sessionManager= new SessionManager(getContext());

        profilePicture = (ImageView) view.findViewById(R.id.ivUserProfilePhoto);
        name =(TextView) view.findViewById(R.id.name);
        alias=(TextView) view.findViewById(R.id.alias);
        bio=(TextView) view.findViewById(R.id.bio);
        edit=(Button) view.findViewById(R.id.btnFollow);
        posts=(TextView) view.findViewById(R.id.nbPost);
        followers=(TextView) view.findViewById(R.id.nbFollowers);
        followings=(TextView) view.findViewById(R.id.nbFollowings);
        edit=(Button) view.findViewById(R.id.btnEdit);
        mRecyclerView=(RecyclerView) view.findViewById(R.id.rvMyProfile);
        postList=new ArrayList<>();
        picturesAdapter=new PicturesAdapter(getContext(),postList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(picturesAdapter);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        setParams();
        return view;
    }

    public void setParams(){
        name.setText(sessionManager.getUserName());
        alias.setText(sessionManager.getUserEmail());
        bio.setText(sessionManager.getUserBio());
        posts.setText(String.valueOf(sessionManager.getUserPosts()));
        followers.setText(String.valueOf(sessionManager.getUserFollowers()));
        followings.setText(String.valueOf(sessionManager.getUserFollowings()));
        Picasso.with(getContext())
                .load(sessionManager.getUserPhoto())
                .transform(new RoundedTransformation())
                .into(profilePicture);
        getPosts();

    }

    public void getPosts(){
        postList.clear();
        Volley.newRequestQueue(getActivity()).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_POSTS_BY_USER
                + "?id="+sessionManager.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                JSONArray array = jo.getJSONArray("post");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject job = array.getJSONObject(i);
                                    Post post = new Post(job);
                                    postList.add(post);
                                }
                                picturesAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error setting note : " + error.getMessage());
                if (error instanceof TimeoutError) {
                    System.out.println("erreur");
                }
            }
        }));
    }
}
