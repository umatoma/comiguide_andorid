package net.umatoma.comiguide.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import net.umatoma.comiguide.adapter.Comic1CircleAdapter;
import net.umatoma.comiguide.model.Comic1Circle;
import net.umatoma.comiguide.model.Comic1Layout;

public class Comic1CircleMapView extends MapImageView {

    private static final int CIRCLE_CPACE_SIZE = 11;
    private Comic1CircleAdapter mCircleAdapter;

    public Comic1CircleMapView(Context context) {
        super(context);
    }

    public Comic1CircleMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Comic1CircleMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mCircleAdapter != null) {
            float[] values = new float[9];
            Matrix matrix = getImageMatrix();
            matrix.getValues(values);
            float scale = values[Matrix.MSCALE_X];
            float trans_x = values[Matrix.MTRANS_X];
            float trans_y = values[Matrix.MTRANS_Y];
            float space_size = (float) CIRCLE_CPACE_SIZE * mImageScale * scale;
            float space_size_half = space_size / 2.0f;

            int count = mCircleAdapter.getCount();
            for (int i = 0; i < count; i++) {
                Comic1Circle circle = mCircleAdapter.getItem(i);
                Comic1Layout layout = circle.getComic1Layout();
                Paint paint = new Paint();
                paint.setColor(circle.getColorCode());
                paint.setAlpha(150);


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
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }

    public void setComic1CircleAdapter(Comic1CircleAdapter adapter) {
        mCircleAdapter = adapter;
    }
}
