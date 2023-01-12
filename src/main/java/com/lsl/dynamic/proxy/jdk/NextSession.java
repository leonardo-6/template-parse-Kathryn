package com.lsl.dynamic.proxy.jdk;

import com.lsl.observer.subject.SqlExecuteSubject;
import com.lsl.observer.watch.SqlExecuteWatcher;
import com.lsl.utils.bean.BeanUtils;
import com.lsl.utils.check.CheckValueUtil;
import com.lsl.common.enums.CharInfoEnum;
import com.lsl.common.enums.CommonEnum;
import com.lsl.common.enums.SqlHandleEnum;
import com.lsl.common.enums.WatcherEnum;
import com.lsl.configure.freemarker.FreeMarkerConfig;
import com.lsl.configure.freemarker.support.FreemarkerSupport;
import com.lsl.utils.strings.MyStrUtils;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 实际处理 sql的位置 --
 * @Description: 动态代理 方法
 * @Author: lsl
 * @Data:2023-01-10 15:56
 * @Projectname: Kathryn
 * @Filename: Mysession
 */
@Slf4j
public class NextSession {

    public static <T> T getMapper(Class<T> type) {
        return (T) Proxy.newProxyInstance(NextSession.class.getClassLoader(), new Class[]{type}, new MyInvocationHandler());
    }

    private static class MyInvocationHandler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            //0.  把进来的 toString 方法 排除了
            if(CommonEnum.TO_STRING.getValue().equals(method.getName())){
                return proxy.getClass().getInterfaces()[0].getName();
            }

            // 1. 分析 如何处理解析的sql
            switch (Objects.requireNonNull(analysisMethod(method))){
                // 1.1. 解析sql语句即可
                case PARSE_SQL:
                    Object parseSql = generalProcess(proxy, method, args);

                    try {
                        return  String.valueOf(parseSql);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                // 1.2. 解析完以后执行sql语句
                case EXECUTE_SQL:
                    Object executeSql = generalProcess(proxy, method, args);
                    try {
                        // 获取到解析的结果
                        String sql =  String.valueOf(executeSql);

                        // 发起通知
                        SqlExecuteSubject sqlExecuteSubject = BeanUtils.getBean(SqlExecuteSubject.class);
                        Map<WatcherEnum, Map<CommonEnum, Object>> watcherEnumMap= packageMessage(sql, method);
                        sqlExecuteSubject.notify(watcherEnumMap);

                        SqlExecuteWatcher sqlExecuteWatcher = BeanUtils.getBean(SqlExecuteWatcher.class);
                        return sqlExecuteWatcher.getResult();


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    return proxy;
            }

            // TODO jdbc链接 sql提交
            return null;
        }

        /*****
         * 主链路 处理 的通用方法
         * @param proxy 代理
         * @param method 方法
         * @param args 参数
         * @return Object
         */
        private Object generalProcess(Object proxy, Method method, Object[] args){

            // 1. 获取方法名
            Template template = findTemplate(method);
            if (!CheckValueUtil.notBlank(template)) {
                log.error("找不到当前 方法对应子模板");
                return proxy;
            }
            log.info("==== 1. 获取的模板为:{} ", template);

            // 2. 获取方法的入参
            Map parameter = findParameter(args);
            log.info("==== 2. 获取的参数为: {}", parameter);

            // 3. 解析当前的方法模板
            String sql = FreemarkerSupport.parseSql(template, parameter);
            log.info("==== 3. 执行后的sql为：{}",sql);

            return sql;
        }



        /****
         * 0. 分析怎么执行sql
         * @param method 方法
         * @return 枚举方式
         */
        private SqlHandleEnum analysisMethod(Method method){

            String methodName = method.getName();

            if (methodName.contains(SqlHandleEnum.PARSE_SQL.getValue()) && methodName.length() >SqlHandleEnum.PARSE_SQL.getValue().length()) {
                return SqlHandleEnum.PARSE_SQL;
            }

            return SqlHandleEnum.EXECUTE_SQL;
        }


        /****
         * 1. 获取方法对应的 模板
         * @param method 方法
         * @return 模板
         */
        private Template findTemplate(Method method){

            // 1.1. 获取代理对象的方法名
            String methodName = method.getName();
            if (methodName.contains(SqlHandleEnum.PARSE_SQL.getValue())) {
                methodName =  methodName.replace(SqlHandleEnum.PARSE_SQL.getValue(),"");
            }

            // 1.2. 获取代理对象的类名
            String className = MyStrUtils.getClassName(method.getDeclaringClass().getName());

            Template template = null;

            // 2. 遍历 子模板集合 获取
            for (String key : FreeMarkerConfig.CHILDREN_TEMPLATE.keySet()) {
                // 2.1. 包含类命和方法同时存在 很大可能就是对应的 模板 (但不是100%）
                // -- 当前策略是 存在多个就 前面覆盖后面
                if (key.contains(className+ CharInfoEnum.POINT.symbol()+methodName)) {
                    template = FreeMarkerConfig.CHILDREN_TEMPLATE.get(key);
                }
            }

            return template;
        }

        /****
         * 2. 获取方法的入参
         * @param args 入参
         * @return 入参集合
         */
        private Map findParameter(Object[] args){

            // 0. 建一个空集合
            Map<String, Object> parameterMap = new HashMap<>();

            // 2. 校验当前的传参是否为空
            if (CheckValueUtil.notBlank(args)) {
                //2.1. 不为空下 遍历入参
                for (Object arg : args) {
                    // 2.1.1. 这边定死的参数为 map
                    if(Map.class.isAssignableFrom(arg.getClass())){
                        parameterMap = (Map<String, Object>) arg;
                    }
                }
            }

            return parameterMap;
        }

        /*****
         * 3. 封装消息
         * @param sql sql 语句
         * @param method 方法
         * @return
         */
        private Map<WatcherEnum,Map<CommonEnum,Object>> packageMessage(String sql,Method method){
            HashMap<WatcherEnum,Map<CommonEnum,Object>> map = new HashMap<>();

            Map<CommonEnum,Object> detailMap = new HashMap<>();
            detailMap.put(CommonEnum.SQL,sql);
            detailMap.put(CommonEnum.METHOD,method);

            map.put(WatcherEnum.SQL_EXECUTE,detailMap);

            return map;
        }

    }




}
