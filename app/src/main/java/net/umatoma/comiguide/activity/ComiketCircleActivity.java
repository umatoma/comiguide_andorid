package net.umatoma.comiguide.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import net.umatoma.comiguide.ComiGuide;
import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.ComiketCircleArrayAdapter;
import net.umatoma.comiguide.api.ComiGuideApiClient;
import net.umatoma.comiguide.api.OnApiClientPostExecuteListener;
import net.umatoma.comiguide.fragment.ComiketCIrcleMapDialogFragment;
import net.umatoma.comiguide.fragment.ComiketCircleFormFragment;
import net.umatoma.comiguide.fragment.ComiketCircleListFragment;
import net.umatoma.comiguide.fragment.ComiketCircleMapFragment;
import net.umatoma.comiguide.fragment.ComiketCircleMenuDialogFragment;
import net.umatoma.comiguide.fragment.OnComiketCircleCreateListener;
import net.umatoma.comiguide.fragment.OnComiketCircleSelectListener;
import net.umatoma.comiguide.fragment.OnComiketCircleUpdateListener;
import net.umatoma.comiguide.fragment.OnMenuDialogSelectListener;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.model.ComiketLayout;
import net.umatoma.comiguide.util.ComiketCircleMapSharedPref;
import net.umatoma.comiguide.util.IntentUtil;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ComiketCircleActivity extends MapActivity
        implements ComiketCircleMapFragment.OnFooterViewClickListener,
            OnComiketCircleUpdateListener, OnComiketCircleCreateListener, OnComiketCircleSelectListener {

    public static final String TAG = "ComiketCircleActivity";
    private int mComiketId;
    private int mCmapId;
    private int mDay;
    private ComiketCircleArrayAdapter mCircleArrayAdapter;
    private ComiGuideApiClient.HttpClientTask mLoadComiketCirclesTask;
    private ComiGuideApiClient.HttpClientTask mDeleteCircleTask;
    private ComiketCircleListFragment mCircleListFragment;
    private ComiketCircleMapFragment mMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCircleArrayAdapter = new ComiketCircleArrayAdapter(this);

        int comiket_id = ComiGuide.COMIKET_ID;
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

        mCircleListFragment = ComiketCircleListFragment.newInstance(mCircleArrayAdapter);
        mCircleListFragment.setOnComiketCircleSelectListener(this);
        setCircleListFragment(mCircleListFragment);

        mMapFragment = ComiketCircleMapFragment.newInstance(mComiketId, mCmapId, mDay);
        mMapFragment.setComiketCircleArrayAdapter(mCircleArrayAdapter);
        setMapFragment(mMapFragment);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, mMapFragment, ComiketCircleMapFragment.TAG);
        transaction.replace(R.id.left_drawer, mCircleListFragment, ComiketCircleListFragment.TAG);
        transaction.commit();

        loadComiketCircles(mComiketId, mDay, mCmapId);
    }

    @Override
    protected void onCircleListRefresh() {
        loadComiketCircles(mComiketId, mDay, mCmapId);
    }

    @Override
    public void onCreateButtonClick(View v) {
        showComiketCircleCreateForm();
    }

    @Override
    public void onChangeMapButtonClick(View v) {
        showSelectMapDialog();
    }

    @Override
    public void onShowListButtonClick(View v) {
        getDrawerLayout().openDrawer(Gravity.LEFT);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentByTag(ComiketCircleFormFragment.TAG);
            if (getDrawerLayout().isDrawerOpen(Gravity.LEFT)) {
                getDrawerLayout().closeDrawer(Gravity.LEFT);
                return false;
            } else if (fragment != null) {
                manager.beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .remove(fragment)
                        .commit();
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
    public void onCircleSelect(ComiketCircle circle) {
        showCircleInfo(circle);
    }

    @Override
    public void onFooterViewClick(ComiketCircle circle) {
        showCircleEditFragment(circle);
    }

    @Override
    public void onFooterViewLongClick(final ComiketCircle circle) {
        Log.d(TAG, "onFooterViewLongClick");

        ComiketCircleMenuDialogFragment fragment = ComiketCircleMenuDialogFragment.newInstance(circle);
        fragment.setOnMenuDialogSelectListener(new OnMenuDialogSelectListener() {
            @Override
            public void onMenuSelect(int menuId) {
                switch (menuId) {
                    case ComiketCircleMenuDialogFragment.MENU_MAP:
                        showCircleInfo(circle);
                        return;
                    case ComiketCircleMenuDialogFragment.MENU_OPEN_URL:
                        if (IntentUtil.checkImplicitIntent(ComiketCircleActivity.this, circle.getCircleUrl())) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(circle.getCircleUrl())));
                        }
                        return;
                    case ComiketCircleMenuDialogFragment.MENU_EDIT:
                        showCircleEditFragment(circle);
                        return;
                    case ComiketCircleMenuDialogFragment.MENU_DELETE:
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

        mMapFragment.setComiketCircle(circle);
        mMapFragment.showFooterView();
    }

    @Override
    public void onComiketCircleCreate(ComiketCircle circle) {
        mCircleArrayAdapter.add(circle);
    }

    private void loadComiketCircles(int comiket_id, int day, int cmap_id) {
        String path = String.format("api/v1/comikets/%d/ccircle_checklists", comiket_id);
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("day", String.valueOf(day)));
        params.add(new BasicNameValuePair("cmap_id", String.valueOf(cmap_id)));
        mLoadComiketCirclesTask = new ComiGuideApiClient(this).callGetTask(path, params);
        mLoadComiketCirclesTask.setOnApiClientPostExecuteListener(
                new OnApiClientPostExecuteListener() {

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
        ComiketCircleFormFragment fragment = ComiketCircleFormFragment.newInstance(mComiketId, mDay, mCmapId);
        fragment.setOnComiketCircleCreateListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.content_frame, fragment, ComiketCircleFormFragment.TAG)
                .commit();
    }

    private void showSelectMapDialog() {
        ComiketCIrcleMapDialogFragment fragment = ComiketCIrcleMapDialogFragment.newInstance();
        fragment.setOnComiketCircleMapSelectListener(new ComiketCIrcleMapDialogFragment.OnComiketCircleMapSelectListener() {
            @Override
            public void onSelect(int day, int cmap_id) {
                initialize(mComiketId, day, cmap_id);
            }
        });
        fragment.show(getSupportFragmentManager(), ComiketCIrcleMapDialogFragment.TAG);
    }

    private void showCircleInfo(ComiketCircle circle) {
        ComiketLayout layout = circle.getComiketLayout();
        float dx = (float) layout.getPosX();
        float dy = (float) layout.getPosY();

        mMapFragment.setMapPosition(dx, dy);
        mMapFragment.setComiketCircle(circle);
        mMapFragment.showFooterView();

        closeDrawers();
    }

    private void showCircleEditFragment(ComiketCircle circle) {
        ComiketCircleFormFragment fragment = ComiketCircleFormFragment.newInstance(circle);
        fragment.setOnComiketCircleUpdateListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.content_frame, fragment, ComiketCircleFormFragment.TAG)
                .commit();
    }

    private void deleteCircle(final ComiketCircle circle) {
        String path = String.format("api/v1/ccircle_checklists/%d", circle.getId());
        mDeleteCircleTask = new ComiGuideApiClient(this).callDeleteTask(path);
        mDeleteCircleTask.setOnApiClientPostExecuteListener(new OnApiClientPostExecuteListener() {
            @Override
            public void onSuccess(JSONObject result) {
                mCircleArrayAdapter.remove(circle);

                if (mMapFragment != null) {
                    mMapFragment.hideFooterView(circle);
                }

                Toast.makeText(ComiketCircleActivity.this,
                        getString(R.string.message_success_circle_delete), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail() {
                Toast.makeText(ComiketCircleActivity.this,
                        getString(R.string.message_error_common), Toast.LENGTH_SHORT).show();
            }
        });
        mDeleteCircleTask.setProgressDialog(this);
        mDeleteCircleTask.execute();
    }
}
