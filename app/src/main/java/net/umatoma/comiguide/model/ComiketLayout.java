package net.umatoma.comiguide.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ComiketLayout {

    private int mId;
    private int mSpaceNo;
    private int mLayout;
    private ComiketBlock mComiketBlock;

    public ComiketLayout(JSONObject layout) throws JSONException {
        mId = layout.getInt("id");
        mSpaceNo = layout.getInt("space_no");
        mLayout = layout.getInt("layout");
        mComiketBlock = new ComiketBlock(layout.getJSONObject("cblock"));
    }

    public int getSpaceNo() {
        return mSpaceNo;
    }

    public ComiketBlock getComiketBlock() {
        return mComiketBlock;
    }
}
