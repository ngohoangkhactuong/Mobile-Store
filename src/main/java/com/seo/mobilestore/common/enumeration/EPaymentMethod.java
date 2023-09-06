package com.seo.mobilestore.common.enumeration;

public enum EPaymentMethod {
    Momo("Momo"),
    VNPay("VNPay"),
    Banking("Banking"),
    Cash("Cash");

    public static int momoPaymentMethod = 1;
    public static int vnpayPaymentMethod = 2;
    public static int bankingPaymentMethod = 3;
    public static int cashPaymentMethod = 4;

    private final String NAME;

    EPaymentMethod(String string) {
        this.NAME = string;
    }

    @Override
    public String toString() {
        return NAME;
    }
}
