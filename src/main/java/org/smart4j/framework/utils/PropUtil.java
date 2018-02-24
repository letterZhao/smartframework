package org.smart4j.framework.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2018/2/9.
 */
public class PropUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropUtil.class);

    /**
     * 使用properties流读取文件
     * @return
     */
    public static Properties loadProperties(String fileName){
        Properties properties = new Properties();
        InputStream is = null;

        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(is == null ){
                throw new FileNotFoundException("文件"+fileName+"未找到");
            }
            properties.load(is);
        }catch (IOException e){
            e.printStackTrace();
            logger.error("load file error"+e);
        }finally {
            if(is!=null){
                try{
                    is.close();
                }catch (Exception e){
                    logger.error("is closed error"+e);
                }
            }
        }
        return properties;
    }

    /**
     * 获取String属性
     * @param p
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getString(Properties p,String key ,String defaultValue){
       String value = defaultValue;
       if(p.contains(key)){
           value = CastUtil.castString(p.getProperty(key));
       }
       return value;
    }

    /**
     * 获取int属性
     * @param p
     * @param key
     * @param defaultValue
     * @return
     */
    public static int getInt(Properties p , String key , int defaultValue){
        int value = defaultValue;
        if(p.contains(key)){
            value = CastUtil.castInt(p.getProperty(key));
        }
        return value;
    }
}
