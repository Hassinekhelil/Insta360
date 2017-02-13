package com.esprit.insta360.DAO;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.insta360.AppController;
import com.esprit.insta360.Utils.AppConfig;
import com.esprit.insta360.Utils.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TIBH on 28/01/2017.
 */

public class InvitationDao {

    private Activity activity;

    public InvitationDao(Activity activity) {
        this.activity=activity;
    }

    public void sendFriendRequest(final int sender,final int receiver){
        String tag_string_req = "req_send";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_SEND_REQUEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
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
                params.put("sender", String.valueOf(sender));
                params.put("receiver", String.valueOf(receiver));
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
    public void deleteFriendRequest(final int sender,final int receiver){
        String tag_string_req = "req_delete";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_DELETE_REQUEST, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
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
                params.put("sender", String.valueOf(sender));
                params.put("receiver", String.valueOf(receiver));
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
