package net.umatoma.comiguide.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ComiketArea {

    private int mId;
    private int mCmapId;
    private String mName;
    private String mSimpleName;

    public ComiketArea(JSONObject area) throws JSONException {
        mId = area.getInt("id");
        mCmapId = area.getInt("cmap_id");
        mName = area.getString("name");
        mSimpleName = area.getString("simple_name");
    }

    public int getCmapId() {
        return mCmapId;
    }

    public String getName() {
        return mName;
    }

    public String getSimpleName() {
        return mSimpleName;
    }
}
