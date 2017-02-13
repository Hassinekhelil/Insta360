package com.esprit.insta360.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
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
import com.esprit.insta360.Adapters.PicturesAdapter;
import com.esprit.insta360.DAO.InvitationDao;
import com.esprit.insta360.DAO.NotificationDao;
import com.esprit.insta360.DAO.UserDao;
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
 * Created by TIBH on 27/01/2017.
 */

public class ProfileActivity extends AppCompatActivity {
    private Button follow;
    private TextView name,alias,bio,posts,followers,followings,idPost,id;
    private ImageView profilePicture;
    private List<Post> postList;
    private List<User> userList;
    private Bundle bundle;
    private int idUser;
    private UserDao userDao;
    private PicturesAdapter picturesAdapter;
    private RecyclerView mRecyclerView;
    private InvitationDao invitationDao;
    private SessionManager sessionManager;
    private NotificationDao notificationDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        sessionManager= new SessionManager(getApplicationContext());
        profilePicture = (ImageView) findViewById(R.id.ivUserProfilePhoto);
        name =(TextView) findViewById(R.id.name);
        alias=(TextView) findViewById(R.id.alias);
        bio=(TextView) findViewById(R.id.bio);
        follow=(Button) findViewById(R.id.btnFollow);
        posts=(TextView) findViewById(R.id.nbPost);
        followers=(TextView) findViewById(R.id.nbFollowers);
        followings=(TextView) findViewById(R.id.nbFollowings);
        mRecyclerView=(RecyclerView)findViewById(R.id.rvUserProfile);
        userDao=new UserDao();
        invitationDao=new InvitationDao(this);
        notificationDao= new NotificationDao();
        postList=new ArrayList<>();
        userList=new ArrayList<>();
        picturesAdapter=new PicturesAdapter(getApplicationContext(),postList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(picturesAdapter);
        bundle=getIntent().getExtras();
        idUser=bundle.getInt("id");

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userList.get(0).getFriendship()){
                    userList.get(0).setFriendship(false);
                    followers.setText(String.valueOf(userList.get(0).getFollowers()-1));
                    invitationDao.deleteFriendRequest(sessionManager.getUserId(),idUser);
                    follow.setText("follow");
                    follow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                    follow.setBackgroundResource(R.drawable.btn_follow);
                }
                else{
                    userList.get(0).setFriendship(true);
                    followers.setText(String.valueOf(userList.get(0).getFollowers()+1));
                    invitationDao.sendFriendRequest(sessionManager.getUserId(),idUser);
                    notificationDao.addNotification(sessionManager.getUserId(),idUser,"request",null);
                    follow.setText("following");
                    follow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                    follow.setBackgroundResource(R.drawable.btn_follow_pressed);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        getUser();
    }

    public void setParams(){

        if (userList.size()>0){
            name.setText(userList.get(0).getName());
            alias.setText(userList.get(0).getLogin());
            bio.setText(userList.get(0).getBiographie());
            followers.setText(String.valueOf(userList.get(0).getFollowers()));
            followings.setText(String.valueOf(userList.get(0).getFollowings()));
            posts.setText(String.valueOf(userList.get(0).getPosts()));
            Picasso.with(getApplicationContext())
                    .load(userList.get(0).getPhoto())
                    .transform(new RoundedTransformation())
                    .into(profilePicture);
            if (userList.get(0).getFriendship()){
                follow.setText("following");
                follow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                follow.setBackgroundResource(R.drawable.btn_follow_pressed);
            }
            else{
                follow.setText("follow");
                follow.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
                follow.setBackgroundResource(R.drawable.btn_follow);

            }
        }
    }

    public void getUser(){
        postList.clear();
        userList.clear();
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_USER_BY_ID
                + "?id=" +idUser+"&me="+sessionManager.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                JSONArray array1 = jo.getJSONArray("users");
                                JSONObject j = array1.getJSONObject(0);
                                User user=new User(j);
                                userList.add(user);
                                JSONArray array = jo.getJSONArray("post");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject job = array.getJSONObject(i);
                                    Post post = new Post(job);
                                    postList.add(post);
                                }
                                setParams();
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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
