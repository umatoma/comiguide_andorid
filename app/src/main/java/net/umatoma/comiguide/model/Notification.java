package net.umatoma.comiguide.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Notification {

    private int mId;
    private String mTitle;
    private String mContent;
    private String mSummary;

    public Notification(JSONObject notification) throws JSONException {
        mId = notification.getInt("id");
        mTitle = notification.getString("title");
        mContent = notification.getString("content");

        if (mContent.length() > 50) {
            mSummary = mContent.substring(0, 50) + "...";
        }
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public String getSummary() {
        return mSummary;
    }
}
