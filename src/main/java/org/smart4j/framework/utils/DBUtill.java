package org.smart4j.framework.utils;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Administrator on 2018/2/9.
 */
public class DBUtill {

    private static Logger logger = LoggerFactory.getLogger(DBUtill.class);
    private static final ThreadLocal<Connection> CONNECTION_HOLDER ;//threadLoacal线程隔离的容器
    private static final String DRIVER ;
    private static final String URL ;
    private static final String USERNAME ;
    private static final String PASSWORD ;
    private static final QueryRunner queryRunner = new QueryRunner();
    //使用dbcp2数据库连接池获取连接
    private static BasicDataSource basicDataSource ;

    static {
        CONNECTION_HOLDER  = new ThreadLocal<Connection>();
        basicDataSource = new BasicDataSource();

        Properties prop = PropUtil.loadProperties("jdbc.properties");
        DRIVER = prop.getProperty("jdbc.driver");
        URL = prop.getProperty("jdbc.url");
        USERNAME = prop.getProperty("jdbc.username");
        PASSWORD = prop.getProperty("jdbc.password");

        basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(DRIVER);
        basicDataSource.setUrl(URL);
        basicDataSource.setUsername(USERNAME);
        basicDataSource.setPassword(PASSWORD);
    }

    /**
     * 获取数据库连接
     * @return
     */
    public static Connection getConn(){
        Connection conn ;
        conn = CONNECTION_HOLDER.get();
        if(conn == null ){
            try {
                conn = basicDataSource.getConnection();
            }catch (Exception e){
                logger.error("get connection error"+e);
                e.printStackTrace();
            }finally {
                CONNECTION_HOLDER.set(conn);
            }
        }
        return conn;
    }

    /**
     * 开始事务
     */
    public static void beginTransaction(){
        Connection connection = getConn();
        if(connection!=null){
            try {
                connection.setAutoCommit(false);
            }catch (Exception e){
                logger.error("begin transaction failure" + e);
                throw new RuntimeException();
            }finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
    }
    /**
     * 提交事务
     */
    public static void commitTransaction(){
        Connection connection = getConn();
        if(connection!=null){
            try {
                connection.commit();
                connection.close();
            }catch (Exception e){
                logger.error("commit transaction failure" + e);
                throw new RuntimeException();
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction(){
        Connection connection = getConn();
        if(connection!=null){
            try {
                connection.rollback();
                connection.close();
            }catch (Exception e){
                logger.error("rollback transaction failure" + e);
                throw new RuntimeException();
            }finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }
    /**
     * 关闭连接
     * @param connection
     */
//    public static void closeConn(Connection connection){
//        if(connection!=null){
//            try {
//                connection.close();
//            }catch (Exception e){
//                e.printStackTrace();
//                throw new RuntimeException();
//            }finally {
//                CONNECTION_HOLDER.remove();
//            }
//        }
//    }
    /**
     * 查询所有实体类
     * @param entity
     * @param sql
     * @param <T>
     * @return
     */
    public static <T> List<T> queryEntityList(Class<T> entity , String sql){
        List<T> entityList = null ;
        try {
            entityList = queryRunner.query(getConn(),sql,new BeanListHandler<T>(entity));
        }catch (Exception e){
            e.printStackTrace();
        }
        return entityList;
    }

    /**
     * 查询单个实体类
     * @param entityClass
     * @param sql
     * @param <T>
     * @return
     */
    public static <T> T  queryEntity(Class<T> entityClass , String sql){
        T entity = null ;
        try {
            entity = queryRunner.query(getConn(),sql,new BeanHandler<T>(entityClass));
        }catch (Exception e){
            logger.error("getEntityList error "+e );
            throw new RuntimeException();
        }
        return entity;
    }

    /**
     *执行一个动态sql查询语句
     * @param sql
     * @param params
     * @return
     */
    public static List<Map<String,Object>> excuteQuery(String sql , Object... params){
        List<Map<String,Object>> mapList = null;
        try {
            mapList = queryRunner.query(getConn(),sql,new MapListHandler(),params);
        }catch (Exception e){
            logger.error("excuteQuery error" + e );
            throw new RuntimeException();
        }
        return mapList;
    }


    /**
     * 执行更新操作(insert update delete)
     * @param sql
     * @param params
     * @return
     */
    public static int excuteupdate(String sql ,Object... params ){
        int rows = 0;
        try {
            rows = queryRunner.update(sql,params);
        }catch (Exception e){
            logger.error("updateEntity error "+e);
            throw new RuntimeException();
        }
        return rows;
    }


    /**
     * 执行插入sql
     * @param entityClass
     * @param filedMap
     * @param <T>
     * @return
     */
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String,Object> filedMap){
        if(MapUtils.isEmpty(filedMap)){
            logger.error("filedMap can not empty ");
            return false;
        }
        String sql = "insert into "+getTableName(entityClass);
        StringBuffer fileds = new StringBuffer("(");
        StringBuffer values = new StringBuffer("(");
        for (String key : filedMap.keySet()){
            fileds.append(key+",");
            values.append("?,");
        }
        fileds = fileds.replace(fileds.lastIndexOf(","),fileds.length(),")");
        values = fileds.replace(values.lastIndexOf(","),values.length(),")");
        sql += fileds +"values" +values;
        Object[] params = filedMap.values().toArray();
        return excuteupdate(sql,params) == 1 ;
    }

    /**
     * 更新实体
     * @param entityClass
     * @param filedMap
     * @param id
     * @param <T>
     * @return
     */
    public static <T> boolean updateEntity(Class<T> entityClass , Map<String,Object> filedMap, long id ){
        if(MapUtils.isEmpty(filedMap)){
            logger.error("filedMap cannot null ");
            return false;
        }
        String sql = "update "+ getTableName(entityClass);
        StringBuffer cloums = new StringBuffer("(");
        List<Object> paramsList = new LinkedList<Object>();
        for (String key : filedMap.keySet()){
            cloums.append(key+"=?,");
        }
        sql += cloums.substring(0,cloums.lastIndexOf(","))+" where id = ? ";
        paramsList.addAll(filedMap.values());
        paramsList.add(id);
        return excuteupdate(sql,paramsList) == 1 ;
    }


    /**
     * 删除实体
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    public static   <T> boolean deleteEntity(Class<T> entityClass,long id){
        String sql = "delete from "+ getTableName(entityClass) + " where id = ? ";
        return excuteupdate(sql,id) == 1;
    }

    /**
     * 获取当前执行类名
     * @param entityClass
     * @param <T>
     * @return
     */
    private static  <T> String getTableName(Class<T> entityClass){
        String tableName = entityClass.getSimpleName();
        return tableName;
    }

    /**
     * 执行sql文件
     * @param path
     */
    public static void excuteSqlFile(String path){
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String sql ;
            if((sql =reader.readLine())!=null){
                excuteupdate(sql);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
