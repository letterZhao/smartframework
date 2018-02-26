package org.smart4j.framework.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 代理链,执行顺序取决于代理链上的顺序n
 * Created by Administrator on 2018/2/25.
 */
public class ProxyChain {

    private final Class<?> targetClass;//目标类
    private final Object targetObject;//目标对象
    private final Method targetMethod;//目标方法
    private final MethodProxy methodProxy;//方法代理
    private final Object[] methodPrams;//方法参数

    private List<Proxy> proxyList = new ArrayList<Proxy>();

    private int proxyIndex = 0;

    public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod,
                      MethodProxy methodProxy, Object[] methodPrams, List<Proxy> proxyList) {
        this.targetClass = targetClass;
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodPrams = methodPrams;
        this.proxyList = proxyList;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodPrams() {
        return methodPrams;
    }

    public Object doProxyChain() throws Throwable{
        Object methodResult;
        if(proxyIndex<proxyList.size()){
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        }else {
            methodResult = methodProxy.invokeSuper(targetObject,methodPrams);
        }
        return methodResult;
    }

}
