package net.umatoma.comiguide;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ComiGuide extends Application {

    public static int COMIKET_ID = 87;

    @Override
    public void onCreate () {
        super.onCreate();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.contains(getString(R.string.prefs_key_comiket_id))) {
            COMIKET_ID = prefs.getInt(getString(R.string.prefs_key_comiket_id), COMIKET_ID);
        } else {
            prefs.edit().putInt(getString(R.string.prefs_key_comiket_id), COMIKET_ID).apply();
        }
    }
}
