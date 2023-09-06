package com.seo.mobilestore.common.enumeration;

public enum ENum {
    ZERO(0),
    ONE(1);
    private final int value;

    private ENum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
