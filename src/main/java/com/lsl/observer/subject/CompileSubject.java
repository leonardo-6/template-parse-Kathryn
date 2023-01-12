package com.lsl.observer.subject;

import com.lsl.observer.abstraction.Observer;
import com.lsl.observer.abstraction.Subject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 1. 编译主题
 * @Description: 编译
 * @Author: lushul 12314
 * @Data:2022-11-30 17:30
 * @Projectname: Kathryn
 * @Filename: DensensitizeSubject
 */
@Component
public class CompileSubject extends Subject {

    /** 储存订阅 脱敏 用户 **/
    private List<Observer> compileList = new ArrayList<>();

    @Override
    public void attach(Observer observer) {
        compileList.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        compileList.remove(observer);
    }

    @Override
    public void notify(Map messages) {
        for (Observer observer : compileList) {
            observer.watch(messages);
        }
    }
}
