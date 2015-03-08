package net.umatoma.comiguide.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ComiketCircleMapSharedPref {

    private Context mContext;

    public ComiketCircleMapSharedPref(Context context) {
        mContext = context;
    }

    public ComiketCircleMapSharedPref setDayHistory(int day) {
        getSharedPreferences()
                .edit()
                .putInt(SharedPrefKeys.History.COMIKET_CIRCLE_DAY, day)
                .apply();
        return this;
    }

    public ComiketCircleMapSharedPref setMapIdHistory(int cmap_id) {
        getSharedPreferences()
                .edit()
                .putInt(SharedPrefKeys.History.COMIKET_CIRCLE_MAP_ID, cmap_id)
                .apply();
        return this;
    }

    public int getDayHistory(int default_val) {
        return getSharedPreferences()
                .getInt(SharedPrefKeys.History.COMIKET_CIRCLE_DAY, default_val);
    }

    public int getMapIdHistory(int default_val) {
        return getSharedPreferences()
                .getInt(SharedPrefKeys.History.COMIKET_CIRCLE_MAP_ID, default_val);
    }

    private SharedPreferences getSharedPreferences() {
        return mContext.getSharedPreferences(
                SharedPrefKeys.History.PREF_NAME, Context.MODE_PRIVATE);
    }
}
