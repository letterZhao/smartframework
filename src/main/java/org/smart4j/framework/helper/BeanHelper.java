package org.smart4j.framework.helper;

import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.utils.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 类实例化容器
 * Created by Administrator on 2018/2/23.
 */
public class BeanHelper {

    public static final Map<Class<?>,Object> BEAN_MAP = new HashMap<Class<?>, Object>();//类容器

    static {
        Set<Class<?>> classSET = ClassHelper.getBeanClassSet();//获取所有class
        for(Class<?> clazz : classSET ){
            Object instance = ReflectionUtil.newInstance(clazz);//实例化class
            BEAN_MAP.put(clazz,instance);
        }
    }

    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**
     * 获取bean对象
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> tClass){
        if(!BEAN_MAP.containsKey(tClass)){
            throw new RuntimeException("can not get bean by class"+tClass);
        }
        return (T)BEAN_MAP.get(tClass);
    }


}
