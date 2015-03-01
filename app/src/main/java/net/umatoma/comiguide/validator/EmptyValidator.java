package net.umatoma.comiguide.validator;

import android.text.TextUtils;

public class EmptyValidator extends AbstractValidator {

    private String mTargetValue;

    public EmptyValidator(String value) {
        mTargetValue = value;
    }

    @Override
    public boolean isValid() {
        return !TextUtils.isEmpty(mTargetValue);
    }
}
