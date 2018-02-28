package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 封装HttpServletRequest对象和HttpServletResponse对象
 * 并使其线程安全
 * Created by Administrator on 2018/2/28.
 */
public class ServletHelper {

    private static Logger logger = LoggerFactory.getLogger(ServletHelper.class);
    private static final ThreadLocal<ServletHelper> SERVLET_HOLDER = new ThreadLocal<ServletHelper>();

    private HttpServletRequest request;
    private HttpServletResponse response;

    /**
     * 使得每个线程拥有独立的servletHelper对象，保证线程安全
     * @param request
     * @param response
     */
    private ServletHelper(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    /**
     * 初始化
     * @param request
     * @param response
     */
    public static void init(HttpServletRequest request,HttpServletResponse response){
        SERVLET_HOLDER.set(new ServletHelper(request,response));
    }

    /**
     * 销毁
     */
    public static void destory(){
        SERVLET_HOLDER.remove();
    }


    /**
     * 获取request对象
     * @return
     */
    private static HttpServletRequest getRequest(){
        return SERVLET_HOLDER.get().request;
    }

    /**
     * 获取response对象
     * @return
     */
    private static HttpServletResponse getResponse(){
        return SERVLET_HOLDER.get().response;
    }

    /**
     * 获取session对象
     * @return
     */
    private static HttpSession getSession(){
        return getRequest().getSession();
    }


    /**
     * 获取serlvetContext对象
     * @return
     */
    private static ServletContext getServletContext(){
        return getRequest().getServletContext();
    }

    /**
     * 将属性放入request中
     * @param key
     * @param value
     */
    public static void setRequestAttribute(String key,Object value){
        getRequest().setAttribute(key,value);
    }

    /**
     * 从request当中获取属性
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getRequestAttribute(String key){
        return (T)getRequest().getAttribute(key);
    }

    /**
     * 从request当中移除属性
     * @param key
     */
    public static void removeRequestAttribute(String key){
        getRequest().removeAttribute(key);
    }

    /**
     * 发送重定向响应
     * @param location
     */
    public static void sendRedirect(String location){
        try {
            getResponse().sendRedirect(getRequest().getContextPath()+location);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("sendRedirect failure"+e);
        }
    }


    /**
     * 将属性放入session中
     * @param key
     * @param value
     */
    public static void setSessionAttribute(String key ,Object value){
        getSession().setAttribute(key,value);
    }

    /**
     * 从session中获取属性
     * @param key
     * @param <T>
     * @return
     */
    public static <T> T getSessionAttribute(String key){
        return (T)getSession().getAttribute(key);
    }

    /**
     * 从seesion中移除属性
     * @param key
     */
    public static void removeSessionAttribute(String key){
        getSession().removeAttribute(key);
    }

    /**
     * 使seesion失效
     */
    public static void invalidateSession(){
        getSession().invalidate();
    }



}
