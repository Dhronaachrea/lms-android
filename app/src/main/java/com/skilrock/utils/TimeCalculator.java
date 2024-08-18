package com.skilrock.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class TimeCalculator {

    public static long timestampToMilliseconds(String dateValue) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
            java.util.Date date = sdf.parse(dateValue);
            long responseMilli = date.getTime();
            return responseMilli;

        } catch (ParseException e) {

            return 0;

        }
    }

    public static String convertSecondsToHMmSs(long seconds) {
        long h = (long) Math.floor((seconds / (60 * 60)));
        long m = (long) Math.floor(((seconds - (h * 60 * 60)) / (60)));
        long s = (long) Math.floor((seconds - (h * 60 * 60) - (m * 60)));

        return String.format("%d:%02d:%02d", h, m, s);
    }
}
