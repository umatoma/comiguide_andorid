package net.umatoma.comiguide.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.Comic1CircleAdapter;
import net.umatoma.comiguide.model.Comic1Circle;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.view.Comic1CircleMapView;
import net.umatoma.comiguide.view.ComiketCircleMapView;
import net.umatoma.comiguide.view.MapImageView;

public class Comic1CircleMapFragment extends MapFragment {

    public static final String TAG = "Comic1CircleMapFragment";

    private int mComic1Id;
    private Comic1Circle mCircle;
    private Comic1CircleAdapter mCircleAdapter;
    private OnFooterViewClickListener mOnFooterViewClickListener;
    private Comic1CircleMapView mMapImageView;

    public static Comic1CircleMapFragment getInstance(int comic1_id) {
        return new Comic1CircleMapFragment(comic1_id);
    }

    public Comic1CircleMapFragment() {

    }

    private Comic1CircleMapFragment(int comic1_id) {
        mComic1Id = comic1_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        mMapImageView = new Comic1CircleMapView(getActivity());
        mMapImageView.setImageResource(R.drawable.c1circle_map_8);
        mMapImageView.setComic1CircleAdapter(mCircleAdapter);
        setMapImageView(mMapImageView);

        FrameLayout mapImageContainerView = (FrameLayout) view.findViewById(R.id.circle_map_container);
        mapImageContainerView.addView(mMapImageView);

        return view;
    }

    @Override
    public void onStart () {
        super.onStart();
        hideChangeMapButton();
    }

    @Override
    public void onDetach() {
        mOnFooterViewClickListener = null;

        super.onDetach();
    }

    @Override
    protected int getFooterLayoutResorce() {
        return R.layout.comic1_circle_map_footer;
    }

    @Override
    protected void onFooterViewClick(MotionEvent e) {
        if (mOnFooterViewClickListener != null) {
            mOnFooterViewClickListener.onFooterViewClick(mCircle);
        }
    }

    @Override
    protected void onFooterViewLongClick(MotionEvent e) {
        if (mOnFooterViewClickListener != null) {
            mOnFooterViewClickListener.onFooterViewLongClick(mCircle);
        }
    }

    public void setOnFooterViewClickListener(OnFooterViewClickListener listener) {
        mOnFooterViewClickListener = listener;
    }

    public void setCircleAdapter(Comic1CircleAdapter adapter) {
        mCircleAdapter = adapter;
    }

    public void setCircle(Comic1Circle circle) {
        mCircle = circle;

        View footerView = getFooterView();
        footerView.findViewById(R.id.color).setBackgroundColor(circle.getColorCode());
        ((TextView) footerView.findViewById(R.id.space_info)).setText(circle.getSpaceInfo());
        ((TextView) footerView.findViewById(R.id.circle_name)).setText(circle.getCircleName());
        ((TextView) footerView.findViewById(R.id.cost)).setText(circle.getCost());
        ((TextView) footerView.findViewById(R.id.comment)).setText(circle.getComment());
    }

    public void hideFooterView(Comic1Circle circle) {
        if (mCircle.getId() == circle.getId()) {
            hideFooterView();
        }
    }

    public interface OnFooterViewClickListener {
        public void onFooterViewClick(Comic1Circle circle);
        public void onFooterViewLongClick(Comic1Circle circle);
    }
}
