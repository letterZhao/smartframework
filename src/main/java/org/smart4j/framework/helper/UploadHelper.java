package org.smart4j.framework.helper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.bean.FileParam;
import org.smart4j.framework.bean.FormParam;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.utils.FileUtil;
import org.smart4j.framework.utils.StreamUtil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件上传助手类
 * Created by Administrator on 2018/2/27.
 */
public class UploadHelper {

    private static Logger logger = LoggerFactory.getLogger(UploadHelper.class);

    //apache commons 提供的serlvet文件上传类
    private static ServletFileUpload servletFileUpload;

    /**
     * 初始化
     * @param servletContext
     */
    public static void init(ServletContext servletContext){
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD,repository));
        int uploadLimit = ConfigHelper.getAppUploadLimit();//获取文件上传限制
        if(uploadLimit!=0){
            servletFileUpload.setFileSizeMax(uploadLimit*1024*1024);
        }
    }

    /**
     * 判断请求是否multipart
     * @param request
     * @return
     */
    public static boolean isMultipart(HttpServletRequest request){
        return servletFileUpload.isMultipartContent(request);
    }

    /**
     * 创建请求对象param
     * @param request
     * @return
     * @throws IOException
     */
    public static Param createParam(HttpServletRequest request) throws IOException{
        List<FormParam> formParamList = new ArrayList<FormParam>();
        List<FileParam> fileParamList = new ArrayList<FileParam>();
        try {
            //获取文件上传请求参数集合
            Map<String,List<FileItem>> fileItemList = servletFileUpload.parseParameterMap(request);
            if(MapUtils.isNotEmpty(fileItemList)){
                //遍历参数集合
                for (Map.Entry<String,List<FileItem>> fileItemListEntry: fileItemList.entrySet()) {
                    String fieldName = fileItemListEntry.getKey();
                    List<FileItem> fileItemList1 = fileItemListEntry.getValue();
                    if(CollectionUtils.isNotEmpty(fileItemList1)){
                        for (FileItem fileItem:fileItemList1) {
                            if(fileItem.isFormField()){
                                String fieldValue = fileItem.getString("UTF-8");
                                formParamList.add(new FormParam(fieldName,fieldValue));
                            }else {
                                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(),"UTF-8"));
                                if(StringUtils.isNotEmpty(fileName)){
                                    long fileSize = fileItem.getSize();
                                    String contentType = fileItem.getContentType();
                                    InputStream inputStream = fileItem.getInputStream();
                                    fileParamList.add(new FileParam(fieldName,fileName,fileSize,contentType,inputStream));
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error("upload file failure " + e );
            throw new RuntimeException();
        }
        return new Param(formParamList,fileParamList);
    }


    /**
     * 文件上传
     * @param basePath
     * @param fileParam
     */
    public static void uploadFile(String basePath,FileParam fileParam){
        try {
            if(fileParam!=null){
                String filePath = basePath + fileParam.getFileName();
                FileUtil.createFile(filePath);
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamUtil.copyStream(inputStream,outputStream);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 批量上传文件
     * @param basePath
     * @param paramList
     */
    public static void batchUploadFile(String basePath,List<FileParam> paramList){
        try {
            if(CollectionUtils.isNotEmpty(paramList)){
                for (FileParam fileParam: paramList) {
                    uploadFile(basePath,fileParam);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
