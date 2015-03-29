package net.umatoma.comiguide.model;

import android.graphics.Color;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ComiketKigyoChecklist {

    private static final HashMap<String, Integer> COLOR_MAP = new HashMap<String, Integer>(){
        { put("black",  Color.parseColor("#000000")); }
        { put("gray",   Color.parseColor("#9e9e9e")); }
        { put("red",    Color.parseColor("#f44336")); }
        { put("green",  Color.parseColor("#4caf50")); }
        { put("blue",   Color.parseColor("#2196f3")); }
        { put("yellow", Color.parseColor("#ffeb3b")); }
        { put("orange", Color.parseColor("#ff5722")); }
    };

    private int mId = -1;
    private int mCkigyoId = -1;
    private String mComment;
    private String mCost;
    private String mColor = "black";
    private ComiketKigyo mComiketKigyo;

    public ComiketKigyoChecklist(JSONObject circle) throws JSONException {
        mId = circle.getInt("id");
        mCkigyoId = circle.getInt("ckigyo_id");
        mComment = circle.getString("comment");
        mCost = circle.getString("cost");
        mColor = circle.getString("color");
        mComiketKigyo = new ComiketKigyo(circle.getJSONObject("ckigyo"));
    }

    public ComiketKigyoChecklist() {}

    public int getId() {
        return mId;
    }

    public int getCkigyoId() {
        return mCkigyoId;
    }

    public String getComment() {
        return mComment;
    }

    public String getCost() {
        return mCost;
    }

    public String getColor() {
        return mColor;
    }

    public int getColorCode() {
        if (COLOR_MAP.containsKey(mColor)) {
            return COLOR_MAP.get(mColor).intValue();
        } else {
            return COLOR_MAP.get("black").intValue();
        }
    }

    public String getSpaceInfo() {
        return new StringBuilder()
                .append(mComiketKigyo.getKigyoNo())
                .append(" - ")
                .append(mComiketKigyo.getName())
                .toString();
    }

    public ComiketKigyo getComiketKigyo() {
        return mComiketKigyo;
    }

    public boolean isCreated() {
        return mId > 0;
    }
}
