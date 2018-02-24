package org.smart4j.framework.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2018/2/23.
 */
public final class StreamUtil {

    /**
     * 从输入流中读取字符串
     * @param is
     * @return
     */
    public static String getString(InputStream is){
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        try {
            if((line=reader.readLine())!=null){
                sb.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }


}
