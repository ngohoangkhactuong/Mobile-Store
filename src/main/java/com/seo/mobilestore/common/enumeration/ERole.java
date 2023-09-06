package com.seo.mobilestore.common.enumeration;

public enum ERole {

    Admin("Role_Admin"),
    Customer("Role_Customer");

    private final String NAME;

    ERole(String NAME) {
        this.NAME = NAME;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
