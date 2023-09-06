package com.seo.mobilestore.service;

public interface JsonReaderService{
    <U> U readValue(String content, Class<U> valueType);
}
