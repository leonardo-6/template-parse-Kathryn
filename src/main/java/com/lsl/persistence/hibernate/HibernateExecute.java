package com.lsl.persistence.hibernate;

import com.lsl.annotation.HibernateSession;
import com.lsl.persistence.hibernate.base.AbstractHibernate;
import com.lsl.utils.rename.HumpNameConverter;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 关于 Hibernate 执行查询的封装
 * @Description: 当前只有几种测试类型
 * @Author: lsl
 * @Data:2023-01-12 14:02
 * @Projectname: Kathryn
 * @Filename: HibernateExcute
 */
@Repository
@HibernateSession
public class HibernateExecute extends AbstractHibernate{

    private Session session;

    @Override
    public Object select(Object o) {

        return null;
    }

    @Override
    public List<Map<String,Object>> selectList(String sql) {

        List list = session.createQuery(sql).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

        List resultList = HumpNameConverter.convertListMapKey(list);

        return resultList;
    }


}
