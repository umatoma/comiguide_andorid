package net.umatoma.comiguide.model;

import org.json.JSONException;
import org.json.JSONObject;

public class ComiketCircle {

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

    public ComiketCircle(JSONObject circle) throws JSONException {
        mId = circle.getInt("id");
        mComiketId = circle.getInt("comiket_id");
        mClayoutId = circle.getInt("clayout_id");
        mDay = circle.getInt("day");
        mSpaceNoSub = circle.getString("space_no_sub");
        mCircleName = circle.getString("circle_name");
        mCircleUrl = circle.getString("circle_url");
        mComment = circle.getString("cost");
        mColor = circle.getString("color");
    }

    public String getCircleName() {
        return mCircleName;
    }
}
