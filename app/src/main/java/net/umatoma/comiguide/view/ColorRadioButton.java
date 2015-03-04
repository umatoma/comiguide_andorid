package net.umatoma.comiguide.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import net.umatoma.comiguide.R;

import java.util.HashMap;

public class ColorRadioButton extends RadioButton implements CompoundButton.OnCheckedChangeListener {

    private static final HashMap<String, Integer> COLOR_MAP = new HashMap<String, Integer>(){
        { put("black",  Color.parseColor("#000000")); }
        { put("gray",   Color.parseColor("#9e9e9e")); }
        { put("red",    Color.parseColor("#f44336")); }
        { put("green",  Color.parseColor("#4caf50")); }
        { put("blue",   Color.parseColor("#2196f3")); }
        { put("yellow", Color.parseColor("#ffeb3b")); }
        { put("orange", Color.parseColor("#ff5722")); }
    };
    private static final HashMap<String, Integer> CHECKED_COLOR_MAP = new HashMap<String, Integer>(){
        { put("black",  Color.parseColor("#000000")); }
        { put("gray",   Color.parseColor("#424242")); }
        { put("red",    Color.parseColor("#b71c1c")); }
        { put("green",  Color.parseColor("#1b5e20")); }
        { put("blue",   Color.parseColor("#0d47a1")); }
        { put("yellow", Color.parseColor("#f57f17")); }
        { put("orange", Color.parseColor("#e65100")); }
    };
    private static final int CHECKED_BORDER_COLOR = Color.parseColor("#607D8B");
    private String mColorValue = "black";

    public ColorRadioButton(Context context) {
        this(context, null);
    }

    public ColorRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.colorRadioButtonStyle);
    }

    public ColorRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColorRadioButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs, defStyleAttr);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(
                    attrs, R.styleable.ColorRadioButton, defStyleAttr, 0);
            if (a.hasValue(R.styleable.ColorRadioButton_colorValue)) {
                mColorValue = a.getString(R.styleable.ColorRadioButton_colorValue);
            }
            a.recycle();
        }

        updateBackgroundColor(isChecked());
        setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateBackgroundColor(isChecked);
    }

    private void updateBackgroundColor(boolean isChecked) {
        GradientDrawable gd = new GradientDrawable();
        if (isChecked) {
            gd.setColor(getCheckedColorCode(mColorValue));
            gd.setStroke(5, CHECKED_BORDER_COLOR);
        } else {
            gd.setColor(getColorCode(mColorValue));
            gd.setStroke(1, getCheckedColorCode(mColorValue));
        }
        gd.setCornerRadius(5.0f);
        setBackgroundDrawable(gd);
    }

    private int getColorCode(String colorValue) {
        if (COLOR_MAP.containsKey(colorValue)) {
            return COLOR_MAP.get(colorValue).intValue();
        } else {
            return COLOR_MAP.get("black").intValue();
        }
    }

    private int getCheckedColorCode(String colorValue) {
        if (CHECKED_COLOR_MAP.containsKey(colorValue)) {
            return CHECKED_COLOR_MAP.get(colorValue).intValue();
        } else {
            return CHECKED_COLOR_MAP.get("black").intValue();
        }
    }
}
