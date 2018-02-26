package org.smart4j.framework.proxy;

/**
 * Created by Administrator on 2018/2/25.
 */
public interface Proxy {

    /**
     * 执行代理链
     * @param proxyChain
     * @return
     * @throws Throwable
     */
    public Object doProxy(ProxyChain proxyChain) throws Throwable;

}
