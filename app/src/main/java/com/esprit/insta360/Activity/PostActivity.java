package com.esprit.insta360.Activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import com.esprit.insta360.DAO.LikeDao;
import com.esprit.insta360.Models.Post;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.AppConfig;
import com.esprit.insta360.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by TIBH on 30/01/2017.
 */

public class PostActivity extends AppCompatActivity {

    private ImageView picture, image,like;
    private TextView name, date, description, location, likes;
    private int id;
    private Button back;
    private Bundle bundle;
    private Post post;
    private SessionManager sessionManager;
    private LikeDao likeDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        sessionManager= new SessionManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.post_toolbar);
        setSupportActionBar(toolbar);
        back = (Button) findViewById(R.id.returnBtn);
        picture = (ImageView) findViewById(R.id.tvUserImage);
        image = (ImageView) findViewById(R.id.post);
        like = (ImageView) findViewById(R.id.likePicture);
        name = (TextView) findViewById(R.id.writer);
        date = (TextView) findViewById(R.id.tvTime);
        location = (TextView) findViewById(R.id.tvlocation);
        description = (TextView) findViewById(R.id.content);
        likes = (TextView) findViewById(R.id.like);
        bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        sessionManager.setUserId(7);
        likeDao=new LikeDao(this);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (post.getLiked()){
                    likeDao.deleteLike(sessionManager.getUserId(),id);
                    like.setBackgroundResource(R.drawable.ic_like);
                    likes.setText(String.valueOf(post.getLikes()-1));
                    post.setLiked(false);
                }else{
                    likeDao.addLike(sessionManager.getUserId(),id);
                    like.setBackgroundResource(R.drawable.ic_like_pressed);
                    likes.setText(String.valueOf(post.getLikes()+1));
                    post.setLiked(true);
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        getPost();
    }

    public void setParams() {
        if (post != null) {

            if (post.getLiked()){
                like.setBackgroundResource(R.drawable.ic_like_pressed);
            }
            Picasso.with(getApplicationContext()).load(post.getUrl()).into(image);
            Picasso.with(getApplicationContext()).load(post.getPhoto()).into(picture);
            name.setText(post.getName());
            description.setText(post.getDescription());
            likes.setText(String.valueOf(post.getLikes()));
            location.setText(post.getLocation());

            Calendar myCalendar = Calendar.getInstance();
            myCalendar.setTime(new Date());
            long actual = myCalendar.getTimeInMillis();
            Calendar userCalendar = Calendar.getInstance();
            userCalendar.setTime(post.getDate());
            long update = userCalendar.getTimeInMillis();
            long time=actual-update;
            if (TimeUnit.MILLISECONDS.toDays(time)<=0){
                if (TimeUnit.MILLISECONDS.toHours(time)<=0){
                    date.setText(String.valueOf(TimeUnit.MILLISECONDS.toMinutes(time))+" minutes ago");
                }
                else {
                    date.setText(String.valueOf(TimeUnit.MILLISECONDS.toHours(time))+" hours ago");
                }
            }else {
                date.setText(String.valueOf(TimeUnit.MILLISECONDS.toDays(time))+" days ago");
            }
        }


    }

    public void getPost() {
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_POST
                + "?user=" + sessionManager.getUserId() +"&post="+id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                JSONArray array = jo.getJSONArray("post");
                                JSONObject j = array.getJSONObject(0);
                                post = new Post(j);
                                setParams();
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
