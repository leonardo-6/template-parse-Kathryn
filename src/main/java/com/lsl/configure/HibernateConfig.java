package com.lsl.configure;

import com.lsl.observer.subject.SqlExecuteSubject;
import com.lsl.observer.watch.SqlExecuteWatcher;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * Hibernate的配置
 * @Description: 默认的连接数据库 方式
 * @Author: lsl
 * @Data:2022-12-06 18:25
 * @Projectname: Kathryn
 * @Filename: HibernateConfiguration
 */
@Configuration
@Slf4j
public class HibernateConfig {

    @Autowired
    private SqlExecuteSubject sqlExecuteSubject;

    @Autowired
    private SqlExecuteWatcher sqlExecuteWatcher;

    private SessionFactory factory;

    @Bean
    public SessionFactory initHibernate(){
        // 获取加载配置管理类并默认加载hibernate.cfg.xml文件
        org.hibernate.cfg.Configuration cfg = new org.hibernate.cfg.Configuration().configure();

        // 得到Session工厂对象
        return factory = cfg.buildSessionFactory();

    }

    @Bean(destroyMethod= "closeHibernate")
    public HibernateConfig hibernateConnect(){
        // 启动hibernate的配置 加入 hibernate的监听
        sqlExecuteSubject.attach(sqlExecuteWatcher);
        return  this;
    }

    public void closeHibernate() throws IOException {
        // 关闭 factory
        factory.close();
        log.info(" ====== hibernate的SessionFactory关闭");
    }
}
