package net.umatoma.comiguide.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.fragment.ComiketCircleListFragment;
import net.umatoma.comiguide.fragment.ComiketCircleMapFragment;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.model.ComiketLayout;
import net.umatoma.comiguide.view.MapImageView;

public class ComiketCircleActivity extends ActionBarActivity
        implements ComiketCircleMapFragment.OnFragmentInteractionListener,
            ComiketCircleListFragment.OnFragmentInteractionListener {

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comiket_circle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, new ComiketCircleMapFragment());
        transaction.replace(R.id.left_drawer, new ComiketCircleListFragment());
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
    public void onFunctionsButtonClicke(int id) {
        switch (id) {
            case R.id.button_circle_list:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                return;
        }
    }

    @Override
    public void onComiketCircleSelected(ComiketCircle circle) {
        MapImageView mapImageView = (MapImageView) findViewById(R.id.circle_map);
        if (mapImageView != null) {
            ComiketLayout layout = circle.getComiketLayout();
            float dx = (float) layout.getPosX();
            float dy = (float) layout.getPosY();
            mapImageView.setCurrentPosition(dx, dy);
            mDrawerLayout.closeDrawers();
        }
    }
}
