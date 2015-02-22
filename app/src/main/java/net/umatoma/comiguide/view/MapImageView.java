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
import android.widget.Toast;

public class MapImageView extends ImageView {

    private static final String TAG = "MapImageView";
    private static final float MAX_SCALE_FACTOR = 5.0f;
    private static final float MIN_SCALE_FACTOR = 0.5f;
    private float mDefaultScale = 1.0f;
    private float mMaxScale = 2.0f;
    private float mMinScale = 0.5f;
    private long mScrollEndAt = 0;
    private int mImageWidth = 0;
    private int mImageHeight = 0;
    private int mImageOriginalWidth = 0;
    private int mImageOriginalHeight = 0;
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
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resId, options);
        mImageOriginalWidth = options.outWidth;
        mImageOriginalHeight = options.outHeight;

        Bitmap b = BitmapFactory.decodeResource(getResources(), resId);
        mImageWidth = b.getWidth();
        mImageHeight = b.getHeight();
        b.recycle();

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
        float img_scale = (float) mImageWidth / (float) mImageOriginalWidth;
        float px = (float) getWidth() / 2.0f;
        float py = (float) getHeight() / 2.0f;
        float scale = mDefaultScale * MAX_SCALE_FACTOR;
        Matrix matrix = getImageMatrix();
        matrix.setTranslate(-dx * img_scale, -dy * img_scale);
        matrix.preTranslate(px, py);
        matrix.postScale(scale, scale, px, py);
        setImageMatrix(matrix);
        invalidate();
    }
}
