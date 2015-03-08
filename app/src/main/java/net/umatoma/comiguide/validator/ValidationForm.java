package net.umatoma.comiguide.validator;

import java.util.ArrayList;
import java.util.List;

public class ValidationForm {

    private List<AbstractValidator> mValidators = new ArrayList<>();

    public ValidationForm() {}

    public ValidationForm addValidator(AbstractValidator validator) {
        mValidators.add(validator);
        return this;
    }

    public boolean isValid() {
        boolean valid = true;
        for (AbstractValidator validator : mValidators) {
            valid = valid & validator.isValid();
        }
        return valid;
    }
}
