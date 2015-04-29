package net.umatoma.comiguide.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.ComiketKigyoChecklistAdapter;
import net.umatoma.comiguide.model.ComiketKigyo;
import net.umatoma.comiguide.model.ComiketKigyoChecklist;
import net.umatoma.comiguide.view.ComiketKigyoMapView;

public class ComiketKigyoMapFragment extends MapFragment {

    public static final String TAG = "ComiketKigyoMapFragment";

    private int mComiketId;
    private ComiketKigyoChecklist mChecklist;
    private ComiketKigyoChecklistAdapter mChecklistAdapter;
    private OnFooterViewClickListener mOnFooterViewClickListener;
    private ComiketKigyoMapView mMapImageView;

    public static ComiketKigyoMapFragment getInstance(int comiket_id) {
        ComiketKigyoMapFragment instance = new ComiketKigyoMapFragment();
        instance.setComiketId(comiket_id);
        return instance;
    }

    public ComiketKigyoMapFragment() {

    }

    private void setComiketId(int comiket_id) {
        mComiketId = comiket_id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);

        mMapImageView = new ComiketKigyoMapView(getActivity());
        mMapImageView.setImageResource(R.drawable.ckigyo_map_c87);
        mMapImageView.setComiketKigyoCheckistAdapter(mChecklistAdapter);
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

    public void setChecklist(ComiketKigyoChecklist checklist) {
        mChecklist = checklist;
        ComiketKigyo kigyo = checklist.getComiketKigyo();

        View footerView = getFooterView();
        footerView.findViewById(R.id.color).setBackgroundColor(checklist.getColorCode());
        ((TextView) footerView.findViewById(R.id.space_info)).setText(String.valueOf(kigyo.getKigyoNo()));
        ((TextView) footerView.findViewById(R.id.circle_name)).setText(kigyo.getName());
        ((TextView) footerView.findViewById(R.id.cost)).setText(checklist.getCost());
        ((TextView) footerView.findViewById(R.id.comment)).setText(checklist.getComment());
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
