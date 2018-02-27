package org.smart4j.framework.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.bean.FormParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.utils.CodeUtil;
import org.smart4j.framework.utils.StreamUtil;
import org.smart4j.framework.utils.StringUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 请求助手类
 * Created by Administrator on 2018/2/27.
 */
public final class RequestHelper {

    /**
     * 创建请求对象
     * @param request
     * @return
     * @throws IOException
     */
    public static Param createParm(HttpServletRequest request) throws IOException{
        List<FormParam> formParamList = new ArrayList<FormParam>();
        formParamList.addAll(parserParamterNames(request));
        formParamList.addAll(parseInputStream(request));
        return new Param(formParamList);
    }

    public static List<FormParam> parserParamterNames(HttpServletRequest request){
        List<FormParam> formParamList = new ArrayList<FormParam>();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()){
            String filedName = paramNames.nextElement();
            String[] filedValues = request.getParameterValues(filedName);
            if(ArrayUtils.isNotEmpty(filedValues)){
                Object filedValue;
                if(filedValues.length == 1){
                    filedValue = filedValues[0];
                }else {
                    StringBuilder sb = new StringBuilder("");
                   for(int i=0;i<filedValues.length;i++){
                       sb.append(filedValues[i]);
                       if(i!=filedValues.length-1){
                           sb.append(StringUtil.SEPAROTOR);
                       }
                   }
                    filedValue = sb.toString();
                }
                formParamList.add(new FormParam(filedName,filedValue));
            }
        }
        return formParamList;
    }


    private static List<FormParam> parseInputStream(HttpServletRequest request) throws IOException{
        List<FormParam> formParamList = new ArrayList<FormParam>();
        String body = CodeUtil.decode(StreamUtil.getString(request.getInputStream()));
        if(StringUtils.isNotEmpty(body)){
            String[] kvs =StringUtils.split(body,"&");
            if(ArrayUtils.isNotEmpty(kvs)){
                for (String kv: kvs) {
                   String[] array = StringUtils.split(kv,"=");
                   if(ArrayUtils.isNotEmpty(array) && array.length == 2){
                       String fieldName = array[0];
                       String fieldValue = array[1];
                       formParamList.add(new FormParam(fieldName,fieldValue));
                   }
                }
            }
        }
        return formParamList;
    }

}
