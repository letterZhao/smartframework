package org.smart4j.framework.bean;

import org.smart4j.framework.utils.CastUtil;

import java.util.Map;

/**
 * Created by Administrator on 2018/2/23.
 */
public class Param {

    private Map<String,Object> paramMap;

    public Param() {
    }

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    /**
     * 获取long类型参数
     * @param name
     * @return
     */
    public long getLong(String name){
        return CastUtil.castLong(paramMap.get(name));
    }

    /**
     * 获取所有字段信息
     * @return
     */
    public Map<String,Object> getMap(){
        return paramMap;
    }

}
