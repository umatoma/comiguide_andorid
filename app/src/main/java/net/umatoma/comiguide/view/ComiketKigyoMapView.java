package net.umatoma.comiguide.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;

import net.umatoma.comiguide.adapter.ComiketKigyoChecklistAdapter;
import net.umatoma.comiguide.model.ComiketKigyo;
import net.umatoma.comiguide.model.ComiketKigyoChecklist;

public class ComiketKigyoMapView extends MapImageView {

    private static final int BOOTH_SIZE = 18;
    private ComiketKigyoChecklistAdapter mChecklistAdapter;

    public ComiketKigyoMapView(Context context) {
        super(context);
    }

    public ComiketKigyoMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ComiketKigyoMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mChecklistAdapter != null) {
            float[] values = new float[9];
            Matrix matrix = getImageMatrix();
            matrix.getValues(values);
            float scale = values[Matrix.MSCALE_X];
            float trans_x = values[Matrix.MTRANS_X];
            float trans_y = values[Matrix.MTRANS_Y];
            float space_size = (float) BOOTH_SIZE * mImageScale * scale;
            float space_size_half = space_size / 2.0f;

            int count = mChecklistAdapter.getCount();
            for (int i = 0; i < count; i++) {
                ComiketKigyoChecklist circle = mChecklistAdapter.getItem(i);
                ComiketKigyo kigyo = circle.getComiketKigyo();
                Paint paint = new Paint();
                paint.setColor(circle.getColorCode());
                paint.setAlpha(150);


                float left = (float) kigyo.getMapPosX() * mImageScale * scale + trans_x;
                float top = (float) kigyo.getMapPosY() * mImageScale * scale + trans_y;
                float right = left + space_size_half;
                float bottom = top + space_size;
                canvas.drawRect(left, top, right, bottom, paint);
            }
        }
    }

    public void setComiketKigyoCheckistAdapter(ComiketKigyoChecklistAdapter adapter) {
        mChecklistAdapter = adapter;
    }
}
