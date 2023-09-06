package com.seo.mobilestore.common.enumeration;

public enum Estatus {
    Active("Active"),
    Default("Default"),
    Transport("Transport"),
    Disable("Disable"),
    Cancel("Cancel"),
    Delete("Delete");

    public static int activeStatus = 1;
    public static int defaultStatus = 2;
    public static int transportStatus = 3;
    public static int disableStatus = 4;
    public static int cancelStatus = 5;
    public static int deleteStatus = 6;

    private final String NAME;

    Estatus(String string) {
        this.NAME = string;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
