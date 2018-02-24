package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.utils.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 类操作帮助类
 * Created by Administrator on 2018/2/23.
 */
public class ClassHelper {


    private static final Set<Class<?>> CLASS_SET ;

    static {
        CLASS_SET = ClassUtil.getClassSet(ConfigHelper.getBasePackage());
    }

    /**
     * 获取包下面所有的类
     * @return
     */
    public static Set<Class<?>> getClassSet( ){
        return CLASS_SET;
    }

    /**
     * 获取所有@service的类
     * @return
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for(Class clazz : classSet ){
            if(clazz.isAnnotationPresent(Service.class)){
                classSet.add(clazz);
            }
        }
        return classSet;
    }


    /**
     * 获取所有@controller的类
     * @return
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        for(Class clazz : classSet ){
            if(clazz.isAnnotationPresent(Controller.class)){
                classSet.add(clazz);
            }
        }
        return classSet;
    }
    /**
     * 获取所有@controller和@service的类
     * @return
     */
    public static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        classSet.addAll(getServiceClassSet());
        classSet.addAll(getControllerClassSet());
        return classSet;
    }


}
