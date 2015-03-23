package net.umatoma.comiguide.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Comic1Layout {

    private int mId = -1;
    private int mC1blockId = -1;
    private int mSpaceNo;
    private int mLayout = 1;
    private int mPosX;
    private int mPosY;
    private int mMapPosX;
    private int mMapPosY;
    private Comic1Block mComic1Block;

    public Comic1Layout(JSONObject layout) throws JSONException {
        mId = layout.getInt("id");
        mC1blockId = layout.getInt("c1block_id");
        mSpaceNo = layout.getInt("space_no");
        mLayout = layout.getInt("layout");
        mPosX = layout.getInt("pos_x");
        mPosY = layout.getInt("pos_y");
        mMapPosX = layout.getInt("map_pos_x");
        mMapPosY = layout.getInt("map_pos_y");
        mComic1Block = new Comic1Block(layout.getJSONObject("c1block"));
    }

    public int getId() {
        return mId;
    }

    public int getC1blockId() {
        return mC1blockId;
    }

    public int getLayout() {
        return mLayout;
    }

    public int getSpaceNo() {
        return mSpaceNo;
    }

    public int getPosX() {
        return mPosX;
    }

    public int getPosY() {
        return mPosY;
    }

    public int getMapPosX() {
        return mMapPosX;
    }

    public int getMapPosY() {
        return mMapPosY;
    }

    public Comic1Block getComic1Block() {
        return mComic1Block;
    }
}
