package net.umatoma.comiguide.model;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ComiketCircle {

    private static final HashMap<String, Integer> COLOR_MAP = new HashMap<String, Integer>(){
        { put("black",  Color.parseColor("#000000")); }
        { put("red",    Color.parseColor("#f44336")); }
        { put("green",  Color.parseColor("#4caf50")); }
        { put("blue",   Color.parseColor("#2196f3")); }
        { put("yellow", Color.parseColor("#ffeb3b")); }
        { put("orange", Color.parseColor("#ff5722")); }
    };

    private int mId;
    private int mComiketId;
    private int mClayoutId;
    private int mDay;
    private String mSpaceNoSub;
    private String mCircleName;
    private String mCircleUrl;
    private String mComment;
    private String mCost;
    private String mColor;
    private ComiketLayout mComiketLayout;

    public ComiketCircle(JSONObject circle) throws JSONException {
        mId = circle.getInt("id");
        mComiketId = circle.getInt("comiket_id");
        mClayoutId = circle.getInt("clayout_id");
        mDay = circle.getInt("day");
        mSpaceNoSub = circle.getString("space_no_sub");
        mCircleName = circle.getString("circle_name");
        mCircleUrl = circle.getString("circle_url");
        mComment = circle.getString("comment");
        mCost = circle.getString("cost");
        mColor = circle.getString("color");
        mComiketLayout = new ComiketLayout(circle.getJSONObject("clayout"));
    }

    public int getId() {
        return mId;
    }

    public String getSpaceNoSub() {
        return mSpaceNoSub;
    }

    public String getCircleName() {
        return mCircleName;
    }

    public String getCircleUrl() {
        return mCircleUrl;
    }

    public String getComment() {
        return mComment;
    }

    public String getCost() {
        return mCost;
    }

    public int getColor() {
        if (COLOR_MAP.containsKey(mColor)) {
            return COLOR_MAP.get(mColor).intValue();
        } else {
            return COLOR_MAP.get("black").intValue();
        }
    }

    public String getSpaceInfo() {
        ComiketLayout layout = mComiketLayout;
        ComiketBlock block = layout.getComiketBlock();
        ComiketArea area = block.getComiketArea();

        return new StringBuilder()
                .append(area.getName())
                .append(block.getName())
                .append(" - ")
                .append(layout.getSpaceNo())
                .append(mSpaceNoSub)
                .toString();
    }

    public ComiketLayout getComiketLayout() {
        return mComiketLayout;
    }
}