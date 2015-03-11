package net.umatoma.comiguide.validator;

import android.content.Context;

import net.umatoma.comiguide.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator extends AbstractValidator {

    private String mTargetValue;

    public EmailValidator(Context context, String value) {
        super(context);
        mTargetValue = value;
    }

    @Override
    public boolean isValid() {
        if (isNotEmpty(mTargetValue)) {
            Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher matcher = pattern.matcher(mTargetValue);
            return matcher.matches();
        }
        return false;
    }

    @Override
    public String getErrorMessage() {
        return getContext().getString(R.string.validate_error_email);
    }
}
