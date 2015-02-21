package net.umatoma.comiguide.model;

import android.content.Context;
import android.content.SharedPreferences;

import net.umatoma.comiguide.util.SharedPrefKeys;

public class User {

    private Context mContext;

    private String mApiToken;
    private int mUserId;
    private String mUserName;

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(
                SharedPrefKeys.User.PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = User.getSharedPreferences(context);
        return prefs.contains(SharedPrefKeys.User.API_TOKEN)
                && prefs.getString(SharedPrefKeys.User.API_TOKEN, null) != null;
    }

    public User(Context context) {
        mContext = context;
        SharedPreferences prefs = getSharedPreferences();
        mApiToken = prefs.getString(SharedPrefKeys.User.API_TOKEN, null);
        mUserId = prefs.getInt(SharedPrefKeys.User.USER_ID, -1);
        mUserName = prefs.getString(SharedPrefKeys.User.USER_NAME, null);
    }

    public SharedPreferences getSharedPreferences() {
        return User.getSharedPreferences(mContext);
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
        SharedPreferences prefs = getSharedPreferences();
        prefs.edit()
                .putString(SharedPrefKeys.User.API_TOKEN, mApiToken)
                .putInt(SharedPrefKeys.User.USER_ID, mUserId)
                .putString(SharedPrefKeys.User.USER_NAME, mUserName)
                .apply();
    }

    public void delete() {
        SharedPreferences prefs = getSharedPreferences();
        prefs.edit()
                .remove(SharedPrefKeys.User.API_TOKEN)
                .remove(SharedPrefKeys.User.USER_ID)
                .remove(SharedPrefKeys.User.USER_NAME)
                .apply();
    }
}
