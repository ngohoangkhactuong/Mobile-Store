package com.seo.mobilestore.exception;

import java.util.Map;

public class ResourceNotFoundException extends ExceptionCustom {

    public ResourceNotFoundException(Map<String, Object> errors) {
        super("DATA NOT FOUND", errors);
    }



}