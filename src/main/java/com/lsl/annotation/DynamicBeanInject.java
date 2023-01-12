package com.lsl.annotation;

import com.lsl.dynamic.inject.before.BeforeBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 动态Bean 注入的
 * @Description: 动态Bean注入
 * @Author: lsl
 * @Data:2023-01-09 13:53
 * @Projectname: Kathryn
 * @Filename: DynamicBeanInject
 */
// 封装一个自定义注解 供配置类引用
@Retention(RetentionPolicy.RUNTIME)
@Import(BeforeBeanDefinitionRegistrar.class)
public @interface DynamicBeanInject {
    String value() default "";
}
