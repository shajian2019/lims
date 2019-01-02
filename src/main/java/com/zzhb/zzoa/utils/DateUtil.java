package com.zzhb.zzoa.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String dateToStrings(){
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowStr = sdf.format(now);
        return nowStr;
    }
}
