package com.esprit.insta360.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

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
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.AppConfig;
import com.esprit.insta360.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TIBH on 05/02/2017.
 */

public class CommentsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Comment> commentList;
    private CommentsAdapter commentAdapter;
    private Bundle bundle;
    private int post;
    private Button send;
    private EditText content;
    private CommentDao commentDao;
    private SessionManager sessionManager;
    private LikeDao likeDao;
    private NotificationDao notificationDao;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        sessionManager= new SessionManager(getApplicationContext());
        send = (Button) findViewById(R.id.send);
        content = (EditText) findViewById(R.id.content);
        commentList=new ArrayList<>();
        likeDao=new LikeDao(this);
        bundle = getIntent().getExtras();
        post = bundle.getInt("id");
        commentAdapter=new CommentsAdapter(getApplicationContext(),commentList,likeDao,sessionManager,post);
        recyclerView = (RecyclerView) findViewById(R.id.comment_recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(commentAdapter);
        commentDao= new CommentDao(this);
        send.setEnabled(false);
        notificationDao=new NotificationDao();
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!content.getText().equals(null)){
                    send.setEnabled(true);
                }
                else {
                    send.setEnabled(false);
                }

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!content.getText().equals(null)||!content.getText().equals("")){
                    commentDao.addComment(sessionManager.getUserId(),post,content.getText().toString());
                    if (sessionManager.getUserId()!=commentList.get(0).getOwner()){
                        notificationDao.addNotification(sessionManager.getUserId(),commentList.get(0).getOwner(),
                                "comment",String.valueOf(post));
                    }
                    getComment();
                    content.setText(null);
                    InputMethodManager inputManager =
                            (InputMethodManager) getApplicationContext().
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(
                            CommentsActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        getComment();
    }

    public void getComment(){
        commentList.clear();
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_COMMENTS+
                "?post="+post+"&user="+sessionManager.getUserId(),
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
