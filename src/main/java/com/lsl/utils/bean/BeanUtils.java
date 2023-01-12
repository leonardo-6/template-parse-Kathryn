package com.lsl.utils.bean;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 解决某些Bean注入的问题
 * describe bean 注入工具
 * @author LuShuL 12304
 * @date 2022/5/3 1:08
 * @version V1.0
 */
@Component
public class BeanUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    /****
     * setApplicationContext 方法
     * @param applicationContext applicationContext
     * @throws BeansException 基础异常
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (BeanUtils.applicationContext == null) {
            BeanUtils.applicationContext = applicationContext;
        }

    }

    /****
     * 获取applicationContext
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /****
     * 通过name获取 Bean.
     * @param name
     * @return
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);

    }

    /***
     * 通过class获取Bean.
     * @param clazz 类
     * @param <T> 泛型
     * @return 泛型类
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /****
     * 通过name,以及Clazz返回指定的Bean
     * @param name bean名字
     * @param clazz 类型
     * @param <T> 泛型
     * @return 泛型类
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }
}
