package net.umatoma.comiguide.validator;

import android.content.Context;
import android.text.TextUtils;

import net.umatoma.comiguide.R;

public class EmptyValidator extends AbstractValidator {

    private String mTargetValue;

    public EmptyValidator(Context context, String value) {
        super(context);
        mTargetValue = value;
    }

    @Override
    public boolean isValid() {
        return !TextUtils.isEmpty(mTargetValue);
    }

    @Override
    public String getErrorMessage() {
        return getContext().getString(R.string.validate_error_empty);
    }
}
