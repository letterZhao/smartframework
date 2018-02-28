package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.proxy.AspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;
import org.smart4j.framework.proxy.TransactionProxy;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * aop框架
 * Created by Administrator on 2018/2/26.
 */
public class AopHelper {

    private static Logger logger = LoggerFactory.getLogger(AopHelper.class);

    //初始化aop框架
    static {
        try {
            Map<Class<?>,Set<Class<?>>>  proxyMap = createProxyMap();
            Map<Class<?>,List<Proxy>>  targetMap = createTargetMap(proxyMap);
            for(Map.Entry<Class<?>,List<Proxy>> targetEntry : targetMap.entrySet()) {
                Class<?> targetClass = targetEntry.getKey();
                List<Proxy> proxyList = targetEntry.getValue();
                Object proxy = ProxyManager.createProxy(targetClass,proxyList);
                BeanHelper.setBean(targetClass,proxy);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("aop init failure " + e);
        }
    }


    /**
     * 获取aspect注解中设置的所有类
     * @param aspect
     * @return
     * @throws Exception
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
        Set<Class<?>> targetClass = new HashSet<Class<?>>();
        Class<? extends Annotation> annotation = aspect.value();
        if(annotation!=null && !annotation.equals(Aspect.class)){
            targetClass.addAll(ClassHelper.getClassByAnnotation(annotation));
        }
        return targetClass;
    }

    /**
     * 映射代理类和目标集合之间的关系
     * @return
     * @throws Exception
     */
    private static Map<Class<?>,Set<Class<?>>> createProxyMap() throws Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
        //获取所有代理类(继承了aspectProxy类)
        Set<Class<?>> proxyClassSet = ClassHelper.getClassBySuper(AspectProxy.class);
        //遍历所有代理类并找到带有aspect的注解
        for (Class<?> proxyClass:proxyClassSet) {
            if(proxyClass.isAnnotationPresent(Aspect.class)){
                //获取Aspect
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                //获取aspect注解中设置的所有类
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass,targetClassSet);
            }
        }
        return proxyMap;
    }


    /**
     * 映射目标类与代理对象列表之间的关系
     * @param proxyMap
     * @return
     * @throws Exception
     */
    public static Map<Class<?>,List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap)throws  Exception{
        Map<Class<?>,List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
        //遍历代理集合
        for (Map.Entry<Class<?>,Set<Class<?>>> proxyEntry:proxyMap.entrySet()) {
            //获取代理类
            Class<?> proxyClass = proxyEntry.getKey();
            //获取目标对象集合
            Set<Class<?>> targetClassSet = proxyEntry.getValue();
            for (Class<?> targetClass : targetClassSet) {
                //获取代理实例
                Proxy proxy = (Proxy)proxyClass.newInstance();
                if(targetMap.containsKey(targetClass)){
                    targetMap.get(targetClass).add(proxy);
                }else {
                    List<Proxy> proxyList = new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass,proxyList);
                }
            }
        }
        return targetMap;
    }

    /**
     * 添加普通代理
     * @param proxyMap
     * @throws Throwable
     */
    private static void addAspectProxy(Map<Class<?>,Set<Class<?>>> proxyMap) throws Throwable{
        Set<Class<?>> proxyClassSet = ClassHelper.getClassBySuper(AspectProxy.class);
        for (Class<?> proxyClass: proxyClassSet) {
            if(proxyClass.isAnnotationPresent(Aspect.class)){
                Aspect aspect = proxyClass.getAnnotation(Aspect.class);
                Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
                proxyMap.put(proxyClass,targetClassSet);
            }
        }
    }

    /**
     * 添加事务代理
     * @param proxyMap
     * @throws Throwable
     */
    private static void addTransactionProxy(Map<Class<?>,Set<Class<?>>> proxyMap) throws Throwable{
        Set<Class<?>> proxyClassSet = ClassHelper.getClassByAnnotation(Service.class);
        proxyMap.put(TransactionProxy.class,proxyClassSet);
    }

}
