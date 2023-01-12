package com.lsl.observer.abstraction;

import java.util.Map;

/**
 * 主题抽象类
 * @Description: 主题的抽象方法
 * @Author: LSL
 * @Data:2022-11-30 17:28
 * @Projectname: Kathryn
 * @Filename: Subject
 */
public abstract class Subject {

    /*** 增加订阅者 */
    public abstract void attach(Observer observer);

    /** 删除订阅者 */
    public abstract void detach(Observer observer);

    /** 通知订阅者更新消息*/
    public abstract void notify(Map messages);
}
