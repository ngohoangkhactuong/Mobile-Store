package com.seo.mobilestore.exception;

public class CannotDeleteException extends ExceptionCustom {

    public CannotDeleteException(Object errors) {
        super("CANNOT DELETE", errors);
    }
}
