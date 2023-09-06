package com.seo.mobilestore.common.util;

import java.time.LocalDateTime;

public class DateTimeHelper {

    public static LocalDateTime getEarliestTimeOfDate(LocalDateTime datetime) {
        return datetime == null ? null
                : LocalDateTime.of(datetime.getYear(), datetime.getMonth(), datetime.getDayOfMonth(), 0, 0, 0);
    }

    public static LocalDateTime getDateTimeOfMonthAgo(int monthAgo){
        return LocalDateTime.now().minusMonths(monthAgo);
    }

}
