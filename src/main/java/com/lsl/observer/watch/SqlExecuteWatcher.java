package com.lsl.observer.watch;

import com.lsl.observer.abstraction.Observer;
import com.lsl.persistence.hibernate.HibernateExecute;
import com.lsl.utils.check.CheckValueUtil;
import com.lsl.common.enums.CommonEnum;
import com.lsl.common.enums.WatcherEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;


/**
 * 观察者 ---- sql执行的观察者
 * @Description: sql语句委托
 * @Author: LSL
 * @Data:2022-11-30 17:27
 * @Projectname: Kathryn
 * @Filename: DesensitizeWatcher
 */
@Component
@Slf4j
@Data
public class SqlExecuteWatcher extends Observer {

    @Autowired
    private HibernateExecute hibernateExecute;

    private Object result;


    /***
     * 0. watch 方法
     * -- 校验通知信息是否为 当前监听者职责
     * @param messages 消息
     */
    @Override
    public void watch(Map messages) {

        if (!CheckValueUtil.notBlank(messages)) {
            return;
        }

        for (Object key : messages.keySet()) {
            if (WatcherEnum.SQL_EXECUTE.equals(key)) {
                log.info("====== 执行 sql语句   ======");
                executeSql((Map<CommonEnum, Object>) messages.get(key));
            }
        }
    }

    /****
     * 1. 执行sql处理 主方法
     * @param messages 规则信息
     */
    void  executeSql(Map<CommonEnum,Object>  messages) {

        // 1. 预先校验
        if (!CheckValueUtil.notBlank(messages)) {
            return ;
        }


        // 2. 获取首要信息
        String  sql   =   (String) messages.get(CommonEnum.SQL);
        Method method = (Method) messages.get(CommonEnum.METHOD);

        // TODO 根据方法返回类型 校验对应的sql处理

        // 4. 执行sql操作
        List<Map<String, Object>> list = hibernateExecute.selectList(sql);

        setResult(list);
    }





}
