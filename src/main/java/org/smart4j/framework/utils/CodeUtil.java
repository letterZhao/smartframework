package org.smart4j.framework.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2018/2/23.
 */
public class CodeUtil {

    /**
     * 编码
     * @param source
     * @return
     */
    public static String encode(String source){
        String target = null;
        try {
            target = URLEncoder.encode(source,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return target;
    }
    /**
     * 解码
     * @param source
     * @return
     */
    public static String decode(String source){
        String target = null;
        try {
            target = URLDecoder.decode(source,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return target;
    }

}
