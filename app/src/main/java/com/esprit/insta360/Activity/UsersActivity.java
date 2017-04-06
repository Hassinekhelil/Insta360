package com.esprit.insta360.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.insta360.Adapters.UsersAdapter;
import com.esprit.insta360.DAO.InvitationDao;
import com.esprit.insta360.DAO.NotificationDao;
import com.esprit.insta360.Models.User;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.AppConfig;
import com.esprit.insta360.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TIBH on 19/02/2017.
 */

public class UsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    private List<User> userList;
    private SessionManager sessionManager;
    private NotificationDao notificationDao;
    private InvitationDao invitationDao;
    private Bundle bundle;
    private int choice;
    private int id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        sessionManager= new SessionManager(getApplicationContext());
        userList=new ArrayList<>();
        invitationDao=new InvitationDao(this);
        bundle=getIntent().getExtras();
        choice=bundle.getInt("c");
        id=bundle.getInt("id");
        usersAdapter=new UsersAdapter(getApplicationContext(),userList);
        recyclerView = (RecyclerView) findViewById(R.id.user_recycler);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(usersAdapter);

    }
    @Override
    protected void onStart() {
        super.onStart();
        if (choice==1){
            getFollowers();
        }else{
            getFollowings();
        }
    }


    public void getFollowers(){
        userList.clear();
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_FOLLOWERS
                + "?id=" +id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                JSONArray array = jo.getJSONArray("users");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject j = array.getJSONObject(i);
                                    User user=new User(j);
                                    userList.add(user);
                                }
                                Toast.makeText(UsersActivity.this, "nbflw="+userList.size(), Toast.LENGTH_SHORT).show();
                                usersAdapter.notifyDataSetChanged();
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
    public void getFollowings(){
        userList.clear();
        Volley.newRequestQueue(this).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_FOLLOWINGS
                + "?id=" +id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                JSONArray array = jo.getJSONArray("users");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject j = array.getJSONObject(i);
                                    User user=new User(j);
                                    userList.add(user);
                                }
                                usersAdapter.notifyDataSetChanged();
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
