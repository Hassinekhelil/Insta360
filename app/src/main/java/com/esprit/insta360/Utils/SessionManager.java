package com.esprit.insta360.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by TIBH on 07/11/2016.
 */

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;
    Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "FindMeLogin";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";

    private static final String USER_ID = "id";

    private static final String CIRCLE_ID = "circle_id";

    private static final String CIRCLE_CODE= "circle_code";

    private static final String USER_PHOTO= "user_photo";

    private static final String NEWS_ID = "news_id";

    private static final String USER_POSITION = "user_position";

    public int DefaultId = 0;

    public int DefaultCircleId =0;

    public String DefaultCircleCode ="";

    public int DefaultIdNews = 0;

    public int DefaultPosition = 0;

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

        editor.putInt(USER_ID, id);

        // commit changes
        editor.commit();

    }

    public int getUserId() {

        return pref.getInt(USER_ID, DefaultId);
    }

    public void setCircleId(int id) {

        editor.putInt(CIRCLE_ID, id);

        // commit changes
        editor.commit();
    }

    public int getCircleId() {

        return pref.getInt(CIRCLE_ID, DefaultCircleId);
    }
    public void setNewsId(int id) {

        editor.putInt(NEWS_ID, id);

        // commit changes
        editor.commit();

    }
    public int getNewsId() {

        return pref.getInt(NEWS_ID, DefaultIdNews);

    }

    public void setCircleCode(String code) {

        editor.putString(CIRCLE_CODE, code);

        // commit changes
        editor.commit();
    }

    public String getCircleCode() {

        return pref.getString(CIRCLE_CODE, DefaultCircleCode);
    }
    public void setUserPhoto(String url) {

        editor.putString(USER_PHOTO, url);

        // commit changes
        editor.commit();
    }

    public String getUserPhoto() {

        return pref.getString(USER_PHOTO, "");
    }




    public void setUserName(String url) {

        editor.putString("user_name", url);

        // commit changes
        editor.commit();
    }

    public String getUserName() {

        return pref.getString("user_name", "");
    }
    public void setUserEmail(String url) {

        editor.putString("user_email", url);

        // commit changes
        editor.commit();
    }

    public String getUserEmail() {

        return pref.getString("user_email", "");
    }
    public void setUserPhone(String url) {

        editor.putString("user_phone", url);

        // commit changes
        editor.commit();
    }

    public String getUserPhone() {

        return pref.getString("user_phone", "");
    }
    public void setUserPwd(String url) {

        editor.putString("user_pwd", url);

        // commit changes
        editor.commit();
    }

    public String getUserPwd() {

        return pref.getString("user_pwd", "");
    }

    public void setUserPosition(String position) {

        editor.putString("user_position", position);

        // commit changes
        editor.commit();
    }

    public String getUserPosition() {

        return pref.getString("user_position", "");
    }
}
