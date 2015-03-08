package net.umatoma.comiguide.validator;


public abstract class AbstractValidator {

    private String mErrorMessage;

    public AbstractValidator() {}

    public abstract boolean isValid();

    public String getErrorMessage() {
        return mErrorMessage;
    }

}
