package net.umatoma.comiguide.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ComiketKigyo {
    private int mId;
    private int mComiketId;
    private int mKigyoNo;
    private int mX;
    private int mY;
    private int mW;
    private int mH;
    private int mMapPosX;
    private int mMapPosY;
    private String mName;

    public ComiketKigyo(JSONObject block) throws JSONException {
        mId = block.getInt("id");
        mComiketId = block.getInt("comiket_id");
        mKigyoNo = block.getInt("kigyo_no");
        mX = block.getInt("x");
        mY = block.getInt("y");
        mW = block.getInt("w");
        mH = block.getInt("h");
        mMapPosX = block.getInt("map_pos_x");
        mMapPosY = block.getInt("map_pos_y");
        mName = block.getString("name");
    }

    public int getId() {
        return mId;
    }

    public int getComiketId() {
        return mComiketId;
    }

    public int getKigyoNo() {
        return mKigyoNo;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getW() {
        return mW;
    }

    public int getH() {
        return mH;
    }

    public int getMapPosX() {
        return mMapPosX;
    }

    public int getMapPosY() {
        return mMapPosY;
    }

    public String getName() {
        return mName;
    }

}
