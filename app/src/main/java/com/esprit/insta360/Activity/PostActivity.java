package com.esprit.insta360.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.insta360.Adapters.CommentsAdapter;
import com.esprit.insta360.DAO.CommentDao;
import com.esprit.insta360.DAO.LikeDao;
import com.esprit.insta360.DAO.NotificationDao;
import com.esprit.insta360.Models.Comment;
import com.esprit.insta360.Models.Post;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.AppConfig;
import com.esprit.insta360.Utils.RoundedTransformation;
import com.esprit.insta360.Utils.SessionManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by TIBH on 30/01/2017.
 */

public class PostActivity extends AppCompatActivity {

    private ImageView picture, image,like,comment;
    private TextView name, date, description, location, likes,comments;
    private int id;
    private Bundle bundle;
    private Post post;
    private SessionManager sessionManager;
    private LikeDao likeDao;
    private int nbLikes=0;
    private RecyclerView recyclerView;
    private List<Comment> commentList;
    private CommentsAdapter commentAdapter;
    private Button send;
    private EditText content;
    private CommentDao commentDao;
    private NotificationDao notificationDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        sessionManager= new SessionManager(getApplicationContext());
        picture = (ImageView) findViewById(R.id.tvUserImage);
        image = (ImageView) findViewById(R.id.post);
        like = (ImageView) findViewById(R.id.likePicture);
        comment = (ImageView) findViewById(R.id.comment);
        name = (TextView) findViewById(R.id.writer);
        date = (TextView) findViewById(R.id.tvTime);
        location = (TextView) findViewById(R.id.tvlocation);
        description = (TextView) findViewById(R.id.content);
        likes = (TextView) findViewById(R.id.like);
        comments = (TextView) findViewById(R.id.nb_comments);
        bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        likeDao=new LikeDao(this);
        notificationDao= new NotificationDao();
        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",post.getOwner());
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",post.getOwner());
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putInt("id",post.getOwner());
                Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (post.getLiked()){
                    likeDao.deleteLike(sessionManager.getUserId(),id,"post");
                    like.setImageResource(0);
                    like.setBackgroundResource(R.drawable.ic_like);
                    likes.setText(String.valueOf(nbLikes-1));
                    nbLikes--;
                    post.setLiked(false);
                }else{
                    likeDao.addLike(sessionManager.getUserId(),id,"post");
                    if (sessionManager.getUserId()!=post.getOwner()){
                        notificationDao.addNotification(sessionManager.getUserId(),post.getOwner(),
                                "like",String.valueOf(id));
                    }
                    like.setImageResource(0);
                    like.setBackgroundResource(R.drawable.ic_like_pressed);
                    likes.setText(String.valueOf(nbLikes+1));
                    nbLikes++;
                    post.setLiked(true);
                }
            }
        });
        send = (Button) findViewById(R.id.send);
        content = (EditText) findViewById(R.id.Pcontent);
        commentList=new ArrayList<>();
        likeDao=new LikeDao(this);
        commentAdapter=new CommentsAdapter(getApplicationContext(),commentList,likeDao,sessionManager,id);
        recyclerView = (RecyclerView) findViewById(R.id.comment_recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(commentAdapter);
        commentDao= new CommentDao(this);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!content.getText().equals(null)||!content.getText().equals("")){
                    commentDao.addComment(sessionManager.getUserId(),post.getId(),content.getText().toString());
                    if (sessionManager.getUserId()!=post.getOwner()) {
                        notificationDao.addNotification(sessionManager.getUserId(),post.getOwner(),
                                "comment",String.valueOf(id));
                    }
                    getComment();
                    content.setText(null);
                    InputMethodManager inputManager =
                            (InputMethodManager) getApplicationContext().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            PostActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getPost();
        getComment();
    }

    public void setParams() {
        if (post != null) {

            if (post.getLiked()){
                like.setImageResource(0);
                like.setBackgroundResource(R.drawable.ic_like_pressed);
            }
            Picasso.with(getApplicationContext()).load(post.getUrl()).into(image);
            Picasso.with(getApplicationContext()).load(post.getPhoto()).transform(new RoundedTransformation()).into(picture);
            name.setText(post.getName());
            description.setText(post.getDescription());
            nbLikes=post.getLikes();
            if (post.getLikes()>0){
                likes.setText(String.valueOf(post.getLikes()));
            }
            if (post.getComments()>0){
                comments.setText(String.valueOf(post.getComments()));
            }
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

    public void getComment(){
        commentList.clear();
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_COMMENTS+
                "?post="+id+"&user="+sessionManager.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                JSONArray array = jo.getJSONArray("comments");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject j = array.getJSONObject(i);
                                    Comment comment = new Comment(j);
                                    commentList.add(comment);
                                }
                                commentAdapter.notifyDataSetChanged();
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
