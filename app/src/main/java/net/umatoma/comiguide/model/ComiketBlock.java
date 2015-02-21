package net.umatoma.comiguide.model;


import org.json.JSONException;
import org.json.JSONObject;

public class ComiketBlock {

    private int mId;
    private int mCareaId;
    private String mName;

    public ComiketBlock(JSONObject block) throws JSONException {
        mId = block.getInt("id");
        mCareaId = block.getInt("carea_id");
        mName = block.getString("name");
    }
}
