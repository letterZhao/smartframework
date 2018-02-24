package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装action信息
 * Created by Administrator on 2018/2/23.
 */
public class Handler {

    /**
     * 所对应的controller类
     */
    private Class<?> controllerClass;

    /**
     * action方法
     */
    private Method actionMethod;

    public Handler() {
    }

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }
}
