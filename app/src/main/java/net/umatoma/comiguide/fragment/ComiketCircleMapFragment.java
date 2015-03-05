package net.umatoma.comiguide.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.adapter.ComiketCircleArrayAdapter;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.view.ComiketCircleMapView;
import net.umatoma.comiguide.view.MapImageView;

public class ComiketCircleMapFragment extends Fragment {

    public static final String TAG = "ComiketCircleMapFragment";

    private int mComiketId;
    private int mCmapId;
    private int mDay;
    private OnFragmentInteractionListener mListener;
    private FloatingActionButton mCreateCircleButton;
    private FloatingActionButton mCircleListButton;
    private FloatingActionButton mChangeMapButton;
    private ComiketCircleMapView mMapImage;
    private ComiketCircle mComiketCircle;
    private ComiketCircleArrayAdapter mAdapter;

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
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comiket_circle_map, container, false);
        View footerView = view.findViewById(R.id.footer_content_inner);
        footerView.setVisibility(View.GONE);
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFooterViewClick(mComiketCircle);
            }
        });

        mMapImage = (ComiketCircleMapView)view.findViewById(R.id.circle_map);
        mMapImage.setImageResource(getMapImageResourceId(mDay, mCmapId));
        mMapImage.setComiketCircleArrayAdapter(mAdapter);

        mCreateCircleButton = (FloatingActionButton)view.findViewById(R.id.button_create_circle);
        mCircleListButton   = (FloatingActionButton)view.findViewById(R.id.button_circle_list);
        mChangeMapButton   = (FloatingActionButton)view.findViewById(R.id.button_change_map);

        mCreateCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFunctionsButtonClicke(R.id.button_create_circle);
            }
        });
        mCircleListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFunctionsButtonClicke(R.id.button_circle_list);
            }
        });
        mChangeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFunctionsButtonClicke(R.id.button_change_map);
            }
        });
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

        View view = getView().findViewById(R.id.footer_content_inner);
        view.findViewById(R.id.color).setBackgroundColor(circle.getColorCode());
        ((TextView) view.findViewById(R.id.space_info)).setText(circle.getSpaceInfo());
        ((TextView) view.findViewById(R.id.circle_name)).setText(circle.getCircleName());
        ((TextView) view.findViewById(R.id.cost)).setText(circle.getCost());
        ((TextView) view.findViewById(R.id.comment)).setText(circle.getComment());
    }

    public void setComiketCircleArrayAdapter(ComiketCircleArrayAdapter adapter) {
        mAdapter = adapter;
    }

    public void showFooterView() {
        getView().findViewById(R.id.footer_content_inner).setVisibility(View.VISIBLE);
    }

    public interface OnFragmentInteractionListener {
        public void onFunctionsButtonClicke(int id);
        public void onFooterViewClick(ComiketCircle circle);
    }

}
