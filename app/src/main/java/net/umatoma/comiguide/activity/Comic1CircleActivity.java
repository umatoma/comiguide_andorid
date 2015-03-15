package net.umatoma.comiguide.activity;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import net.umatoma.comiguide.ComiGuide;
import net.umatoma.comiguide.R;

public class Comic1CircleActivity extends ActionBarActivity {

    private int mComic1Id;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic1_circle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        initialize(ComiGuide.COMIC1_ID);
    }

    private void initialize(int comic1_id) {
        mComic1Id = comic1_id;
        getSupportActionBar().setTitle(String.format("COMIC1☆%d", mComic1Id));
    }
}
