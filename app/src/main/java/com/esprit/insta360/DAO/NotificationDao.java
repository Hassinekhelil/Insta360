package com.esprit.insta360.DAO;

import android.app.Activity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.insta360.Models.Notification;
import com.esprit.insta360.Utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by TIBH on 25/01/2017.
 */

public class NotificationDao {

    private Activity activity;
    public NotificationDao(Activity activity){
        this.activity=activity;
    }

    public void getNotifications(final List<Notification> notificationList){
        Volley.newRequestQueue(activity).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_NOTIFICATIONS + "?receiver=" +7,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                JSONArray array = jo.getJSONArray("notifications");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject j = array.getJSONObject(i);
                                    Notification notification = new Notification(j);
                                    notificationList.add(notification);
                                }

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
