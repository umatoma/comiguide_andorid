package net.umatoma.comiguide.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class MapImageView extends ImageView {

    private static final String TAG = "MapImageView";
    private float mScaleFactor = 1.0f;
    private GestureDetector mGestureDetector;
    private GestureDetector.SimpleOnGestureListener mGestureListener;
    private ScaleGestureDetector mScaleGestureDetector;
    private ScaleGestureDetector.SimpleOnScaleGestureListener mScaleGestureListener;

    public MapImageView(Context context) {
        super(context);
        initialize(context);
    }

    public MapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        setScaleType(ScaleType.MATRIX);
        mGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap (MotionEvent e) {
                Log.d(TAG, "onDoubleTap");
                return true;
            }

            @Override
            public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(TAG, "onScroll");
                return true;
            }
        };
        mScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                Log.d(TAG, "onScaleBegin : "+ detector.getScaleFactor());
                invalidate();
                return super.onScaleBegin(detector);
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                Log.d(TAG, "onScaleEnd : "+ detector.getScaleFactor());
                mScaleFactor *= detector.getScaleFactor();
                invalidate();
                super.onScaleEnd(detector);
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.d(TAG, "onScale : "+ detector.getScaleFactor());
                mScaleFactor *= detector.getScaleFactor();
                invalidate();
                return true;
            };
        };
        mGestureDetector = new GestureDetector(context, mGestureListener);
        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onTouchEvent");
        if (ev.getPointerCount() > 1) {
            mScaleGestureDetector.onTouchEvent(ev);
        } else {
            mGestureDetector.onTouchEvent(ev);
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");

        Matrix matrix = getImageMatrix();
        matrix.setScale(mScaleFactor, mScaleFactor);
        setImageMatrix(matrix);

        super.onDraw(canvas);
    }
}
