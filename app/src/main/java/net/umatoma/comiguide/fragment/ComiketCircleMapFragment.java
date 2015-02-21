package net.umatoma.comiguide.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.getbase.floatingactionbutton.FloatingActionButton;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.view.MapImageView;

public class ComiketCircleMapFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private FloatingActionButton mCreateCircleButton;
    private FloatingActionButton mCircleListButton;
    private FloatingActionButton mChangeMapButton;
    private MapImageView mMapImage;

    public ComiketCircleMapFragment() {}

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
        mMapImage = (MapImageView)view.findViewById(R.id.circle_map);
        mMapImage.setImageResource(R.drawable.ccircle_map_d1_e123);

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

    public interface OnFragmentInteractionListener {
        public void onFunctionsButtonClicke(int id);
    }

}
