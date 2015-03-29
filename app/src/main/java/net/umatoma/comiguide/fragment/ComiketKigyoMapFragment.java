package net.umatoma.comiguide.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.ComiketKigyoChecklistAdapter;
import net.umatoma.comiguide.model.ComiketKigyoChecklist;
import net.umatoma.comiguide.view.MapImageView;

public class ComiketKigyoMapFragment extends MapFragment {

    public static final String TAG = "ComiketKigyoMapFragment";

    private int mComiketId;
    private ComiketKigyoChecklist mChecklist;
    private ComiketKigyoChecklistAdapter mChecklistAdapter;
    private OnFooterViewClickListener mOnFooterViewClickListener;
    private MapImageView mMapImageView;

    public static ComiketKigyoMapFragment getInstance(int comiket_id) {
        return new ComiketKigyoMapFragment(comiket_id);
    }

    public ComiketKigyoMapFragment() {

    }

    private ComiketKigyoMapFragment(int comiket_id) {
        mComiketId = comiket_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

//        mMapImageView = new ComiketKigyoChecklistMapView(getActivity());
//        mMapImageView.setImageResource(R.drawable.c1circle_map_8);
//        mMapImageView.setComiketKigyoChecklistAdapter(mChecklistAdapter);
//        setMapImageView(mMapImageView);

        FrameLayout mapImageContainerView = (FrameLayout) view.findViewById(R.id.circle_map_container);
        mapImageContainerView.addView(mMapImageView);

        return view;
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
            mOnFooterViewClickListener.onFooterViewClick(mChecklist);
        }
    }

    @Override
    protected void onFooterViewLongClick(MotionEvent e) {
        if (mOnFooterViewClickListener != null) {
            mOnFooterViewClickListener.onFooterViewLongClick(mChecklist);
        }
    }

    public void setOnFooterViewClickListener(OnFooterViewClickListener listener) {
        mOnFooterViewClickListener = listener;
    }

    public void setCircleAdapter(ComiketKigyoChecklistAdapter adapter) {
        mChecklistAdapter = adapter;
    }

    public void setCircle(ComiketKigyoChecklist circle) {
        mChecklist = circle;

        View footerView = getFooterView();
        footerView.findViewById(R.id.color).setBackgroundColor(circle.getColorCode());
        ((TextView) footerView.findViewById(R.id.space_info)).setText(circle.getSpaceInfo());
        ((TextView) footerView.findViewById(R.id.cost)).setText(circle.getCost());
        ((TextView) footerView.findViewById(R.id.comment)).setText(circle.getComment());
    }

    public void hideFooterView(ComiketKigyoChecklist circle) {
        if (mChecklist.getId() == circle.getId()) {
            hideFooterView();
        }
    }

    public interface OnFooterViewClickListener {
        public void onFooterViewClick(ComiketKigyoChecklist circle);
        public void onFooterViewLongClick(ComiketKigyoChecklist circle);
    }
}
