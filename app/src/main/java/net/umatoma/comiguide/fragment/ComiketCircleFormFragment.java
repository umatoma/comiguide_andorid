package net.umatoma.comiguide.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import net.umatoma.comiguide.R;
import net.umatoma.comiguide.model.ComiketCircle;

public class ComiketCircleFormFragment extends Fragment {

    public static final String TAG = "ComiketCircleFormFragment";
    private ComiketCircle mComiketCircle;

    public ComiketCircleFormFragment() {
        // Required empty public constructor
    }

    public ComiketCircleFormFragment(ComiketCircle circle) {
        mComiketCircle = circle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comiket_circle_form, container, false);
        view.setOnTouchListener(new OnCnacelListener()); // Not through touch event.
        return view;
    }

    private class OnCnacelListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    }


}
