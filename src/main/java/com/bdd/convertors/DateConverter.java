package com.bdd.convertors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static Date convert(String dateString) {
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }
    public static String toString(String dateString) {
        try {
            Date expectedDate = format.parse(dateString);
            String newDate = format.format(expectedDate);
            Date actualDate =  format.parse(newDate);
            if(actualDate.equals(expectedDate)) {
                return newDate;
            }
            return null;
        } catch (ParseException e) {
            return null;
        }
    }
}
