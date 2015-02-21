package net.umatoma.comiguide.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class MapImageView extends ImageView {

    private static final String TAG = "MapImageView";
    private long mScrollEndAt = 0;
    private int mImageWidth = 0;
    private int mImageHeight = 0;
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
                invalidate();
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
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d(TAG, "onTouchEvent");
        if (ev.getPointerCount() > 1) {
            mScaleGestureDetector.onTouchEvent(ev);
        } else if (System.currentTimeMillis() - mScrollEndAt > 100) {
            mGestureDetector.onTouchEvent(ev);
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw");
        super.onDraw(canvas);

        float cx = (float)getWidth() / 2.0f;
        float cy = (float)getHeight() / 2.0f;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawCircle(cx, cy, 10.0f, paint);
    }

    @Override
    public void setImageResource (int resId) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), resId);
        mImageWidth = b.getWidth();
        mImageHeight = b.getHeight();

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
            } else {
                float distY = ((float)h - (float)mImageHeight * scale_x) / 2.0f;
                matrix.preScale(scale_x, scale_x);
                matrix.postTranslate(0, distY);
            }
            setImageMatrix(matrix);
            invalidate();
        }
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

        Matrix matrix = getImageMatrix();
        matrix.postScale(scale_x, scale_y, px, py);
        setImageMatrix(matrix);
    }
}
