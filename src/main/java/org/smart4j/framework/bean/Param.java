package org.smart4j.framework.bean;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart4j.framework.utils.CastUtil;
import org.smart4j.framework.utils.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请求参数对象
 * 兼容表单参数和文件参数
 * Created by Administrator on 2018/2/23.
 */
public class Param {

    private List<FormParam> formParamList;
    private List<FileParam> fileParamList;

    public Param(List<FormParam> formParamList) {
        this.formParamList = formParamList;
    }

    public Param(List<FormParam> formParamList, List<FileParam> fileParamList) {
        this.formParamList = formParamList;
        this.fileParamList = fileParamList;
    }


    /**
     * 配置form表单映射
     * @return
     */
    public Map<String ,Object> getFieldMap(){
        Map<String,Object> fieldMap = new HashMap<String, Object>();
        if(CollectionUtils.isEmpty(formParamList)){
            for (FormParam formParam: formParamList) {
                String paramName = formParam.getParamName();
                Object paramValue = formParam.getParamValue();
                if(fieldMap.containsKey(paramName)){
                    paramValue = fieldMap.get(paramName)+ StringUtil.SEPAROTOR + paramValue;
                }
                fieldMap.put(paramName,paramValue);
            }
        }
        return fieldMap;
    }


    /**
     * 配置文件上传参数
     * @return
     */
    public  Map<String,List<FileParam>> getFileMap(){
        Map<String,List<FileParam>> fileMap = new HashMap<String, List<FileParam>>();
        if(CollectionUtils.isNotEmpty(fileParamList)){
            for (FileParam param : fileParamList) {
                String fieldName = param.getFieldName();
                List<FileParam> fileParamList;
                if(fileMap.containsKey(fieldName)){
                    fileParamList = fileMap.get(fieldName);
                }else {
                    fileParamList = new ArrayList<FileParam>();
                }
                fileMap.put(fieldName,fileParamList);
            }
        }
        return fileMap;
    }


    /**
     * 获取所有上传文件
     */
    public  List<FileParam> getFileList(String fieldName){
        return getFileMap().get(fieldName);
    }

    /**
     * 获取唯一上传文件
     * @param fieldName
     * @return
     */
    public FileParam getFile(String fieldName){
        List<FileParam> paramList = getFileList(fieldName);
        if(CollectionUtils.isNotEmpty(paramList) && paramList.size() == 1 ){
            return paramList.get(0);
        }
        return null;
    }


    /**
     * 获取long类型参数
     * @param name
     * @return
     */
    public long getLong(String name){
        return CastUtil.castLong(getFieldMap().get(name));
    }

    /**
     * 获取double类型参数
     * @param name
     * @return
     */
    public double getDouble(String name){
        return CastUtil.castDouble(getFieldMap().get(name));
    }

    /**
     * 获取int类型参数
     * @param name
     * @return
     */
    public int getInt(String name){
        return CastUtil.castInt(getFieldMap().get(name));
    }

    /**
     * 获取String类型参数
     * @param name
     * @return
     */
    public String getString(String name){
        return CastUtil.castString(getFieldMap().get(name));
    }

    /**
     * 验证参数是否为空
     * @return
     */
    public boolean isEmpty(){
        return CollectionUtils.isEmpty(fileParamList) && CollectionUtils.isEmpty(formParamList);
    }
}
