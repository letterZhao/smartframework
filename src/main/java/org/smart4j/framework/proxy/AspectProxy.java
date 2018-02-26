package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 切面代理
 * Created by Administrator on 2018/2/25.
 */
public class AspectProxy implements Proxy {

    private Logger logger = LoggerFactory.getLogger(AspectProxy.class);

    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Class<?> cls = proxyChain.getTargetClass();
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodPrams();

        begin();
        try {
            if(intercept(cls,method,params)){
                before(cls,method,params);
                result = proxyChain.doProxyChain();
                after(cls,method,params);
            }else {
                result = proxyChain.doProxyChain();
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("proxy failure");
            error(cls,method,params);
        }finally {
            end();
        }
        return result;
    }


    public void begin(){

    }

    public boolean intercept(Class<?> cls,Method method,Object[] params) throws Throwable{
        return true;
    }

    public void before(Class<?> cls,Method method,Object[] params) throws Throwable{

    }

    public void after(Class<?> cls,Method method,Object[] params) throws Throwable{

    }

    public void error(Class<?> cls,Method method,Object[] params) throws Throwable{

    }

    public void end(){

    }

}
