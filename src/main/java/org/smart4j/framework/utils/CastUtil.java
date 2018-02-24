package org.smart4j.framework.utils;


/**
 * Created by Administrator on 2018/2/9.
 */
public class CastUtil {

    public static String castString(Object obj){
        String defaultValue = "";
        return obj != null ? String.valueOf(obj) : defaultValue;
    }

    public static int castInt(Object obj){
        int value = 0;
        if(obj!=null){
            Integer.parseInt(castString(obj));
        }
        return value;
    }
    public static long castLong(Object obj){
        int value = 0;
        if(obj!=null){
            Long.parseLong(castString(obj));
        }
        return value;
    }

    public static boolean castBoolean(Object obj){
        boolean vaule = false;
        if(obj!=null){
            Boolean.parseBoolean(castString(obj));
        }
        return vaule;
    }
}
