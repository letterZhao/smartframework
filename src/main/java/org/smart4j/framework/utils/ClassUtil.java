package org.smart4j.framework.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类操作工具
 * Created by Administrator on 2018/2/23.
 */
public class ClassUtil {

    private final Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * 获取当前线程类加载器
     * @return
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     *
     * 加载类
     * @param className
     * @param isInit  是否初始化类(即类的静态代码)
     * @return
     */
    public static Class<?> loadClass(String className,boolean isInit){
        Class<?> clazz ;
        try {
            clazz = Class.forName(className,isInit,getClassLoader());
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("类加载异常");
        }
        return clazz;
    }


    /**
     * 加载指定包名下的所有类
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet = new HashSet<Class<?>>();
        try{
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".","/"));
            while (urls.hasMoreElements()){
                URL url = urls.nextElement();
                if( url != null ){
                    String protocol = url.getProtocol();
                    if(protocol.equals("file")){
                        String packagePath = url.getPath().replaceAll("%20","");
                        addClass(classSet,packagePath,packageName);
                    }else if(protocol.equals("jar")){
                        //处理jar类型文件
                        JarURLConnection jarURLConnection = (JarURLConnection)url.openConnection();
                        if(jarURLConnection!=null){
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if(jarFile!=null){
                                Enumeration<JarEntry> jarEntries = jarFile.entries();
                                while (jarEntries.hasMoreElements()){
                                   JarEntry jarEntry = jarEntries.nextElement();
                                   String jarEntryName = jarEntry.getName();
                                   if(jarEntryName.endsWith(".class")){
                                       String className = jarEntryName.substring(0,jarEntryName.lastIndexOf("."))
                                               .replaceAll(".","/");
                                       doAddClass(classSet,className);
                                   }
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return classSet;
    }

    /**
     * 添加class
     * @param classSet
     * @param packagePath
     * @param packageName
     */
    public static void  addClass(Set<Class<?>> classSet,String packagePath,String packageName){
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class") || file.isDirectory());
            }
        });
        for (File file: files ){
            String fileName = file.getName();
            if(file.isFile()){//如果是文件
                String className = fileName.substring(0,fileName.lastIndexOf("."));
                if(StringUtils.isNotEmpty(packageName)){
                    className = packageName + "." + className;
                }
                doAddClass(classSet,className);
            }else {//如果是文件夹
                String subPackagePath = fileName;
                if(StringUtils.isNotEmpty(packagePath)){
                    subPackagePath = packagePath+"/"+subPackagePath;
                }
                String subPackageName = fileName;
                if(StringUtils.isNotEmpty(subPackageName)){
                    subPackageName = subPackageName+"/"+subPackageName;
                }
                addClass(classSet,subPackagePath,subPackageName);
            }
        }
    }

    public static void doAddClass(Set<Class<?>> classSet,String className){
        Class<?> clazz = loadClass(className,false);
        classSet.add(clazz);
    }


}
