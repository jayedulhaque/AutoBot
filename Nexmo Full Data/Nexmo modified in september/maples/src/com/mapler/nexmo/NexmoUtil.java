package com.mapler.nexmo;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class NexmoUtil {

    public static long getCurrentTimeInUTCSecond(long ts) {
        Date localTime = new Date(ts);
        String format = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // Convert Local Time to UTC (Works Fine)
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmtTime = new Date(sdf.format(localTime));
        long gmtTimeInSec=gmtTime.getTime()/1000;
        return gmtTimeInSec;
    }
     public static String getCurrentTimeInUTCDate(Date localTime){
         String format = "yyyy/MM/dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        // Convert Local Time to UTC (Works Fine)
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date gmtTime = new Date(sdf.format(localTime));
        Timestamp ts_now = new Timestamp(gmtTime.getTime());
        return ts_now.toString();
     }
}
