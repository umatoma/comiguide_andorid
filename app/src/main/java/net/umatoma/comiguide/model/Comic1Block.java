package net.umatoma.comiguide.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Comic1Block {

    private int mId;
    private int mPosX;
    private int mPosY;
    private String mName;

    public Comic1Block(JSONObject block) throws JSONException {
        mId = block.getInt("id");
        mPosX = block.getInt("pos_x");
        mPosY = block.getInt("pos_y");
        mName = block.getString("name");
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }
}
