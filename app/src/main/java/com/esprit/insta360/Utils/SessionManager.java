package com.esprit.insta360.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by TIBH on 07/h11/2016.
 */

public class SessionManager {
    // LogCat tag
    //private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;
    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "PimLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public Editor getEditor() {
        return editor;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();


    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setUserId(int id) {

        editor.putInt("id", id);

        // commit changes
        editor.commit();

    }

    public int getUserId() {

        return pref.getInt("id", 0);
    }

    public void setUserPhoto(String url) {

        editor.putString("photo", url);

        // commit changes
        editor.commit();
    }

    public String getUserPhoto() {

        return pref.getString("photo", "");
    }

    public void setUserName(String url) {

        editor.putString("name", url);

        // commit changes
        editor.commit();
    }

    public String getUserName() {

        return pref.getString("name", "");
    }
    public void setUserEmail(String url) {

        editor.putString("email", url);

        // commit changes
        editor.commit();
    }

    public String getUserEmail() {

        return pref.getString("email", "");
    }
    public void setUserPhone(String url) {

        editor.putString("phone", url);

        // commit changes
        editor.commit();
    }

    public String getUserPhone() {

        return pref.getString("phone", "");
    }
    public void setUserPwd(String url) {

        editor.putString("pwd", url);

        // commit changes
        editor.commit();
    }

    public String getUserPwd() {

        return pref.getString("pwd", "");
    }

    public void setUserBio(String url) {

        editor.putString("bio", url);

        // commit changes
        editor.commit();
    }

    public String getUserBio() {

        return pref.getString("bio", "");
    }

    public void setUserSexe(String url) {

        editor.putString("sexe", url);

        // commit changes
        editor.commit();
    }

    public String getUserSexe() {

        return pref.getString("sexe", "");
    }

    public void setUserFollowers(int url) {

        editor.putInt("followers", url);

        // commit changes
        editor.commit();
    }

    public int getUserFollowers() {

        return pref.getInt("followers", 0);
    }

    public void setUserFollowings(int url) {

        editor.putInt("followings", url);

        // commit changes
        editor.commit();
    }

    public int getUserFollowings() {

        return pref.getInt("followings", 0);
    }

    public void setUserPosts(int url) {

        editor.putInt("posts", url);

        // commit changes
        editor.commit();
    }

    public int getUserPosts() {

        return pref.getInt("posts", 0);
    }
    public void setUserLogin(String url) {

        editor.putString("login", url);

        // commit changes
        editor.commit();
    }

    public String getUserLogin() {

        return pref.getString("login", "");
    }


}
