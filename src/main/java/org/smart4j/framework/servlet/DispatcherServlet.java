package org.smart4j.framework.servlet;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.constant.ConfigConstant;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.utils.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求转发器
 * Created by Administrator on 2018/2/23.
 */
@WebServlet(urlPatterns = "/",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        //注册容器加载类
        HelperLoader.init();
        ServletContext servletContext = config.getServletContext();
        //注册处理jsp的servlet
        ServletRegistration jspSerlvetRegister = servletContext.getServletRegistration("jsp");
        jspSerlvetRegister.addMapping(ConfigHelper.getJspPath()+"*");
        //注册处理静态资源的serlvet
        ServletRegistration  defualtSevletRegister = servletContext.getServletRegistration("default");
        defualtSevletRegister.addMapping(ConfigHelper.getAssetPath()+"*");

    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取请求路径和请求方法
        String requestMethod = request.getMethod().toLowerCase();
        String requestPath = request.getPathInfo();
        logger.info("request.getPathInfo()+++++++========"+requestPath);
        //获取处理器handler
        Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);
        if(handler!=null){
            //获取请求的controller
            Class<?> controllerClass = handler.getControllerClass();
            Object controllerBean = BeanHelper.getBean(controllerClass);
            //创建请求参数对象
            Map<String,Object> paramMap = new HashMap<String, Object>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()){
                String paramName = paramNames.nextElement();
                String paramValue = request.getParameter(paramName);
                paramMap.put(paramName,paramValue);
                logger.info("request.getParameterNames()+++++++========"+paramName);
            }
            //读取请求信息
            String body = CodeUtil.decode(StreamUtil.getString(request.getInputStream()));
            if(StringUtils.isNotEmpty(body)){
                //截取请求参数
                String[] params = StringUtils.split(body,"&");
                if(ArrayUtils.isNotEmpty(params)){
                    for (String param: params) {
                        String[] array = StringUtils.split(param,"=");
                        if(ArrayUtils.isNotEmpty(array) && array.length==2){
                            String paramName = array[0];
                            String paramValue = array[1];
                            paramMap.put(paramName,paramValue);
                            logger.info("request.getInputStream()=====name==="+paramName+"++++++value========"+paramValue);
                        }
                    }
                }
            }
            Param param = new Param(paramMap);
            //获取请求方法,并调用
            Method actionMethod = handler.getActionMethod();
            Object result ;
            if(param.isEmpty()){
                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod);
            }else {
                result = ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            }
            //处理action方法返回值
            if(result instanceof View){
                //返回jsp页面
                View view = (View)result;
                String path = view.getPath();
                if(StringUtils.isNotEmpty(path)){
                    if(path.startsWith("/")){
                        response.sendRedirect(request.getContextPath()+path);
                    }else {
                        Map<String,Object> model = view.getModel();
                        for (Map.Entry<String,Object> entry : model.entrySet()){
                            request.setAttribute(entry.getKey(),entry.getValue());
                        }
                        request.getRequestDispatcher(ConfigConstant.APP_JSP_PATH + path).forward(request,response);
                    }
                }
            }else if(result instanceof Data){
                //返回json数据
                Data data = (Data)result;
                Object model = data.getModel();
                if(model!=null){
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter writer = response.getWriter();
                    String json = JsonUtil.objToJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }
        }
    }

}
