package com.esprit.insta360.DAO;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.esprit.insta360.Activity.LoginActivity;
import com.esprit.insta360.Activity.MainActivity;
import com.esprit.insta360.AppController;
import com.esprit.insta360.Models.Notification;
import com.esprit.insta360.Models.Post;
import com.esprit.insta360.Models.User;
import com.esprit.insta360.Utils.AppConfig;
import com.esprit.insta360.Utils.SessionManager;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by TIBH on 14/h11/2016.
 */

public class UserDao {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    private SessionManager session;
    private Activity activity;

    public UserDao(Activity activity) {
        this.activity = activity;
    }

    public UserDao() {

    }

    public void registerUser(final String name, final String email, final String password,
                             final String login) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(activity.getApplicationContext());

        pDialog.setMessage("Registering ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_REGISTER, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        JSONObject user = jObj.getJSONObject("user");
                        int id = user.getInt("id");
                        Toast.makeText(activity.getApplicationContext(), "User successfully registered. Try login now!", Toast.LENGTH_LONG).show();
                        // Launch login activity
                    } else {
                        // Error occurred in registration. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(activity.getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(activity.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("login", login);
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void checkLogin(final String email, final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_login";
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in ...");
        showDialog();
        session = new SessionManager(activity.getApplicationContext());
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Now store the user in SQLite

                        JSONObject user = jObj.getJSONObject("user");
                        int id = user.getInt("id");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        String photo = user.getString("photo");
                        // Create login session

                        session.setLogin(true);
                        session.setUserId(id);
                        session.setUserEmail(email);
                        session.setUserName(name);
                        session.setUserPwd(password);


                        // Launch main activity
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(activity.getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(activity.getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(activity.getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void updateUserImage(String email, String url, int id) {
        try {
            String uploadId = UUID.randomUUID().toString();

            //Creating a multi part request
            new MultipartUploadRequest(activity, uploadId, AppConfig.URL_LOAD_PHOTO)
                    .addFileToUpload(url, "image") //Adding file
                    .addParameter("id", String.valueOf(id)) //Adding text parameter to the request
                    .addParameter("email", email) //Adding text parameter to the request
                    .setNotificationConfig(new UploadNotificationConfig())
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            //Log.e(TAG, "upload Error: " + exc.getMessage());
            Toast.makeText(activity, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void loginFacbook(final String name, final String email, final String password,
                             final String login,final String photo) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";
        pDialog = new ProgressDialog(activity);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(activity.getApplicationContext());

        pDialog.setMessage("Registering ...");
        showDialog();


        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.URL_LOGIN_FACEBOOK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Register Response: " + response.toString());

                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        // Now store the user in SQLite

                        JSONObject user = jObj.getJSONObject("user");
                        int id = user.getInt("id");
                        String name = user.getString("name");
                        String email = user.getString("email");
                        String created_at = user.getString("created_at");
                        String photo = user.getString("photo");
                        // Create login session

                        session.setLogin(true);
                        session.setUserId(id);
                        session.setUserEmail(email);
                        session.setUserName(name);
                        session.setUserPwd(password);


                        // Launch main activity
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(activity.getApplicationContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(activity.getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(activity.getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("email", email);
                params.put("password", password);
                params.put("login", login);
                params.put("photo",photo);
                return params;
            }

        };


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }



    public void getUserById(final int id , final List<User> users, final List<Post> postList){
        Volley.newRequestQueue(activity).add(new StringRequest(Request.Method.GET, AppConfig.URL_GET_USER_BY_ID + "?id=" +id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            try {
                                JSONObject jo = new JSONObject(response);
                                JSONArray array1 = jo.getJSONArray("user");
                                JSONObject j = array1.getJSONObject(0);
                                User user=new User(j);
                                users.add(user);
                                JSONArray array = jo.getJSONArray("post");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject job = array.getJSONObject(i);
                                    Post post = new Post(job);
                                    postList.add(post);
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
