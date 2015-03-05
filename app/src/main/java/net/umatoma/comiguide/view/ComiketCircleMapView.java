package net.umatoma.comiguide.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import net.umatoma.comiguide.adapter.ComiketCircleArrayAdapter;
import net.umatoma.comiguide.model.ComiketCircle;
import net.umatoma.comiguide.model.ComiketLayout;

public class ComiketCircleMapView extends MapImageView {

    private static final int CIRCLE_CPACE_SIZE = 11;
    private ComiketCircleArrayAdapter mAdapter;

    public ComiketCircleMapView(Context context) {
        super(context);
    }

    public ComiketCircleMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ComiketCircleMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mAdapter != null) {
            float[] values = new float[9];
            Matrix matrix = getImageMatrix();
            matrix.getValues(values);
            float scale = values[Matrix.MSCALE_X];
            float trans_x = values[Matrix.MTRANS_X];
            float trans_y = values[Matrix.MTRANS_Y];
            float space_size = (float) CIRCLE_CPACE_SIZE * mImageScale * scale;
            Paint paint = new Paint();

            int count = mAdapter.getCount();
            for (int i = 0; i < count; i++) {
                ComiketCircle circle = mAdapter.getItem(i);
                ComiketLayout layout = circle.getComiketLayout();
                float left = (float) layout.getPosX() * mImageScale * scale + trans_x;
                float top = (float) layout.getPosY() * mImageScale * scale + trans_y;
                float right = left + space_size;
                float bottom = top + space_size;

                paint.setColor(circle.getColorCode());
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }

    public void setComiketCircleArrayAdapter(ComiketCircleArrayAdapter adapter) {
        mAdapter = adapter;
    }
}
