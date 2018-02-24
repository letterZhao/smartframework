package org.smart4j.framework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * pojo对象与json类型转化
 * Created by Administrator on 2018/2/23.
 */
public final class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 将pojo转化为json
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> String objToJson(T obj){
        String json = null;
        try{
            json = objectMapper.writeValueAsString(obj);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("convert object to json failure",e);
        }
        return json;
    }

    /**
     * 将json对象转化为object
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    public static <T> Object jsonToObj(String json,Class<T> type){
        Object obj = null;
        try {
            obj = objectMapper.readValue(json,type);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("convert json to object failure",e);
        }
        return obj;
    }


}
