package com.lsl.dynamic.inject.before;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Constructor;

/**
 * @Description: TODO
 * @Author: lsl
 * @Data:2023-01-04 15:08
 * @Projectname: Kathryn
 * @Filename: CustomFactoryBean
 */

public class BeforeFactoryBean implements FactoryBean<Object> {

    /** 这个属性就是任意用户定义sql的 mappr接口 */
    Class<?> templateClass;

    /**  指定存入容器的方式 为 用户定义CustomSqlSession自己实现动态代理 获取 */
    @Override
    public Object getObject() throws Exception {
        // 1. 模板写死的默认会有一个
        Constructor<?>[] constructors = templateClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            return constructor.newInstance();
        }
        return null;
    }

    /** 必须重写的方法指定注入的类型 */
    @Override
    public Class<?> getObjectType() {
        return templateClass;
    }

    /** 通过 set方式 将用户定义sql的mapper set进当前 */
    public void setTemplateClass(Class<?> templateClass) {
        this.templateClass = templateClass;
    }

    /** get方法获取 用户定义sql的mapper */
    public Class<?> getTemplateClass() {
        return templateClass;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
