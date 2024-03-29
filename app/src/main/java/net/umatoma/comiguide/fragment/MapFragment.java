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
import net.umatoma.comiguide.view.MapImageView;

public class MapFragment extends Fragment {

    private GestureDetector mGestureDetector;
    private MapImageView mMapImageView;
    private View mFooterView;
    private OnFunctionButtonClickListener mOnFunctionButtonClickListener;

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

        FrameLayout footerContainer = (FrameLayout) view.findViewById(R.id.footer_content);
        footerContainer.addView(mFooterView);

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

        FloatingActionButton createCircleButton = (FloatingActionButton)view.findViewById(R.id.button_create_circle);
        FloatingActionButton circleListButton = (FloatingActionButton)view.findViewById(R.id.button_circle_list);
        FloatingActionButton changeMapButton = (FloatingActionButton)view.findViewById(R.id.button_change_map);

        createCircleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFunctionButtonClickListener != null) {
                    mOnFunctionButtonClickListener.onCreateButtonClick(v);
                }
            }
        });
        circleListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFunctionButtonClickListener != null) {
                    mOnFunctionButtonClickListener.onShowListButtonClick(v);
                }
            }
        });
        changeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnFunctionButtonClickListener != null) {
                    mOnFunctionButtonClickListener.onChangeMapButtonClick(v);
                }
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnFunctionButtonClickListener = null;
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
                if (view != null) {
                    view.setVisibility(View.GONE);
                }
            }
        });
        animator.start();
    }

    public void setOnFunctionButtonClickListener(OnFunctionButtonClickListener listener) {
        mOnFunctionButtonClickListener = listener;
    }

    public void setMapPosition(float dx, float dy) {
        if (mMapImageView != null) {
            mMapImageView.setCurrentPosition(dx, dy);
        }
    }

    protected void hideChangeMapButton() {
        View view = getView();
        if (view != null) {
            FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.button_change_map);
            button.setVisibility(View.GONE);
        }
    }

    protected void setMapImageView(MapImageView view) {
        mMapImageView = view;
    }

    protected View getFooterView() {
        return mFooterView;
    }

    protected int getFooterLayoutResorce() {
        return -1;
    }

    protected void onFooterViewClick(MotionEvent e) {}

    protected void onFooterViewDoubleClick(MotionEvent e) {
        hideFooterView();
    }

    protected void onFooterViewLongClick(MotionEvent e) {}
}
