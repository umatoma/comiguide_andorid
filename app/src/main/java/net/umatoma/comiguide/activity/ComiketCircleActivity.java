package net.umatoma.comiguide.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.ComiketCircleArrayAdapter;
import net.umatoma.comiguide.api.ComiGuideApiClient;
import net.umatoma.comiguide.fragment.ComiketCircleFormFragment;
import net.umatoma.comiguide.fragment.ComiketCircleListFragment;
import net.umatoma.comiguide.fragment.ComiketCircleMapFragment;
import net.umatoma.comiguide.fragment.OnComiketCircleCreateListener;
import net.umatoma.comiguide.fragment.OnComiketCircleUpdateListener;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.model.ComiketLayout;
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

    private int mComiketId = 87;
    private int mCmapId = 1;
    private int mDay = 1;
    private DrawerLayout mDrawerLayout;
    private ComiketCircleArrayAdapter mCircleArrayAdapter;
    private ComiGuideApiClient.HttpClientTask mLoadComiketCirclesTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comiket_circle);

        mCircleArrayAdapter = new ComiketCircleArrayAdapter(this);

        String path = String.format("api/v1/comikets/%d/ccircle_checklists", mComiketId);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("cmap_id", String.valueOf(mCmapId)));
        mLoadComiketCirclesTask = new ComiGuideApiClient(this).callGetTask(path, params);
        mLoadComiketCirclesTask.setOnHttpClientPostExecuteListener(
                new ComiGuideApiClient.OnHttpClientPostExecuteListener() {

            @Override
            public void onSuccess(JSONObject result) {
                mLoadComiketCirclesTask = null;
                parseComiketCircles(result);
            }

            @Override
            public void onFail() {
                mLoadComiketCirclesTask = null;
                Toast.makeText(ComiketCircleActivity.this, "Fail to load...", Toast.LENGTH_SHORT).show();
            }
        }).execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame,
                new ComiketCircleMapFragment(), ComiketCircleMapFragment.TAG);
        transaction.replace(R.id.left_drawer,
                new ComiketCircleListFragment(mCircleArrayAdapter), ComiketCircleListFragment.TAG);
        transaction.commit();
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
        super.onStop();
    }

    @Override
    public void onFunctionsButtonClicke(int id) {
        switch (id) {
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

    @Override
    public void onFooterViewClick(ComiketCircle circle) {
        ComiketCircleFormFragment fragment = new ComiketCircleFormFragment(circle)
                .setOnComiketCircleUpdateListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.content_frame, fragment, ComiketCircleFormFragment.TAG)
                .commit();
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
}
