package net.umatoma.comiguide.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import net.umatoma.comiguide.ComiGuide;
import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.Comic1CircleAdapter;
import net.umatoma.comiguide.api.ComiGuideApiClient;
import net.umatoma.comiguide.fragment.Comic1CircleListFragment;
import net.umatoma.comiguide.fragment.Comic1CircleMapFragment;
import net.umatoma.comiguide.fragment.OnComic1CircleSelectListener;
import net.umatoma.comiguide.model.Comic1Circle;
import net.umatoma.comiguide.model.Comic1Layout;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.model.ComiketLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Comic1CircleActivity extends MapActivity
        implements Comic1CircleMapFragment.OnFooterViewClickListener, OnComic1CircleSelectListener {

    private int mComic1Id;
    private Comic1CircleMapFragment mMapFragment;
    private Comic1CircleListFragment mCircleListFragment;
    private Comic1CircleAdapter mCircleAdapter;
    private ComiGuideApiClient.HttpClientTask mLoadCirclesTask;

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

        mMapFragment = Comic1CircleMapFragment.getInstance(mComic1Id);
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

    @Override
    protected void onCircleListRefresh() {
        loadCircles(mComic1Id);
    }

    @Override
    public void onCreateButtonClick(View v) {
        // showComiketCircleCreateForm();
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

    }

    @Override
    public void onFooterViewLongClick(Comic1Circle circle) {

    }

    @Override
    public void onCircleSelect(Comic1Circle circle) {
        showCircleInfo(circle);
    }

    private void showCircleInfo(Comic1Circle circle) {
        Comic1Layout layout = circle.getComic1Layout();
        float dx = (float) layout.getPosX();
        float dy = (float) layout.getPosY();

        mMapFragment.setMapPosition(dx, dy);
        mMapFragment.setCircle(circle);
        mMapFragment.showFooterView();

        closeDrawers();
    }
}
