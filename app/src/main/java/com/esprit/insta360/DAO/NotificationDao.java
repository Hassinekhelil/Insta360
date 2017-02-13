package com.esprit.insta360.DAO;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.insta360.AppController;
import com.esprit.insta360.Models.Notification;
import com.esprit.insta360.Utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TIBH on 25/01/2017.
 */

public class NotificationDao {

    public NotificationDao(){
    }


    public void addNotification(final int sender,final int receiver,final String type,final String link){
        String tag_string_req = "req_notify";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_NOTIFY, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("sender", String.valueOf(sender));
                params.put("receiver", String.valueOf(receiver));
                params.put("link", link);
                params.put("type",type);

                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
