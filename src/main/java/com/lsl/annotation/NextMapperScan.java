package com.lsl.annotation;

import com.lsl.dynamic.inject.next.NextImportBeanDefinitionRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 1. 动态注入 时扫秒
 * @Description: 动态扫描 接口
 * @Author: lsl
 * @Data:2023-01-10 16:30
 * @Projectname: Kathryn
 * @Filename: MyMapperScan
 */
@Retention(RetentionPolicy.RUNTIME)
@Import(NextImportBeanDefinitionRegistrar.class)
public @interface NextMapperScan {
    String[] value() default {};
}
