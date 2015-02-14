package net.umatoma.comiguide.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.umatoma.comiguide.R;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(R.id.content_prefs_account, new AccountPreferenceFragment())
                .replace(R.id.content_prefs_application, new ApplicationPreferenceFragment())
                .commit();
    }

    public static class ApplicationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.application_preferences);
        }
    }

    public static class AccountPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.account_preferences);
        }
    }
}
