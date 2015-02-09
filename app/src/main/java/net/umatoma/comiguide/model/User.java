package net.umatoma.comiguide.model;

import android.content.Context;
import android.content.SharedPreferences;

import net.umatoma.comiguide.util.SharedPrefKeys;

public class User {

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                SharedPrefKeys.User.PREF_NAME, Context.MODE_PRIVATE);
        return prefs.contains(SharedPrefKeys.User.API_TOKEN)
                && prefs.getString(SharedPrefKeys.User.API_TOKEN, null) != null;
    }

    private Context mContext;

    private String mApiToken;
    private String mUserId;
    private String mUserName;

    public User(Context context) {
        mContext = context;
        SharedPreferences prefs = mContext.getSharedPreferences(
                SharedPrefKeys.User.PREF_NAME, Context.MODE_PRIVATE);
        mApiToken = prefs.getString(SharedPrefKeys.User.API_TOKEN, null);
        mUserId = prefs.getString(SharedPrefKeys.User.USER_ID, null);
        mUserName = prefs.getString(SharedPrefKeys.User.USER_NAME, null);
    }

    public String getApiToken() {
        return mApiToken;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getUserName() {
        return mUserName;
    }
}
