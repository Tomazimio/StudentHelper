package net.venedi.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtil {

    private static String TIMESTAMP_SQLITE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_SQLITE_FORMAT);
        return sdf.format(new Date());
    }

    public static String parseDate(Date date){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_SQLITE_FORMAT);
            return sdf.format(date);
        }catch (Exception e) {
        }
        return "";
    }

    public static String parseDate(Date date, String pattern){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(date);
        }catch (Exception e) {
        }
        return "";
    }

    public static Date parseDate(String date){
        try{
            SimpleDateFormat sdf = new SimpleDateFormat(TIMESTAMP_SQLITE_FORMAT);
            return sdf.parse(date);
        }catch (Exception e) {
        }
        return null;
    }

    public static Date getDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

}
