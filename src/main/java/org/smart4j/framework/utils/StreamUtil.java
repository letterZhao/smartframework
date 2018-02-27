package org.smart4j.framework.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

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

    /**
     * 从输入流复制到输出流
     * @param inputStream
     * @param outputStream
     */
    public static void copyStream(InputStream inputStream, OutputStream outputStream){
        try {
            int length;
            byte[] buffer = new byte[1024*4];
            while ((length=inputStream.read(buffer,0,buffer.length))!=-1){
                outputStream.write(buffer,0,length);
            }
            outputStream.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
                outputStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
