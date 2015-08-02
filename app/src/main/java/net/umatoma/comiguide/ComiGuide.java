package net.umatoma.comiguide;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ComiGuide extends Application {

    public static int COMIKET_ID = 88;
    public static int COMIC1_ID = 9;

    @Override
    public void onCreate () {
        super.onCreate();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.contains(getString(R.string.prefs_key_comiket_id))) {
            COMIKET_ID = prefs.getInt(getString(R.string.prefs_key_comiket_id), COMIKET_ID);
        } else {
            prefs.edit().putInt(getString(R.string.prefs_key_comiket_id), COMIKET_ID).apply();
        }

        if (prefs.contains(getString(R.string.prefs_key_comic1_id))) {
            COMIC1_ID = prefs.getInt(getString(R.string.prefs_key_comic1_id), COMIC1_ID);
        } else {
            prefs.edit().putInt(getString(R.string.prefs_key_comic1_id), COMIC1_ID).apply();
        }
    }
}
