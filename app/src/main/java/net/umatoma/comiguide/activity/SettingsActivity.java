package net.umatoma.comiguide.activity;

import android.app.Activity;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.model.User;

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

        private User mUser;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.account_preferences);

            mUser = new User(getActivity());

            Preference logoutPref = findPreference(getString(R.string.prefs_key_account_logout));
            logoutPref.setSummary(mUser.getUserName());
            logoutPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    mUser.delete();
                    Toast.makeText(getActivity(), getString(R.string.toast_logout), Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                    return false;
                }
            });
        }
    }
}
