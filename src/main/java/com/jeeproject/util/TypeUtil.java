package com.jeeproject.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final Date minDate = new Date(-30610224000000L);
    private static final Date maxDate = new Date(253400659199000L);

    public static Date getDateFromString(String str) {
        if (str == null) {return null;}
        Date date;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            return null;
        }
        if (date.before(minDate) || date.after(maxDate)) {return null;}
        return date;
    }

    public static int getIntFromString(String str) {
        if (str == null) {return -1;}
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static double getDoubleFromString(String str) {
        if (str == null) {return -1;}
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
