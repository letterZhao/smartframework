package org.smart4j.framework.helper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 处理器映射器
 * Created by Administrator on 2018/2/23.
 */
public class ControllerHelper {

    private static Logger logger = LoggerFactory.getLogger(ControllerHelper.class);

    //用于存放请求与处理器的映射关系
    private static final Map<Request,Handler> ACTION_MAP = new HashMap<Request, Handler>();

    //初始化请求与处理器的映射关系
    static {
        //获取所有的controller
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if(CollectionUtils.isNotEmpty(controllerClassSet)){
            //遍历所有的controller
            for (Class<?> clazz :controllerClassSet){
                //获取所有的method方法
                Method[] methods = clazz.getDeclaredMethods();
                if(ArrayUtils.isNotEmpty(methods)){
                    //遍历所有的method方法
                    for (Method method:methods) {
                        //判断当前方法是否带有action注解
                        if(method.isAnnotationPresent(Action.class)){
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();
                            //验证URL映射规则
                            if(mapping.matches("\\w+:/\\w*")){
                                String[] array = mapping.split(":");
                                if(ArrayUtils.isNotEmpty(array) && array.length == 2){
                                    String requestMethod = array[0];
                                    String requestPath = array[1];
                                    logger.info(requestMethod+"+++++++++++++++++"+requestPath);

                                    Request request = new Request(requestMethod,requestPath);
                                    Handler handler = new Handler(clazz,method);
                                    logger.info("method++++++++++++++++++"+method);
                                    //初始化actionMap
                                    ACTION_MAP.put(request,handler);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod , String requestPath){
        Request request = new Request(requestMethod,requestPath);
        return ACTION_MAP.get(request);
    }


}
