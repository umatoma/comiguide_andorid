package net.umatoma.comiguide.model;


import org.json.JSONException;
import org.json.JSONObject;

public class ComiketBlock {

    private int mId;
    private int mCareaId;
    private int mPosX;
    private int mPosY;
    private String mName;
    private ComiketArea mComiketArea;

    public ComiketBlock(JSONObject block) throws JSONException {
        mId = block.getInt("id");
        mCareaId = block.getInt("carea_id");
        mPosX = block.getInt("pos_x");
        mPosY = block.getInt("pos_y");
        mName = block.getString("name");
        mComiketArea = new ComiketArea(block.getJSONObject("carea"));
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public ComiketArea getComiketArea() {
        return mComiketArea;
    }
}
