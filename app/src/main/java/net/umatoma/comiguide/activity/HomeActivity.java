package net.umatoma.comiguide.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.NotificationListAdapter;
import net.umatoma.comiguide.api.ComiGuideApiClient;
import net.umatoma.comiguide.api.OnApiClientPostExecuteListener;
import net.umatoma.comiguide.fragment.NotificationDialogFragment;
import net.umatoma.comiguide.fragment.SideMenuFragment;
import net.umatoma.comiguide.model.Notification;
import net.umatoma.comiguide.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends ActionBarActivity {

    private static final String TAG = "HomeActivity";
    private User mUser;
    private View mListHeaderView;
    private ListView mNotificationList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private NotificationListAdapter mNotificationAdaper;
    private ActionBar mActionBar;
    private ComiGuideApiClient.HttpClientTask mLoadNotificationsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setScrimColor(getResources().getColor(R.color.drawerLayoutScrim));
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mNotificationList = (ListView) findViewById(R.id.notification_list);

        mUser = new User(this);
        mListHeaderView = getLayoutInflater().inflate(R.layout.header_notification_list, null);
        ((TextView) mListHeaderView.findViewById(R.id.user_name)).setText(mUser.getUserName());
        mNotificationList.addHeaderView(mListHeaderView);

        mNotificationAdaper = new NotificationListAdapter(this);
        mNotificationList.setAdapter(mNotificationAdaper);
        mNotificationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Notification notification = (Notification) parent.getItemAtPosition(position);
                if (notification != null) {
                    showNotificationDialog(notification);
                }
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.left_drawer, SideMenuFragment.newInstance());
        transaction.commit();

        loadNotifications();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mUser.isLoggedIn(this)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private void loadNotifications() {
        String path = "api/v1/notifications";
        mLoadNotificationsTask = new ComiGuideApiClient(this).callGetTask(path);
        mLoadNotificationsTask.setOnApiClientPostExecuteListener(new OnApiClientPostExecuteListener() {
            @Override
            public void onSuccess(JSONObject result) {
                mNotificationAdaper.clear();
                try {
                    JSONArray notifications = result.getJSONArray("notifications");
                    int length = notifications.length();
                    for (int i = 0; i < length; i++) {
                        Notification notification = new Notification(notifications.getJSONObject(i));
                        mNotificationAdaper.add(notification);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(HomeActivity.this, "Fail to load...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail() {
                Toast.makeText(HomeActivity.this,
                        getString(R.string.message_error_common), Toast.LENGTH_SHORT).show();
            }
        });
        mLoadNotificationsTask.setProgressDialog(this);
        mLoadNotificationsTask.execute();
    }

    private void showNotificationDialog(Notification notification) {
        NotificationDialogFragment fragment = NotificationDialogFragment.newInstance(notification);
        fragment.show(getSupportFragmentManager(), NotificationDialogFragment.TAG);
    }

}
