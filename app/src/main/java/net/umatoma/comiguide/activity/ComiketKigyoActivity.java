package net.umatoma.comiguide.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import net.umatoma.comiguide.ComiGuide;
import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.ComiketKigyoChecklistAdapter;
import net.umatoma.comiguide.api.ComiGuideApiClient;
import net.umatoma.comiguide.fragment.ComiketKigyoMapFragment;
import net.umatoma.comiguide.model.ComiketKigyo;
import net.umatoma.comiguide.model.ComiketKigyoChecklist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ComiketKigyoActivity extends MapActivity
        implements ComiketKigyoMapFragment.OnFooterViewClickListener {

    private int mComiketId;
    private ComiketKigyoMapFragment mMapFragment;
    private ComiketKigyoChecklistAdapter mCircleAdapter;
    private ComiGuideApiClient.HttpClientTask mLoadCirclesTask;
    private ComiGuideApiClient.HttpClientTask mDeleteCircleTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCircleAdapter = new ComiketKigyoChecklistAdapter(this);

        initialize(ComiGuide.COMIKET_ID);
    }

    private void initialize(int comic1_id) {
        mComiketId = comic1_id;
        getSupportActionBar().setTitle(String.format("C%d 企業ブース", mComiketId));

//        mCircleListFragment = ComiketKigyoListFragment.newInstance(mCircleAdapter);
//        mCircleListFragment.setOnComiketKigyoChecklistSelectListener(this);
//        setCircleListFragment(mCircleListFragment);

        mMapFragment = ComiketKigyoMapFragment.getInstance(mComiketId);
        mMapFragment.setCircleAdapter(mCircleAdapter);
        mMapFragment.setOnFooterViewClickListener(this);
        setMapFragment(mMapFragment);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, mMapFragment, ComiketKigyoMapFragment.TAG);
//        transaction.replace(R.id.left_drawer, mCircleListFragment, ComiketKigyoListFragment.TAG);
        transaction.commit();

        loadCircles(mComiketId);
    }

    private void loadCircles(int comic1_id) {
        String path = String.format("api/v1/comikets/%d/ckigyo_checklists", comic1_id);
        mLoadCirclesTask = new ComiGuideApiClient(this).callGetTask(path);
        mLoadCirclesTask.setOnHttpClientPostExecuteListener(
                new ComiGuideApiClient.OnHttpClientPostExecuteListener() {

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
                        Toast.makeText(ComiketKigyoActivity.this, "Fail to load...", Toast.LENGTH_SHORT).show();
                        setRefreshing(false);
                    }
                })
                .setProgressDialog(this)
                .execute();
    }

    private void parseCircles(JSONObject result) {
        if (result != null) {
            try {
                JSONArray checklists = result.getJSONArray("ckigyo_checklists");
                int length = checklists.length();
                for (int i = 0; i < length; i++) {
                    JSONObject checklist = checklists.getJSONObject(i);
                    mCircleAdapter.add(new ComiketKigyoChecklist(checklist));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(ComiketKigyoActivity.this, "Fail to load...", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCircle(final ComiketKigyoChecklist circle) {
        String path = String.format("api/v1/ckigyo_checklists/%d", circle.getId());
        mDeleteCircleTask = new ComiGuideApiClient(this).callDeleteTask(path);
        mDeleteCircleTask.setOnHttpClientPostExecuteListener(new ComiGuideApiClient.OnHttpClientPostExecuteListener() {
            @Override
            public void onSuccess(JSONObject result) {
                mCircleAdapter.remove(circle);

                if (mMapFragment != null) {
                    mMapFragment.hideFooterView(circle);
                }

                Toast.makeText(ComiketKigyoActivity.this,
                        getString(R.string.message_success_circle_delete), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFail() {
                Toast.makeText(ComiketKigyoActivity.this,
                        getString(R.string.message_error_common), Toast.LENGTH_SHORT).show();
            }
        });
        mDeleteCircleTask.setProgressDialog(this);
        mDeleteCircleTask.execute();
    }

    private void showCircleInfo(ComiketKigyoChecklist circle) {
        ComiketKigyo kigyo = circle.getComiketKigyo();
        float dx = (float) kigyo.getMapPosX();
        float dy = (float) kigyo.getMapPosY();

        mMapFragment.setMapPosition(dx, dy);
        mMapFragment.setCircle(circle);
        mMapFragment.showFooterView();

        closeDrawers();
    }

    private void showCircleEditFragment(ComiketKigyoChecklist circle) {
//        ComiketKigyoChecklistFormFragment fragment = new ComiketKigyoChecklistFormFragment(circle);
//        fragment.setOnComiketKigyoChecklistUpdateListener(this);
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .add(R.id.content_frame, fragment, ComiketKigyoChecklistFormFragment.TAG)
//                .commit();
    }

    private void showComiketKigyoChecklistCreateForm() {
//        ComiketKigyoChecklistFormFragment fragment = new ComiketKigyoChecklistFormFragment(mComiketId);
//        fragment.setOnComiketKigyoChecklistCreateListener(this);
//
//        getSupportFragmentManager()
//                .beginTransaction()
//                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                .add(R.id.content_frame, fragment, ComiketKigyoChecklistFormFragment.TAG)
//                .commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK) {
//            FragmentManager manager = getSupportFragmentManager();
//            Fragment fragment = manager.findFragmentByTag(ComiketKigyoChecklistFormFragment.TAG);
//            if (getDrawerLayout().isDrawerOpen(Gravity.LEFT)) {
//                getDrawerLayout().closeDrawer(Gravity.LEFT);
//                return false;
//            } else if (fragment != null) {
//                manager.beginTransaction()
//                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
//                        .remove(fragment)
//                        .commit();
//                return false;
//            }
//        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCircleListRefresh() {
        loadCircles(mComiketId);
    }

    @Override
    public void onCreateButtonClick(View v) {
        showComiketKigyoChecklistCreateForm();
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
    public void onFooterViewClick(ComiketKigyoChecklist circle) {
        showCircleEditFragment(circle);
    }

    @Override
    public void onFooterViewLongClick(final ComiketKigyoChecklist circle) {
//        ComiketKigyoChecklistMenuDialogFragment fragment = ComiketKigyoChecklistMenuDialogFragment.newInstance(circle);
//        fragment.setOnMenuDialogSelectListener(new OnMenuDialogSelectListener() {
//            @Override
//            public void onMenuSelect(int menuId) {
//                switch (menuId) {
//                    case ComiketCircleMenuDialogFragment.MENU_MAP:
//                        showCircleInfo(circle);
//                        return;
//                    case ComiketCircleMenuDialogFragment.MENU_EDIT:
//                        showCircleEditFragment(circle);
//                        return;
//                    case ComiketCircleMenuDialogFragment.MENU_DELETE:
//                        deleteCircle(circle);
//                        return;
//                }
//            }
//        });
//        fragment.show(getSupportFragmentManager(), ComiketCircleMenuDialogFragment.TAG);
    }

//    @Override
//    public void onCircleSelect(ComiketKigyoChecklist circle) {
//        showCircleInfo(circle);
//    }
//
//
//    @Override
//    public void onComiketKigyoChecklistCreate(ComiketKigyoChecklist circle) {
//        mCircleAdapter.add(circle);
//        mMapFragment.setCircle(circle);
//        mMapFragment.showFooterView();
//    }
//
//    @Override
//    public void onComiketKigyoChecklistUpdate(ComiketKigyoChecklist circle) {
//        mCircleAdapter.updateItem(circle);
//        mMapFragment.setCircle(circle);
//        mMapFragment.showFooterView();
//    }
}
