package com.lsl.observer.abstraction;

import java.util.Map;

/**
 * 观察者抽象类
 * @Description: 观察者抽象类
 * @Author: LSL
 * @Data:2022-11-30 17:25
 * @Projectname: Kathryn
 * @Filename: Observer
 */
public abstract class Observer {
    /***
     * 观察收到的信息
     * @param messages 消息
     */
    public abstract void  watch(Map messages);
}
