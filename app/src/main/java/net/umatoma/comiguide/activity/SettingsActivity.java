package net.umatoma.comiguide.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import net.umatoma.comiguide.ComiGuide;
import net.umatoma.comiguide.R;
import net.umatoma.comiguide.model.User;

import java.util.Arrays;

public class SettingsActivity extends ActionBarActivity {

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

        private AlertDialog mComiketIdDialog;
        private Preference mComiketIdPref;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.application_preferences);

            mComiketIdDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.prefs_title_comiket_id))
                    .setItems(R.array.prefs_entries_comiket_id, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int[] values = getResources().getIntArray(R.array.prefs_entry_values_comiket_id);
                            storeComiketId(values[which]);
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, null)
                    .create();

            mComiketIdPref = findPreference(getString(R.string.prefs_key_comiket_id));
            mComiketIdPref.setSummary(String.format("C%d", ComiGuide.COMIKET_ID));
            mComiketIdPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    mComiketIdDialog.show();
                    return true;
                }
            });
        }

        private void storeComiketId(int comiket_id) {
            getPreferenceManager()
                    .getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putInt(getString(R.string.prefs_key_comiket_id), comiket_id)
                    .apply();
            ComiGuide.COMIKET_ID = comiket_id;
            mComiketIdPref.setSummary(String.format("C%d", comiket_id));
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
