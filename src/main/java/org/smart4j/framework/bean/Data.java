package org.smart4j.framework.bean;

/**
 * Created by Administrator on 2018/2/23.
 */
public class Data {

    private Object model;//模型数据

    public Data() {
    }

    public Data(Object model) {
        this.model = model;
    }

    public Object getModel() {
        return model;
    }
}
