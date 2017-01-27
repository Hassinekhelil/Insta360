package com.esprit.insta360.Activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esprit.insta360.DAO.UserDao;
import com.esprit.insta360.Models.Post;
import com.esprit.insta360.Models.User;
import com.esprit.insta360.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private Button back,follow;
    private TextView title,name,alias,bio,posts,followers,followings;
    private ImageView profilePicture;
    private List<Post> postList;
    private List<User> userList;
    private Bundle bundle;
    private int idUser;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        back = (Button) findViewById(R.id.returnBtn);
        title =(TextView) findViewById(R.id.secondTitre);
        profilePicture = (ImageView) findViewById(R.id.ivUserProfilePhoto);
        name =(TextView) findViewById(R.id.name);
        alias=(TextView) findViewById(R.id.alias);
        bio=(TextView) findViewById(R.id.bio);
        follow=(Button) findViewById(R.id.btnFollow);
        posts=(TextView) findViewById(R.id.nbPost);
        followers=(TextView) findViewById(R.id.nbFollowers);
        followings=(TextView) findViewById(R.id.nbFollowings);
        userDao=new UserDao();
        postList=new ArrayList<>();
        userList=new ArrayList<>();
        /*bundle=getIntent().getExtras();
        idUser=bundle.getInt("id");*/
        idUser=6;
        userDao.getUserById(idUser,userList,postList);

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                //incompleted
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(getApplicationContext(), "u"+userList.size()+"/p"+postList.size(), Toast.LENGTH_SHORT).show();
        if (userList.size()>0){
            name.setText(userList.get(0).getName());
            alias.setText(userList.get(0).getLogin());
            bio.setText(userList.get(0).getBiographie());
            Picasso.with(getApplicationContext()).load(userList.get(0).getPhoto()).into(profilePicture);
        }

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
