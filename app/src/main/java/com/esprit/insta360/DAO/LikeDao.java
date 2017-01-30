package com.esprit.insta360.DAO;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.esprit.insta360.AppController;
import com.esprit.insta360.Utils.AppConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TIBH on 30/01/2017.
 */

public class LikeDao {
    private Activity activity;

    public LikeDao(Activity activity){
        this.activity=activity;

    }

    public void addLike(final int user,final int post){
        String tag_string_req = "req_like";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LIKE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(activity.getApplicationContext(), "sucess", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", String.valueOf(user));
                params.put("post", String.valueOf(post));
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public void deleteLike(final int user,final int post){
        String tag_string_req = "req_like";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_LIKE, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Toast.makeText(activity.getApplicationContext(), "sucess delete", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", String.valueOf(user));
                params.put("post", String.valueOf(post));
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
