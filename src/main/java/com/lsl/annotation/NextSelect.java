package com.lsl.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 根据sql 语句 值直接执行
 * @Description: 暂时 不走注解
 * @Author: lsl
 * @Data:2023-01-09 13:53
 * @Projectname: Kathryn
 * @Filename: DynamicBeanInject
 */
// 封装一个自定义注解 供配置类引用
@Retention(RetentionPolicy.RUNTIME)
public @interface NextSelect {
    String value() default "";
}
