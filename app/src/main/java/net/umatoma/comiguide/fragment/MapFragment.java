package net.umatoma.comiguide.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;

import net.umatoma.comiguide.R;

public class MapFragment extends Fragment {

    private FloatingActionButton mCreateCircleButton;
    private FloatingActionButton mCircleListButton;
    private FloatingActionButton mChangeMapButton;
    private GestureDetector mGestureDetector;
    private FrameLayout mFooterContainer;
    private View mFooterView;

    public MapFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mFooterView = inflater.inflate(getFooterLayoutResorce(), null);
        mFooterView.setVisibility(View.GONE);
        mFooterView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

        mFooterContainer = (FrameLayout) view.findViewById(R.id.footer_content);
        mFooterContainer.addView(mFooterView);

        mGestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapConfirmed (MotionEvent e) {
                onFooterViewClick(e);
                return true;
            }

            @Override
            public boolean onDoubleTap (MotionEvent e) {
                onFooterViewDoubleClick(e);
                return true;
            }

            @Override
            public void onLongPress (MotionEvent e) {
                onFooterViewLongClick(e);
            }
        });

        mCreateCircleButton = (FloatingActionButton)view.findViewById(R.id.button_create_circle);
        mCircleListButton   = (FloatingActionButton)view.findViewById(R.id.button_circle_list);
        mChangeMapButton   = (FloatingActionButton)view.findViewById(R.id.button_change_map);

        mCreateCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreateButtonClick(v);
            }
        });
        mCircleListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowListButtonClick(v);
            }
        });
        mChangeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeMapButtonClick(v);
            }
        });

        return view;
    }

    public void showFooterView() {
        PropertyValuesHolder holderAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mFooterView, holderAlpha);
        animator.setDuration(300);
        animator.start();
        mFooterView.setVisibility(View.VISIBLE);
    }

    public void hideFooterView() {
        PropertyValuesHolder holderAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(mFooterView, holderAlpha);
        animator.setDuration(300);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                View view = (View) ((ObjectAnimator) animation).getTarget();
                view.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    protected View getFooterView() {
        return mFooterView;
    }

    protected int getFooterLayoutResorce() {
        return -1;
    }

    protected void onCreateButtonClick(View v) {}

    protected void onShowListButtonClick(View v) {}

    protected void onChangeMapButtonClick(View v) {}

    protected void onFooterViewClick(MotionEvent e) {}

    protected void onFooterViewDoubleClick(MotionEvent e) {
        hideFooterView();
    }

    protected void onFooterViewLongClick(MotionEvent e) {}
}
