package net.umatoma.comiguide.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.model.ComiketCircle;

public class ComiketCircleMapFooterFragment extends Fragment implements View.OnClickListener {

    private ComiketCircle mComiketCircle;
    private OnFragmentInteractionListener mListener;

    public ComiketCircleMapFooterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_comiket_circle_map_footer, container, false);
        return view;
    }

    public void setComiketCircle(ComiketCircle circle) {
        mComiketCircle = circle;

        View view = getView();
        view.findViewById(R.id.color).setBackgroundColor(circle.getColor());
        ((TextView) view.findViewById(R.id.space_info)).setText(circle.getSpaceInfo());
        ((TextView) view.findViewById(R.id.circle_name)).setText(circle.getCircleName());
        ((TextView) view.findViewById(R.id.cost)).setText(circle.getCost());
        ((TextView) view.findViewById(R.id.comment)).setText(circle.getComment());
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
    public void onResume() {
        super.onResume();

        View view = getView();
        view.setVisibility(View.GONE);
        view.setClickable(true);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mListener.onFooterViewClick(mComiketCircle);
    }

    public void hideView() {
        getView().setVisibility(View.GONE);
    }

    public void showView() {
        getView().setVisibility(View.VISIBLE);
    }

    public interface OnFragmentInteractionListener {
        public void onFooterViewClick(ComiketCircle circle);
    }
}
