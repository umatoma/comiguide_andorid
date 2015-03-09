package net.umatoma.comiguide.validator;


import android.content.Context;
import android.text.TextUtils;

public abstract class AbstractValidator {

    protected Context mContext;

    public AbstractValidator(Context context) {
        mContext = context;
    }

    public abstract boolean isValid();

    public abstract String getErrorMessage();

    protected Context getContext() {
        return mContext;
    }

    protected boolean isNotEmpty(String text) {
        return !TextUtils.isEmpty(text);
    }

}
