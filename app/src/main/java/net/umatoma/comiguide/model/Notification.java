package net.umatoma.comiguide.model;

public class Notification {

    private int mId;
    private String mTitle;
    private String mSummary;
    private String mContent;

    public Notification(int id, String title, String summary, String content) {
        mId = id;
        mTitle = title;
        mSummary = summary;
        mContent = content;
    }

    public int getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSummary() {
        return mSummary;
    }

    public String getContent() {
        return mContent;
    }
}
