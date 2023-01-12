package com.lsl.annotation;

import java.lang.annotation.*;

/**
 * 用来横切 执行Hibernate 的sesssion处理
 * @Description: Hibernate的sesssion 前置后置处理
 * @Author: lsl
 * @Data:2023-01-11 17:41
 * @Projectname: Kathryn
 * @Filename: HibernateSession
 */
@Documented   //该注解表示支持javaDoc文档导出
@Retention(RetentionPolicy.RUNTIME) //该注解表示生命周期
@Target({ElementType.METHOD,ElementType.TYPE})  //该注解表示自定义的注解可以使用的对象
public @interface HibernateSession {
    String value() default "";
}
