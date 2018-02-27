package org.smart4j.framework.helper;

import org.smart4j.framework.constant.ConfigConstant;
import org.smart4j.framework.utils.PropUtil;

import java.util.Properties;

/**
 * 文件属性加载
 * Created by Administrator on 2018/2/23.
 */
public class ConfigHelper {

    private static final Properties CONFIG_PROPS = PropUtil.loadProperties(ConfigConstant.CONFIG_FILE);

    /**
     * 获取jdbc驱动
     * @return
     */
    public static String getJdbcDriver(){
        return PropUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_DRIVER,null);
    }

    /**
     * 获取jdbcURL
     * @return
     */
    public static String getJdbcURL(){
        return PropUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_URL,null);
    }

    public static String getJdbcUserName(){
        return PropUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_USERNAME,null);
    }

    public static String getJdbcPassword(){
        return PropUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_PASSWORD,null);
    }

    public static String getBasePackage(){
        return PropUtil.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE,null);
    }

    public static String getJspPath(){
        return PropUtil.getString(CONFIG_PROPS,ConfigConstant.APP_JSP_PATH,"/WEB-INF/view/");
    }

    public static String getAssetPath(){
        return PropUtil.getString(CONFIG_PROPS,ConfigConstant.APP_ASSET_PATH,"/asset/");
    }
    public static int getAppUploadLimit(){
        return PropUtil.getInt(CONFIG_PROPS,ConfigConstant.APP_UPLOAD_LIMIT,10);
    }
}
