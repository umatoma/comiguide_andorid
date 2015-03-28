package net.umatoma.comiguide.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import net.umatoma.comiguide.ComiGuide;
import net.umatoma.comiguide.R;
import net.umatoma.comiguide.model.User;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction()
                .replace(R.id.content_prefs_application, new ApplicationPreferenceFragment())
                .commit();
    }

    public static class ApplicationPreferenceFragment extends PreferenceFragment {

        private AlertDialog mComiketIdDialog;
        private Preference mComiketIdPref;
        private AlertDialog mComic1IdDialog;
        private Preference mComic1IdPref;
        private User mUser;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.application_preferences);

            // COMIKET_ID
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

            // COMIC1_ID
            mComic1IdDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(getString(R.string.prefs_title_comic1_id))
                    .setItems(R.array.prefs_entries_comic1_id, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int[] values = getResources().getIntArray(R.array.prefs_entry_values_comic1_id);
                            storeComic1Id(values[which]);
                        }
                    })
                    .setNegativeButton(R.string.dialog_cancel, null)
                    .create();

            mComic1IdPref = findPreference(getString(R.string.prefs_key_comic1_id));
            mComic1IdPref.setSummary(String.format("COMIC1☆%d", ComiGuide.COMIC1_ID));
            mComic1IdPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    mComic1IdDialog.show();
                    return true;
                }
            });

            // ACCOUNT
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

        private void storeComiketId(int comiket_id) {
            getPreferenceManager()
                    .getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putInt(getString(R.string.prefs_key_comiket_id), comiket_id)
                    .apply();
            ComiGuide.COMIKET_ID = comiket_id;
            mComiketIdPref.setSummary(String.format("C%d", comiket_id));
        }

        private void storeComic1Id(int comic1_id) {
            getPreferenceManager()
                    .getDefaultSharedPreferences(getActivity())
                    .edit()
                    .putInt(getString(R.string.prefs_key_comic1_id), comic1_id)
                    .apply();
            ComiGuide.COMIC1_ID = comic1_id;
            mComic1IdPref.setSummary(String.format("COMIC1☆%d", comic1_id));
        }
    }
}
