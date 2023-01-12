package com.lsl.persistence.hibernate.base;

import com.lsl.annotation.HibernateSession;
import org.hibernate.Session;

import java.util.Map;

/**
 * 基础Hibernate类
 * @Description: 增删改查的抽象
 * @Author: lsl
 * @Data:2023-01-12 13:34
 * @Projectname: Kathryn
 * @Filename: AbstractHibernate
 */
@HibernateSession
public abstract class AbstractHibernate {

    private Session session;

    /*****
     * 1. 增
     * @param o 对象
     */
    public void save(Object o){
        session.save(o);
    }

    /****
     * 2. 删
     * @param o 对象
     */
    public void delete(Object o){
        session.delete(o);
    }

    /****
     * 3. 改
     * @param o 对象
     */
    public void update(Object o){
        session.update(o);
    }

    /****
     * 4. 保存或修改
     * @param o 对象
     */
    public void  saveOrUpdate(Object o){
        session.saveOrUpdate(o);
    }

    /****
     * 5. 保存或修改
     * @param o 对象
     */
    public void merge(Object o){
        session.merge(o);
    }

    /*****
     * 6. 查
     * @param o
     * @return
     */
    protected abstract Object select(Object o);

    protected abstract Object selectList(String sql);

}
