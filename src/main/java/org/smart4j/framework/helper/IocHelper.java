package org.smart4j.framework.helper;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.smart4j.framework.annotation.Autowired;
import org.smart4j.framework.utils.ReflectionUtil;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类
 * Created by Administrator on 2018/2/23.
 */
public class IocHelper {

    /**
     * 找到所有的autowired注解对象，并设置为目标controller的成员变量
     */
    static {
        //获取bean容器
        Map<Class<?>,Object> beanMap = BeanHelper.getBeanMap();
        if(MapUtils.isNotEmpty(beanMap)){
            for (Map.Entry<Class<?>,Object> entry : beanMap.entrySet() ){
                Class<?> beanClass = entry.getKey();
                Object instance = entry.getValue();
                Field[] fields = beanClass.getDeclaredFields();
                if(ArrayUtils.isNotEmpty(fields)){
                    for (Field field :fields ){
                        if(field.isAnnotationPresent(Autowired.class)){
                            Class<?> fieldBeanClass = field.getType();
                            Object fieldBeanInstance = beanMap.get(fieldBeanClass);
                            if(fieldBeanInstance!=null){
                                ReflectionUtil.setField(instance,field,fieldBeanInstance);
                            }
                        }
                    }
                }
            }
        }
    }

}
