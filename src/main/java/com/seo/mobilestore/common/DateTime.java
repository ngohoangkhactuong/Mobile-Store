package com.seo.mobilestore.common;

import java.util.Date;

public class DateTime {
    private static Date CREATE_DATE;

    public synchronized static Date getInstances() {
        if (CREATE_DATE == null)
            CREATE_DATE = DateTime.getInstances();
        return CREATE_DATE;
    }

    public synchronized static void setTimeNow() {
        CREATE_DATE = DateTime.getInstances();
    }
}
