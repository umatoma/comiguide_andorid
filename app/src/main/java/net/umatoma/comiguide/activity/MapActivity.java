package net.umatoma.comiguide.activity;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.fragment.CircleListFragment;
import net.umatoma.comiguide.fragment.MapFragment;
import net.umatoma.comiguide.fragment.OnFunctionButtonClickListener;

public class MapActivity extends ActionBarActivity implements OnFunctionButtonClickListener {

    private MapFragment mMapFragment;
    private CircleListFragment mCircleListFragment;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
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

    protected DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    protected void setCircleListFragment(CircleListFragment fragment) {
        mCircleListFragment = fragment;
        mCircleListFragment.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onCircleListRefresh();
            }
        });
    }

    protected void setMapFragment(MapFragment fragment) {
        mMapFragment = fragment;
        mMapFragment.setOnFunctionButtonClickListener(this);
    }

    protected void closeDrawers() {
        mDrawerLayout.closeDrawers();
    }

    protected void setRefreshing(boolean refreshing) {
        if (mCircleListFragment != null) {
            mCircleListFragment.setRefreshing(refreshing);
        }
    }

    @Override
    public void onCreateButtonClick(View v) {

    }

    @Override
    public void onShowListButtonClick(View v) {

    }

    @Override
    public void onChangeMapButtonClick(View v) {

    }

    protected void onCircleListRefresh() {

    }
}
