package org.smart4j.framework.utils;

import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.helper.IocHelper;

/**
 * 初始化
 * Created by Administrator on 2018/2/23.
 */
public class HelperLoader {

    public static final Class<?>[] classes ={
            ClassHelper.class, BeanHelper.class, ControllerHelper.class, IocHelper.class
    };

    public static void init(){
        for (Class<?> clazz: classes) {
            ClassUtil.loadClass(clazz.getName(),false);
        }
    }


}
