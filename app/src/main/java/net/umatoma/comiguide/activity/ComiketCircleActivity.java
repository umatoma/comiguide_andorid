package net.umatoma.comiguide.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.ComiketCircleArrayAdapter;
import net.umatoma.comiguide.adapter.MenuListAdapter;
import net.umatoma.comiguide.api.ComiGuideApiClient;
import net.umatoma.comiguide.fragment.ComiketCIrcleMapDialogFragment;
import net.umatoma.comiguide.fragment.ComiketCircleFormFragment;
import net.umatoma.comiguide.fragment.ComiketCircleListFragment;
import net.umatoma.comiguide.fragment.ComiketCircleMapFragment;
import net.umatoma.comiguide.fragment.ComiketCircleMenuDialogFragment;
import net.umatoma.comiguide.fragment.OnComiketCircleCreateListener;
import net.umatoma.comiguide.fragment.OnComiketCircleUpdateListener;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.model.ComiketLayout;
import net.umatoma.comiguide.util.ComiketCircleMapSharedPref;
import net.umatoma.comiguide.view.MapImageView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComiketCircleActivity extends ActionBarActivity
        implements ComiketCircleMapFragment.OnFragmentInteractionListener,
            ComiketCircleListFragment.OnFragmentInteractionListener,
            OnComiketCircleUpdateListener, OnComiketCircleCreateListener {

    public static final String TAG = "ComiketCircleActivity";
    private int mComiketId;
    private int mCmapId;
    private int mDay;
    private DrawerLayout mDrawerLayout;
    private ComiketCircleArrayAdapter mCircleArrayAdapter;
    private ComiGuideApiClient.HttpClientTask mLoadComiketCirclesTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comiket_circle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mCircleArrayAdapter = new ComiketCircleArrayAdapter(this);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        int comiket_id = 87;
        ComiketCircleMapSharedPref pref = new ComiketCircleMapSharedPref(this);
        int day = pref.getDayHistory(1);
        int cmap_id = pref.getMapIdHistory(1);

        initialize(comiket_id, day, cmap_id);
    }

    private void initialize(int comiket_id, int day, int cmap_id) {
        mComiketId = comiket_id;
        mDay = day;
        mCmapId = cmap_id;

        setTitle(comiket_id, day, cmap_id);

        ComiketCircleListFragment listFragment
                = ComiketCircleListFragment.newInstance(mCircleArrayAdapter);
        listFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadComiketCircles(mComiketId, mDay, mCmapId);
            }
        });
        ComiketCircleMapFragment mapFragment
                = ComiketCircleMapFragment.newInstance(mComiketId, mCmapId, mDay);
        mapFragment.setComiketCircleArrayAdapter(mCircleArrayAdapter);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, mapFragment, ComiketCircleMapFragment.TAG);
        transaction.replace(R.id.left_drawer, listFragment, ComiketCircleListFragment.TAG);
        transaction.commit();

        loadComiketCircles(mComiketId, mDay, mCmapId);
    }

    private void setTitle(int comiket_id, int day, int cmap_id) {
        String map_name = getResources()
                .getStringArray(R.array.comiket_circle_maps)[cmap_id - 1];
        String title = new StringBuilder()
                .append("C")
                .append(comiket_id)
                .append(" ")
                .append(day)
                .append("日目")
                .append(" ")
                .append(map_name)
                .toString();
        getSupportActionBar().setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comiket_circle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentByTag(ComiketCircleFormFragment.TAG);
            if (fragment != null) {
                manager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .remove(fragment)
                        .commit();
                return false;
            } else if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onStop() {
        mLoadComiketCirclesTask = null;

        ComiketCircleMapSharedPref pref = new ComiketCircleMapSharedPref(this);
        pref.setDayHistory(mDay);
        pref.setMapIdHistory(mCmapId);

        super.onStop();
    }

    @Override
    public void onFunctionsButtonClicke(int id) {
        switch (id) {
            case R.id.button_change_map:
                showSelectMapDialog();
                return;
            case R.id.button_circle_list:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                return;
            case R.id.button_create_circle:
                showComiketCircleCreateForm();
                return;
        }
    }

    @Override
    public void onComiketCircleSelected(ComiketCircle circle) {
        showCircleInfo(circle);
    }

    @Override
    public void onFooterViewClick(ComiketCircle circle) {
        showCircleEditFragment(circle);
    }

    @Override
    public void onFooterViewLongClick(final ComiketCircle circle) {
        Log.d(TAG, "onFooterViewLongClick");
        ArrayList<MenuListAdapter.MenuOption> options = new ArrayList<>();
        options.add(new MenuListAdapter.MenuOption(
                R.drawable.ic_map_marker, getString(R.string.dialog_comiket_circle_menu_show)));
        options.add(new MenuListAdapter.MenuOption(
                R.drawable.ic_edit, getString(R.string.dialog_comiket_circle_menu_edit)));
        options.add(new MenuListAdapter.MenuOption(
                R.drawable.ic_delete, getString(R.string.dialog_comiket_circle_menu_delete)));

        ComiketCircleMenuDialogFragment fragment = ComiketCircleMenuDialogFragment.newInstance(circle);
        fragment.setMenuOptions(options);
        fragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        showCircleInfo(circle);
                        return;
                    case 1:
                        showCircleEditFragment(circle);
                        return;
                    case 2:
                        deleteCircle(circle);
                        return;
                }
            }
        });
        fragment.show(getSupportFragmentManager(), ComiketCircleMenuDialogFragment.TAG);
    }

    @Override
    public void onComiketCircleUpdate(ComiketCircle circle) {
        mCircleArrayAdapter.updateItem(circle);

        FragmentManager manager = getSupportFragmentManager();
        ComiketCircleMapFragment fragment
                = (ComiketCircleMapFragment) manager.findFragmentByTag(ComiketCircleMapFragment.TAG);
        if (fragment != null) {
            fragment.setComiketCircle(circle);
            fragment.showFooterView();
        }
    }

    @Override
    public void onComiketCircleCreate(ComiketCircle circle) {
        mCircleArrayAdapter.add(circle);
    }

    private void setRefreshing(boolean refreshing) {
        FragmentManager manager = getSupportFragmentManager();
        ComiketCircleListFragment fragment
                = (ComiketCircleListFragment) manager.findFragmentByTag(ComiketCircleListFragment.TAG);
        if (fragment != null) {
            fragment.setRefreshing(refreshing);
        }
    }

    private void loadComiketCircles(int comiket_id, int day, int cmap_id) {
        String path = String.format("api/v1/comikets/%d/ccircle_checklists", comiket_id);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("day", String.valueOf(day)));
        params.add(new BasicNameValuePair("cmap_id", String.valueOf(cmap_id)));
        mLoadComiketCirclesTask = new ComiGuideApiClient(this).callGetTask(path, params);
        mLoadComiketCirclesTask.setOnHttpClientPostExecuteListener(
                new ComiGuideApiClient.OnHttpClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mLoadComiketCirclesTask = null;
                        mCircleArrayAdapter.clear();
                        parseComiketCircles(result);
                        setRefreshing(false);
                    }

                    @Override
                    public void onFail() {
                        mLoadComiketCirclesTask = null;
                        Toast.makeText(ComiketCircleActivity.this, "Fail to load...", Toast.LENGTH_SHORT).show();
                        setRefreshing(false);
                    }
                })
                .setProgressDialog(this)
                .execute();
    }

    private void parseComiketCircles(JSONObject result) {
        if (result != null) {
            try {
                JSONArray comiketCircles = result.getJSONArray("ccircle_checklists");
                int length = comiketCircles.length();
                for (int i = 0; i < length; i++) {
                    JSONObject comiketCircle = comiketCircles.getJSONObject(i);
                    mCircleArrayAdapter.add(new ComiketCircle(comiketCircle));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(ComiketCircleActivity.this, "Fail to load...", Toast.LENGTH_SHORT).show();
        }
    }

    private void showComiketCircleCreateForm() {
        ComiketCircleFormFragment fragment = new ComiketCircleFormFragment(mComiketId, mDay, mCmapId);
        fragment.setOnComiketCircleCreateListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.content_frame, fragment, ComiketCircleFormFragment.TAG)
                .commit();
    }

    private void showSelectMapDialog() {
        ComiketCIrcleMapDialogFragment.newInstance()
                .setOnComiketCircleMapSelectListener(new ComiketCIrcleMapDialogFragment.OnComiketCircleMapSelectListener() {
                    @Override
                    public void onSelect(int day, int cmap_id) {
                        initialize(mComiketId, day, cmap_id);
                    }
                })
                .show(getSupportFragmentManager(), ComiketCIrcleMapDialogFragment.TAG);
    }

    private void showCircleInfo(ComiketCircle circle) {
        FragmentManager manager = getSupportFragmentManager();
        ComiketCircleMapFragment fragment
                = (ComiketCircleMapFragment) manager.findFragmentByTag(ComiketCircleMapFragment.TAG);

        if (fragment != null) {
            MapImageView mapImageView = (MapImageView) findViewById(R.id.circle_map);
            ComiketLayout layout = circle.getComiketLayout();
            float dx = (float) layout.getPosX();
            float dy = (float) layout.getPosY();
            mapImageView.setCurrentPosition(dx, dy);

            fragment.setComiketCircle(circle);
            fragment.showFooterView();
        } else {
            // On edit or create circle mode.
        }

        mDrawerLayout.closeDrawers();
    }

    private void showCircleEditFragment(ComiketCircle circle) {
        ComiketCircleFormFragment fragment = new ComiketCircleFormFragment(circle)
                .setOnComiketCircleUpdateListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.content_frame, fragment, ComiketCircleFormFragment.TAG)
                .commit();
    }

    private void deleteCircle(ComiketCircle circle) {

    }
}
