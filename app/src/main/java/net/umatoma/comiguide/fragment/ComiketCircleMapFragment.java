package net.umatoma.comiguide.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.ComiketCircleArrayAdapter;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.view.ComiketCircleMapView;

public class ComiketCircleMapFragment extends MapFragment {

    public static final String TAG = "ComiketCircleMapFragment";

    private int mComiketId;
    private int mCmapId;
    private int mDay;
    private OnFooterViewClickListener mListener;
    private ComiketCircle mComiketCircle;
    private ComiketCircleArrayAdapter mAdapter;
    private ComiketCircleMapView mMapView;

    public static ComiketCircleMapFragment newInstance(int comiket_id, int cmap_id, int day) {
        return new ComiketCircleMapFragment(comiket_id, cmap_id, day);
    }

    public ComiketCircleMapFragment() {}

    private ComiketCircleMapFragment(int comiket_id, int cmap_id, int day) {
        mComiketId = comiket_id;
        mCmapId = cmap_id;
        mDay = day;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFooterViewClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        mMapView = new ComiketCircleMapView(getActivity());
        mMapView.setImageResource(getMapImageResourceId(mDay, mCmapId));
        mMapView.setComiketCircleArrayAdapter(mAdapter);
        setMapImageView(mMapView);

        FrameLayout mapImageContainerView = (FrameLayout) view.findViewById(R.id.circle_map_container);
        mapImageContainerView.addView(mMapView);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private int getMapImageResourceId(int day, int cmap_id) {
        int id = day * 10 + cmap_id;
        switch (id) {
            case 11:
                return R.drawable.ccircle_map_d1_e123;
            case 12:
                return R.drawable.ccircle_map_d1_e456;
            case 13:
                return R.drawable.ccircle_map_d1_w12;
            case 21:
                return R.drawable.ccircle_map_d2_e123;
            case 22:
                return R.drawable.ccircle_map_d2_e456;
            case 23:
                return R.drawable.ccircle_map_d2_w12;
            case 31:
                return R.drawable.ccircle_map_d3_e123;
            case 32:
                return R.drawable.ccircle_map_d3_e456;
            case 33:
                return R.drawable.ccircle_map_d3_w12;
        }
        return -1;
    }

    public void setComiketCircle(ComiketCircle circle) {
        mComiketCircle = circle;

        View footerView = getFooterView();
        footerView.findViewById(R.id.color).setBackgroundColor(circle.getColorCode());
        ((TextView) footerView.findViewById(R.id.space_info)).setText(circle.getSpaceInfo());
        ((TextView) footerView.findViewById(R.id.circle_name)).setText(circle.getCircleName());
        ((TextView) footerView.findViewById(R.id.cost)).setText(circle.getCost());
        ((TextView) footerView.findViewById(R.id.comment)).setText(circle.getComment());
    }

    public void setComiketCircleArrayAdapter(ComiketCircleArrayAdapter adapter) {
        mAdapter = adapter;
    }

    public void hideFooterView(ComiketCircle circle) {
        if (mComiketCircle.getId() == circle.getId()) {
            hideFooterView();
        }
    }

    @Override
    protected int getFooterLayoutResorce() {
        return R.layout.comiket_circle_map_footer;
    }

    @Override
    protected void onFooterViewClick(MotionEvent e) {
        mListener.onFooterViewClick(mComiketCircle);
    }

    @Override
    protected void onFooterViewLongClick(MotionEvent e) {
        mListener.onFooterViewLongClick(mComiketCircle);
    }

    public interface OnFooterViewClickListener {
        public void onFooterViewClick(ComiketCircle circle);
        public void onFooterViewLongClick(ComiketCircle circle);
    }

}
