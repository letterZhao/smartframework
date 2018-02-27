package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Transaction;
import org.smart4j.framework.utils.DBUtill;

import java.lang.reflect.Method;

/**
 * 事务代理类
 * Created by Administrator on 2018/2/27.
 */
public class TransactionProxy implements Proxy {

    private static Logger logger = LoggerFactory.getLogger(TransactionProxy.class);

    //本地线程变量
    private static final ThreadLocal<Boolean> FLAG_HODLER = new ThreadLocal<Boolean>(){
        protected Boolean initialValue() {
            return false;
        }
    };

    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        boolean flag = FLAG_HODLER.get();
        Method method = proxyChain.getTargetMethod();
        if(!flag && method.isAnnotationPresent(Transaction.class)){
            FLAG_HODLER.set(true);
            try {
                DBUtill.beginTransaction();
                logger.debug("begin transaction");
                result = proxyChain.doProxyChain();
                DBUtill.commitTransaction();
                logger.debug("commit transaction");
            }catch (Exception e){
                e.printStackTrace();
                DBUtill.rollbackTransaction();
            }finally {
                FLAG_HODLER.remove();
            }
        }else {
            result = proxyChain.doProxyChain();
        }
        return result;
    }


}
