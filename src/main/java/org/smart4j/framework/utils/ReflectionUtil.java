package org.smart4j.framework.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 * Created by Administrator on 2018/2/23.
 */
public class ReflectionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建实例
     * @param clazz
     * @return
     */
    public static Object newInstance(Class<?> clazz){
        Object instance = null;
        try {
            instance =  clazz.newInstance();
        }catch (Exception e){
            e.printStackTrace();
            logger.error("new instance failure ",e);
        }
        return instance;
    }

    /**
     * 调用方法
     * @param obj
     * @param method
     * @param objs
     * @return
     */
    public static Object invokeMethod(Object obj, Method method,Object... objs){
        Object result = null;
        try {
            method.setAccessible(true);
            result =  method.invoke(obj,objs);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("new instance failure ",e);
        }
        return result;
    }


    /**
     * 设置成员变量
     * @param obj
     * @param field
     * @param value
     */
    public static void setField(Object obj, Field field,Object value){
        try {
            field.setAccessible(true);
            field.set(obj,value);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("new instance failure ",e);
        }
    }

}
