package org.smart4j.framework.servlet;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.constant.ConfigConstant;
import org.smart4j.framework.helper.*;
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

        UploadHelper.init(servletContext);
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //整合serlvetHelper类
        ServletHelper.init(request,response);
        try {
            //获取请求路径和请求方法
            String requestMethod = request.getMethod().toLowerCase();
            String requestPath = request.getPathInfo();

            if(requestPath.equals("/favicon.ico")){
                return;
            }

            logger.info("request.getPathInfo()+++++++========"+requestPath);
            //获取处理器handler
            Handler handler = ControllerHelper.getHandler(requestMethod,requestPath);
            if(handler!=null){
                //获取请求的controller
                Class<?> controllerClass = handler.getControllerClass();
                Object controllerBean = BeanHelper.getBean(controllerClass);
                Param param ;
                if(UploadHelper.isMultipart(request)){
                    param = UploadHelper.createParam(request);
                }else {
                    param = RequestHelper.createParm(request);
                }

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
                    handleViewResult((View)result,request,response);
                }else if(result instanceof Data){
                    //返回json数据
                    handleDataResult((Data)result,request,response);
                }
            }
        }finally {
            ServletHelper.destory();
        }

    }

    private void  handleViewResult(View view,HttpServletRequest request,HttpServletResponse response)throws IOException,ServletException{
        String path = view.getPath();
        if(StringUtils.isNotEmpty(path)){
            if(path.startsWith("/")){
                response.sendRedirect(request.getContextPath()+path);
            }else {
                //返回数据渲染后的jsp页面
                Map<String,Object> model = view.getModel();
                for (Map.Entry<String,Object> entry : model.entrySet()){
                    request.setAttribute(entry.getKey(),entry.getValue());
                }
                request.getRequestDispatcher(ConfigConstant.APP_JSP_PATH + path).forward(request,response);
            }
        }
    }

    private void  handleDataResult(Data data,HttpServletRequest request,HttpServletResponse response)throws IOException,ServletException{
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
