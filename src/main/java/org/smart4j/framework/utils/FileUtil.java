package org.smart4j.framework.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * 文件操作工具类
 * Created by Administrator on 2018/2/27.
 */
public final class FileUtil {

    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 获取真实文件名(自动去掉文件路径)
     * @param fileName
     * @return
     */
    public static String getRealFileName(String fileName){
        return FilenameUtils.getName(fileName);
    }

    /**
     * 创建文件
     * @param path
     * @return
     */
    public static File createFile(String path){
        File file = null;
        try {
            file = new File(path);
            File parentDir = file.getParentFile();
            if(!parentDir.exists()){
                FileUtils.forceMkdir(parentDir);
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("create file failure " + e );
        }
        return file;
    }

}
