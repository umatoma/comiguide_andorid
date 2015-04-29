package net.umatoma.comiguide.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import net.umatoma.comiguide.ComiGuide;
import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.Comic1CircleAdapter;
import net.umatoma.comiguide.api.ComiGuideApiClient;
import net.umatoma.comiguide.api.OnApiClientPostExecuteListener;
import net.umatoma.comiguide.fragment.Comic1CircleFormFragment;
import net.umatoma.comiguide.fragment.Comic1CircleListFragment;
import net.umatoma.comiguide.fragment.Comic1CircleMapFragment;
import net.umatoma.comiguide.fragment.Comic1CircleMenuDialogFragment;
import net.umatoma.comiguide.fragment.ComiketCircleMenuDialogFragment;
import net.umatoma.comiguide.fragment.OnComic1CircleCreateListener;
import net.umatoma.comiguide.fragment.OnComic1CircleSelectListener;
import net.umatoma.comiguide.fragment.OnComic1CircleUpdateListener;
import net.umatoma.comiguide.fragment.OnMenuDialogSelectListener;
import net.umatoma.comiguide.model.Comic1Circle;
import net.umatoma.comiguide.model.Comic1Layout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Comic1CircleActivity extends MapActivity
        implements Comic1CircleMapFragment.OnFooterViewClickListener, OnComic1CircleSelectListener, OnComic1CircleCreateListener, OnComic1CircleUpdateListener {

    private int mComic1Id;
    private Comic1CircleMapFragment mMapFragment;
    private Comic1CircleListFragment mCircleListFragment;
    private Comic1CircleAdapter mCircleAdapter;
    private ComiGuideApiClient.HttpClientTask mLoadCirclesTask;
    private ComiGuideApiClient.HttpClientTask mDeleteCircleTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCircleAdapter = new Comic1CircleAdapter(this);

        initialize(ComiGuide.COMIC1_ID);
    }

    private void initialize(int comic1_id) {
        mComic1Id = comic1_id;
        getSupportActionBar().setTitle(String.format("COMIC1☆%d", mComic1Id));

        mCircleListFragment = Comic1CircleListFragment.newInstance(mCircleAdapter);
        mCircleListFragment.setOnComic1CircleSelectListener(this);
        setCircleListFragment(mCircleListFragment);

        mMapFragment = Comic1CircleMapFragment.newInstance(mComic1Id);
        mMapFragment.setCircleAdapter(mCircleAdapter);
        mMapFragment.setOnFooterViewClickListener(this);
        setMapFragment(mMapFragment);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, mMapFragment, Comic1CircleMapFragment.TAG);
        transaction.replace(R.id.left_drawer, mCircleListFragment, Comic1CircleListFragment.TAG);
        transaction.commit();

        loadCircles(mComic1Id);
    }

    private void loadCircles(int comic1_id) {
        String path = String.format("api/v1/comic1s/%d/c1circle_checklists", comic1_id);
        mLoadCirclesTask = new ComiGuideApiClient(this).callGetTask(path);
        mLoadCirclesTask.setOnApiClientPostExecuteListener(
                new OnApiClientPostExecuteListener() {

                    @Override
                    public void onSuccess(JSONObject result) {
                        mLoadCirclesTask = null;
                        mCircleAdapter.clear();
                        parseCircles(result);
                        setRefreshing(false);
                    }

                    @Override
                    public void onFail() {
                        mLoadCirclesTask = null;
                        Toast.makeText(Comic1CircleActivity.this, "Fail to load...", Toast.LENGTH_SHORT).show();
                        setRefreshing(false);
                    }
                })
                .setProgressDialog(this)
                .execute();
    }

    private void parseCircles(JSONObject result) {
        if (result != null) {
            try {
                JSONArray checklists = result.getJSONArray("c1circle_checklists");
                int length = checklists.length();
                for (int i = 0; i < length; i++) {
                    JSONObject circle = checklists.getJSONObject(i);
                    mCircleAdapter.add(new Comic1Circle(circle));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(Comic1CircleActivity.this, "Fail to load...", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCircle(final Comic1Circle circle) {
        String path = String.format("api/v1/c1circle_checklists/%d", circle.getId());
        mDeleteCircleTask = new ComiGuideApiClient(this).callDeleteTask(path);
        mDeleteCircleTask.setOnApiClientPostExecuteListener(new OnApiClientPostExecuteListener() {
            @Override
            public void onSuccess(JSONObject result) {
                mCircleAdapter.remove(circle);

                if (mMapFragment != null) {
                    mMapFragment.hideFooterView(circle);
                }

                Toast.makeText(Comic1CircleActivity.this,
                        getString(R.string.message_success_circle_delete), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail() {
                Toast.makeText(Comic1CircleActivity.this,
                        getString(R.string.message_error_common), Toast.LENGTH_SHORT).show();
            }
        });
        mDeleteCircleTask.setProgressDialog(this);
        mDeleteCircleTask.execute();
    }

    private void showCircleInfo(Comic1Circle circle) {
        Comic1Layout layout = circle.getComic1Layout();
        float dx = (float) layout.getMapPosX();
        float dy = (float) layout.getMapPosY();

        mMapFragment.setMapPosition(dx, dy);
        mMapFragment.setCircle(circle);
        mMapFragment.showFooterView();

        closeDrawers();
    }

    private void showCircleEditFragment(Comic1Circle circle) {
        Comic1CircleFormFragment fragment = Comic1CircleFormFragment.newInstance(circle);
        fragment.setOnComic1CircleUpdateListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.content_frame, fragment, Comic1CircleFormFragment.TAG)
                .commit();
    }

    private void showComic1CircleCreateForm() {
        Comic1CircleFormFragment fragment = Comic1CircleFormFragment.newInstance(mComic1Id);
        fragment.setOnComic1CircleCreateListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.content_frame, fragment, Comic1CircleFormFragment.TAG)
                .commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            FragmentManager manager = getSupportFragmentManager();
            Fragment fragment = manager.findFragmentByTag(Comic1CircleFormFragment.TAG);
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
    protected void onCircleListRefresh() {
        loadCircles(mComic1Id);
    }

    @Override
    public void onCreateButtonClick(View v) {
        showComic1CircleCreateForm();
    }

    @Override
    public void onChangeMapButtonClick(View v) {
        // showSelectMapDialog();
    }

    @Override
    public void onShowListButtonClick(View v) {
        getDrawerLayout().openDrawer(Gravity.LEFT);
    }

    @Override
    public void onFooterViewClick(Comic1Circle circle) {
        showCircleEditFragment(circle);
    }

    @Override
    public void onFooterViewLongClick(final Comic1Circle circle) {
        Comic1CircleMenuDialogFragment fragment = Comic1CircleMenuDialogFragment.newInstance(circle);
        fragment.setOnMenuDialogSelectListener(new OnMenuDialogSelectListener() {
            @Override
            public void onMenuSelect(int menuId) {
                switch (menuId) {
                    case ComiketCircleMenuDialogFragment.MENU_MAP:
                        showCircleInfo(circle);
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
    public void onCircleSelect(Comic1Circle circle) {
        showCircleInfo(circle);
    }


    @Override
    public void onComic1CircleCreate(Comic1Circle circle) {
        mCircleAdapter.add(circle);
        mMapFragment.setCircle(circle);
        mMapFragment.showFooterView();
    }

    @Override
    public void onComic1CircleUpdate(Comic1Circle circle) {
        mCircleAdapter.updateItem(circle);
        mMapFragment.setCircle(circle);
        mMapFragment.showFooterView();
    }
}
