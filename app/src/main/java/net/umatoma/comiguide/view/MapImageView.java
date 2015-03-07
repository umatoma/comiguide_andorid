package net.umatoma.comiguide.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Scroller;

public class MapImageView extends ImageView {

    protected static final String TAG = "MapImageView";
    protected static final float MAX_SCALE_FACTOR = 8.0f;
    protected static final float MIN_SCALE_FACTOR = 0.8f;
    protected float mDefaultScale = 1.0f;
    protected float mMaxScale = 2.0f;
    protected float mMinScale = 0.5f;
    protected long mScrollEndAt = 0;
    protected int mImageWidth = 0;
    protected int mImageHeight = 0;
    protected float mImageScale = 1.0f;
    protected int mImageOriginalWidth = 0;
    protected int mImageOriginalHeight = 0;
    protected float mViewWidthHalf = 1.0f;
    protected float mViewHeightHalf = 1.0f;
    protected Scroller mScroller;
    protected ValueAnimator mAnimator;
    protected GestureDetector mGestureDetector;
    protected GestureDetector.SimpleOnGestureListener mGestureListener;
    protected ScaleGestureDetector mScaleGestureDetector;
    protected ScaleGestureDetector.SimpleOnScaleGestureListener mScaleGestureListener;

    public MapImageView(Context context) {
        this(context, null);
    }

    public MapImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public MapImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context);
    }

    private void initialize(Context context) {
        setScaleType(ScaleType.MATRIX);
        mGestureListener = new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap (MotionEvent e) {
                Log.d(TAG, String.format("onDoubleTap, x : %f, y : %f", e.getX(), e.getY()));
                zoomCurrentPosition(e.getX(), e.getY());
                return true;
            }

            @Override
            public boolean onScroll (MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.d(TAG, "onScroll");
                postImageTranslate(-distanceX, -distanceY);
                invalidate();
                return true;
            }
        };
        mScaleGestureListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                Log.d(TAG, "onScaleBegin : "+ detector.getScaleFactor());
                return super.onScaleBegin(detector);
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                Log.d(TAG, "onScaleEnd : "+ detector.getScaleFactor());
                postImageScale(detector.getScaleFactor());
                invalidate();
                mScrollEndAt = System.currentTimeMillis();
                super.onScaleEnd(detector);
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.d(TAG, "onScale : " + detector.getScaleFactor());
                postImageScale(detector.getScaleFactor());
                invalidate();
                return true;
            };
        };
        mGestureDetector = new GestureDetector(context, mGestureListener);
        mScaleGestureDetector = new ScaleGestureDetector(context, mScaleGestureListener);

        mScroller = new Scroller(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onTouchEvent");

        if (mAnimator != null && mAnimator.isRunning()) {
            return true;
        }

        if (ev.getPointerCount() > 1) {
            mScaleGestureDetector.onTouchEvent(ev);
            return true;
        } else if (System.currentTimeMillis() - mScrollEndAt > 100) {
            mGestureDetector.onTouchEvent(ev);
            return true;
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void setImageResource (int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap b1 = BitmapFactory.decodeResource(getResources(), resId, options);
        mImageOriginalWidth = options.outWidth;
        mImageOriginalHeight = options.outHeight;
        if (b1 != null) {
            b1.recycle();
        }

        Bitmap b2 = BitmapFactory.decodeResource(getResources(), resId);
        if (b2 != null) {
            mImageWidth = b2.getWidth();
            mImageHeight = b2.getHeight();
            mImageScale = (float) mImageWidth / (float) mImageOriginalWidth;
            b2.recycle();
        }

        super.setImageResource(resId);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mImageWidth > 0 && mImageHeight > 0) {
            float scale_x = (float)w / mImageWidth;
            float scale_y = (float)h / mImageHeight;

            Matrix matrix = getImageMatrix();
            if (scale_x > scale_y) {
                float distX = ((float)w - (float)mImageWidth * scale_y) / 2.0f;
                matrix.preScale(scale_y, scale_y);
                matrix.postTranslate(distX, 0);
                setScaleParams(scale_y);
            } else {
                float distY = ((float)h - (float)mImageHeight * scale_x) / 2.0f;
                matrix.preScale(scale_x, scale_x);
                matrix.postTranslate(0, distY);
                setScaleParams(scale_x);
            }
            setImageMatrix(matrix);
            invalidate();
        }

        mViewWidthHalf = (float) getWidth() / 2.0f;
        mViewHeightHalf = (float) getHeight() / 2.0f;
    }

    private void setScaleParams(float scale) {
        mDefaultScale = scale;
        mMaxScale = scale * MAX_SCALE_FACTOR;
        mMinScale = scale * MIN_SCALE_FACTOR;
    }

    private void postImageTranslate(float distanceX, float distanceY) {
        Matrix matrix = getImageMatrix();
        matrix.postTranslate(distanceX, distanceY);
        setImageMatrix(matrix);
    }

    private void postImageScale(float scale) {
        postImageScale(scale, scale);
    }

    private void postImageScale(float scale_x, float scale_y) {
        float px = (float)getWidth() / 2.0f;
        float py = (float)getHeight() / 2.0f;
        postImageScale(scale_x, scale_y, px, py);
    }

    private void postImageScale(float scale_x, float scale_y, float px, float py) {
        float[] values = new float[9];
        Matrix matrix = getImageMatrix();
        matrix.getValues(values);

        float target_scale_x = values[Matrix.MSCALE_X] * scale_x;
        float target_scale_y = values[Matrix.MSCALE_Y] * scale_y;
        if (isValidScale(target_scale_x) && isValidScale(target_scale_y)) {
            matrix.postScale(scale_x, scale_y, px, py);
            setImageMatrix(matrix);
        }
    }

    private boolean isValidScale(float scale) {
        Log.v(TAG, String.format("scale : %f", scale));
        return (mMinScale < scale) && (scale < mMaxScale);
    }

    public void setCurrentPosition(float dx, float dy) {
        float scale = mDefaultScale * MAX_SCALE_FACTOR;
        Matrix matrix = getImageMatrix();
        matrix.setTranslate(-dx * mImageScale, -dy * mImageScale);
        matrix.preTranslate(mViewWidthHalf, mViewHeightHalf);
        matrix.postScale(scale, scale, mViewWidthHalf, mViewHeightHalf);
        setImageMatrix(matrix);
        invalidate();
    }

    public void zoomCurrentPosition(final float px, final float py) {
        final float[] values = new float[9];
        final Matrix matrix = getImageMatrix();
        matrix.getValues(values);
        float target_scale = mDefaultScale * MAX_SCALE_FACTOR;
        float current_scale = values[Matrix.MSCALE_X];
        mAnimator = ValueAnimator.ofFloat(current_scale, target_scale);
        mAnimator.setDuration(500);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float[] values = new float[9];
                Matrix matrix = getImageMatrix();
                matrix.getValues(values);
                float scale = (Float) animation.getAnimatedValue() / values[Matrix.MSCALE_X];
                postImageScale(scale, scale, px, py);
                invalidate();
            }
        });
        mAnimator.start();
    }
}
