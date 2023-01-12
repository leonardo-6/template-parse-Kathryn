package com.lsl.dynamic.compiler;

import com.lsl.exception.MyFreemarkerException;

import java.lang.reflect.InvocationTargetException;

/**
 * 动态编译类 包含动态注入
 * @Description: 主要的工作时动态编译
 * @Author: lsl
 * @Data:2023-01-04 13:35
 * @Projectname: Kathryn
 * @Filename: DynamicBeanHandler
 */
public interface DynamicCompileHandler {

    /**
     *
     * @param javaCode java代码
     * @param beanName beanName（同时也是classname），注意:beanName必须与javaCode中的className保持一致
     * @throws Exception
     */
    void loadBean(String resourceName,String javaCode, String beanName,String packageSuffix) throws Exception;

    /**
     * 无参方法执行
     * @param beanName
     * @param methodName
     * @return
     */
    Object invoke(String beanName, String methodName) throws MyFreemarkerException;

    /**
     * 有参方法执行
     * @param beanName
     * @param methodName
     * @param args  demo : new Object[]{value}
     * @param parameterTypes demo : new Class[]{Object.class}
     * @return
     */
    Object invoke(String beanName, String methodName,Object[] args, Class<?>[] parameterTypes) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, MyFreemarkerException;

}


