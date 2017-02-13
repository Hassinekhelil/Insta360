package com.esprit.insta360.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.insta360.Adapters.NotificationsAdapter;
import com.esprit.insta360.DAO.NotificationDao;
import com.esprit.insta360.Models.Notification;
import com.esprit.insta360.R;
import com.esprit.insta360.Utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TIBH on 25/01/2017.
 */

public class NotificationsFragment extends Fragment{

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private List<Notification> notificationList;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.notif_recycler);
        notificationList = new ArrayList<>();
        adapter = new NotificationsAdapter(getActivity(), notificationList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        getNotifications();
        return view;
    }

    private void refresh() {

        mSwipeRefreshLayout.setRefreshing(true);
        getNotifications();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private void getNotifications(){
        notificationList.clear();
        //notificationDao.getNotifications(notificationList);
        Volley.newRequestQueue(getActivity()).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_NOTIFICATIONS + "?receiver=" +7,
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
                                    //Toast.makeText(getActivity(), "id="+notificationList.get(0).getSender()
                                           // +"post="+notificationList.get(0).getLink(), Toast.LENGTH_SHORT).show();
                                }
                                adapter.notifyDataSetChanged();

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
