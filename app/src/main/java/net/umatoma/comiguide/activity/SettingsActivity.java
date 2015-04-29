package net.umatoma.comiguide.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import net.umatoma.comiguide.ComiGuide;
import net.umatoma.comiguide.R;
import net.umatoma.comiguide.model.User;

public class SettingsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(R.id.content_prefs_application, new ApplicationPreferenceFragment())
                .commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class ApplicationPreferenceFragment extends PreferenceFragment {

        private static final String FILE_PATH_CONTACT = "file:///android_asset/activity_settings/contact.html";
        private static final String FILE_PATH_LICENSE = "file:///android_asset/activity_settings/license.html";
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

            // App Version
            Preference versionPref = findPreference(getString(R.string.prefs_key_version));
            versionPref.setSummary(getAppVersionName());

            // Help
            Preference helpPref = findPreference(getString(R.string.prefs_key_help));
            helpPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Toast.makeText(getActivity(), "作成中です。", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            // Contact
            Preference contactPref = findPreference(getString(R.string.prefs_key_contact));
            contactPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(WebViewActivity.IKEY_TITLE, getString(R.string.prefs_title_contact));
                    intent.putExtra(WebViewActivity.IKEY_FILE_PATH, FILE_PATH_CONTACT);
                    startActivity(intent);
                    return false;
                }
            });

            //License
            Preference licensePref = findPreference(getString(R.string.prefs_key_license));
            licensePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    intent.putExtra(WebViewActivity.IKEY_TITLE, getString(R.string.prefs_title_license));
                    intent.putExtra(WebViewActivity.IKEY_FILE_PATH, FILE_PATH_LICENSE);
                    startActivity(intent);
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

        private String getAppVersionName() {
            Context context = getActivity();
            PackageManager pm = context.getPackageManager();
            String versionName = "unknown";

            try {
                PackageInfo packageInfo
                        = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
                versionName = packageInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            return versionName;
        }
    }
}
