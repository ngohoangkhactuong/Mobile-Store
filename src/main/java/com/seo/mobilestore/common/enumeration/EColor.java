package com.seo.mobilestore.common.enumeration;

public enum EColor {

    Black("Black"),
    Green("Green"),
    Blue("Blue"),
    Red("Red"),
    Pink("Pink"),
    Pubple("Pubple");

    private final String name;

    EColor(String NAME){
        this.name = NAME;
    }
    @Override
    public String toString() {
        return name;
    }
}
