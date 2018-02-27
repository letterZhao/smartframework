package org.smart4j.framework.bean;

/**
 * 表单参数
 * Created by Administrator on 2018/2/27.
 */
public class FormParam {

    private String paramName;
    private Object paramValue;

    public FormParam(String paramName, Object paramValue) {
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public Object getParamValue() {
        return paramValue;
    }

    public void setParamValue(Object paramValue) {
        this.paramValue = paramValue;
    }
}
