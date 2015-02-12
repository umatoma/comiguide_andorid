package net.umatoma.comiguide.model;

import android.content.Context;
import android.content.SharedPreferences;

import net.umatoma.comiguide.util.SharedPrefKeys;

public class User {

    private Context mContext;

    private String mApiToken;
    private int mUserId;
    private String mUserName;

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                SharedPrefKeys.User.PREF_NAME, Context.MODE_PRIVATE);
        return prefs.contains(SharedPrefKeys.User.API_TOKEN)
                && prefs.getString(SharedPrefKeys.User.API_TOKEN, null) != null;
    }

    public User(Context context) {
        mContext = context;
        SharedPreferences prefs = mContext.getSharedPreferences(
                SharedPrefKeys.User.PREF_NAME, Context.MODE_PRIVATE);
        mApiToken = prefs.getString(SharedPrefKeys.User.API_TOKEN, null);
        mUserId = prefs.getInt(SharedPrefKeys.User.USER_ID, -1);
        mUserName = prefs.getString(SharedPrefKeys.User.USER_NAME, null);
    }

    public String getApiToken() {
        return mApiToken;
    }

    public int getUserId() {
        return mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setApiToken(String apiToken) {
        mApiToken = apiToken;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public void save() {
        SharedPreferences prefs = mContext.getSharedPreferences(
                SharedPrefKeys.User.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(SharedPrefKeys.User.API_TOKEN, mApiToken);
        editor.putInt(SharedPrefKeys.User.USER_ID, mUserId);
        editor.putString(SharedPrefKeys.User.USER_NAME, mUserName);
        editor.apply();
    }
}
