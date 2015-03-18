package net.umatoma.comiguide.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import net.umatoma.comiguide.ComiGuide;
import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.Comic1CircleAdapter;
import net.umatoma.comiguide.fragment.Comic1CircleMapFragment;
import net.umatoma.comiguide.model.Comic1Circle;

public class Comic1CircleActivity extends MapActivity
        implements Comic1CircleMapFragment.OnFooterViewClickListener {

    private int mComic1Id;
    private Comic1CircleMapFragment mMapFragment;
    private Comic1CircleAdapter mCircleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCircleAdapter = new Comic1CircleAdapter(this);

        initialize(ComiGuide.COMIC1_ID);
    }

    private void initialize(int comic1_id) {
        mComic1Id = comic1_id;
        getSupportActionBar().setTitle(String.format("COMIC1☆%d", mComic1Id));

        mMapFragment = Comic1CircleMapFragment.getInstance(mComic1Id);
        mMapFragment.setCircleAdapter(mCircleAdapter);
        mMapFragment.setOnFooterViewClickListener(this);
        setMapFragment(mMapFragment);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.content_frame, mMapFragment, Comic1CircleMapFragment.TAG);
        transaction.commit();
    }

    @Override
    public void onFooterViewClick(Comic1Circle circle) {

    }

    @Override
    public void onFooterViewLongClick(Comic1Circle circle) {

    }
}
