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
            float space_size_half = space_size / 2.0f;
            Paint paint = new Paint();
            paint.setAlpha(128);

            int count = mAdapter.getCount();
            for (int i = 0; i < count; i++) {
                ComiketCircle circle = mAdapter.getItem(i);
                ComiketLayout layout = circle.getComiketLayout();


                float right, bottom;
                float left = (float) layout.getPosX() * mImageScale * scale + trans_x;
                float top = (float) layout.getPosY() * mImageScale * scale + trans_y;
                int layout_type = layout.getLayout();
                switch (layout_type) {
                    case 1:
                        if (circle.getSpaceNoSub().equals("b")) {
                            left += space_size_half;
                        }
                        right = left + space_size_half;
                        bottom = top + space_size;
                        break;
                    case 2:
                        if (circle.getSpaceNoSub().equals("a")) {
                            top += space_size_half;
                        }
                        right = left + space_size;
                        bottom = top + space_size_half;
                        break;
                    case 3:
                        if (circle.getSpaceNoSub().equals("a")) {
                            left += space_size_half;
                        }
                        right = left + space_size_half;
                        bottom = top + space_size;
                        break;
                    case 4:
                        if (circle.getSpaceNoSub().equals("b")) {
                            top += space_size_half;
                        }
                        right = left + space_size;
                        bottom = top + space_size_half;
                        break;
                    default:
                        continue;
                }

                paint.setColor(circle.getColorCode());
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }

    public void setComiketCircleArrayAdapter(ComiketCircleArrayAdapter adapter) {
        mAdapter = adapter;
    }
}
