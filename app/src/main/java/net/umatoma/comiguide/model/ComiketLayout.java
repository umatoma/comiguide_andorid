package net.umatoma.comiguide.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ComiketLayout {

    private int mId = -1;
    private int mCblockId = -1;
    private int mSpaceNo;
    private int mLayout = 1;
    private int mPosX;
    private int mPosY;
    private ComiketBlock mComiketBlock;

    public ComiketLayout(JSONObject layout) throws JSONException {
        mId = layout.getInt("id");
        mSpaceNo = layout.getInt("space_no");
        mLayout = layout.getInt("layout");
        mPosX = layout.getInt("pos_x");
        mPosY = layout.getInt("pos_y");
        mComiketBlock = new ComiketBlock(layout.getJSONObject("cblock"));
    }

    public int getId() {
        return mId;
    }

    public int getCblockId() {
        return mCblockId;
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

    public ComiketBlock getComiketBlock() {
        return mComiketBlock;
    }
}
