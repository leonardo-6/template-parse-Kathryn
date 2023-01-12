package com.lsl.observer.subject;

import com.lsl.observer.abstraction.Observer;
import com.lsl.observer.abstraction.Subject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2. 执行sql主题
 * @Description: sql执行
 * @Author: lushul 12314
 * @Data:2022-11-30 17:30
 * @Projectname: Kathryn
 * @Filename: DensensitizeSubject
 */
@Component
public class SqlExecuteSubject extends Subject {

    /** 储存订阅 脱敏 用户 **/
    private List<Observer> sqlExecuteList = new ArrayList<>();

    @Override
    public void attach(Observer observer) {
        sqlExecuteList.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        sqlExecuteList.remove(observer);
    }

    @Override
    public void notify(Map messages) {
        for (Observer observer : sqlExecuteList) {
            observer.watch(messages);
        }
    }
}
