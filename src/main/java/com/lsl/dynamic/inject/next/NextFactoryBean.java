package com.lsl.dynamic.inject.next;

import com.lsl.dynamic.proxy.jdk.NextSession;
import org.springframework.beans.factory.FactoryBean;

/**
 * 注入容器时创建的实例
 * @Description: 此处为动态代理所耦合
 * @Author: lsl
 * @Data:2023-01-10 16:26
 * @Projectname: Kathryn
 * @Filename: MyFactoryBean
 */
public class NextFactoryBean<T> implements FactoryBean<T> {

    private Class<T> mapper;

    public NextFactoryBean(Class<T> mapper) {
        this.mapper = mapper;
    }

    @Override
    public T getObject() throws Exception {
        return NextSession.getMapper(this.mapper);
    }

    @Override
    public Class<T> getObjectType() {
        return this.mapper;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
