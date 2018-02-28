package org.smart4j.framework.annotation;

import java.lang.annotation.*;

/**
 * 定义切面注解
 * Created by Administrator on 2018/2/25.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {

    /**
     * 目标类
     * @return
     */
    Class<? extends Annotation> value();
}
