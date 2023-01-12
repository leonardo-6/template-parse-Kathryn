package com.lsl.observer.watch;

import com.lsl.observer.abstraction.Observer;
import com.lsl.utils.check.CheckValueUtil;
import com.lsl.common.enums.WatcherEnum;
import com.lsl.dynamic.compiler.impl.DynamicCompileHandlerImpl;
import com.lsl.configure.freemarker.parameters.FreemarkerParametersConfigure;
import com.lsl.utils.strings.MyStrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


/**
 * 观察者 ---- 编译问题的观察
 * @Description: 1. 编译问题的观察
 * @Author: LSL
 * @Data:2022-11-30 17:27
 * @Projectname: Kathryn
 * @Filename: DesensitizeWatcher
 */
@Component
@Slf4j
public class CompileExecuteWatcher extends Observer {

    /*** 1. 动态编译处理类 */
    @Autowired
    private DynamicCompileHandlerImpl dynamicBeanHandler;

    /***
     * freemarker配置文件的 信息
     *  -- 类型
     */
    @Autowired
    private FreemarkerParametersConfigure parameterConfigure;

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;


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
            if (WatcherEnum.COMPILE_EXECUTE.equals(key)) {
                log.info("====== 执行 动态编译 处理  ======");
                compile((Map<String, Object>) messages.get(key));
            }
        }
    }

    /****
     * 1. 编译执行的 主方法
     * @param messages 规则信息
     */
    void compile(Map<String, Object> messages){

        // 1. 预先校验
        if (!CheckValueUtil.notBlank(messages)) {
            return;
        }

        // 2. 遍历 编译后集合
        for (String className : messages.keySet()) {
            String classNameResult = MyStrUtils.getClassName(className);
            String packagePath = MyStrUtils.getPackagePath(className);
            packagePath = MyStrUtils.pointConvertForReflex(packagePath);
            try {
                // 执行动态编译
                dynamicBeanHandler.loadBean(parameterConfigure.getFreemarkerResourceName(),
                        (String) messages.get(className), classNameResult,packagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }





}
