package com.lsl.dynamic.compiler.impl;

import com.lsl.dynamic.compiler.DynamicCompileHandler;
import com.lsl.dynamic.compiler.support.DynamicCompileHandlerSupport;
import com.lsl.exception.MyFreemarkerException;
import com.lsl.utils.bean.BeanUtils;
import com.lsl.utils.strings.MyStrUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

import static com.lsl.common.enums.CharInfoEnum.POINT;

/**
 * 动态编译实现类
 * @Description: 动态编译的实现类
 * @Author: lsl
 * @Data:2023-01-04 13:36
 * @Projectname: Kathryn
 * @Filename: DynamicBeanHandlerImpl
 */
@Slf4j
@Service
public class DynamicCompileHandlerImpl implements DynamicCompileHandler {

    //private ConcurrentHashMap<String, BeanTTL> cacheBean = new ConcurrentHashMap<>();

    /****
     * 动态编译的字节码 容器
     */
    public static final ConcurrentHashMap<String, Class> DYNAMIC_BEAN = new ConcurrentHashMap<>();

    /****
     * 动态编译的支持类
     */
    @Autowired
    private DynamicCompileHandlerSupport dynamicBeanSupport;


    @SneakyThrows
    @Override
    public void loadBean(String resourceName,String javaCode, String beanName,String packageSuffix) throws IOException {
        /** 0. 打印开始信息 */
        log.info("loadBean，compile {} start",beanName);

        /**  1. 创建 java 文件 */
        File javaFile = dynamicBeanSupport.createJavaFile(resourceName, javaCode, beanName, packageSuffix);

        /**  2. 动态编译 java 文件 为class */
        dynamicBeanSupport.dynamicCompile(javaFile,beanName);


        /**  3. 读取编译的class 信息 */
        Class aClass = dynamicBeanSupport.loadBean(javaFile, beanName, packageSuffix);

        DYNAMIC_BEAN.put(MyStrUtils.pointConvert(packageSuffix)+POINT.symbol()+beanName,aClass);

        /**   4. 动态注入class */
        dynamicBeanSupport.dynamicInjectBean(beanName);

        /** 5. 调用构造方法 只是为了注入到BaseTag */
        dynamicBeanSupport.createForBaseTag(aClass);

        DYNAMIC_BEAN.clear();
    }

    @Override
    public Object invoke(String beanName, String methodName) throws MyFreemarkerException {
        //if (find(beanName)) { return null; }
        try {
            Object bean = BeanUtils.getBean(beanName);
            return MethodUtils.invokeExactMethod(bean,methodName);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.info("executeMethod failed，{}::{}",beanName,methodName);
            log.info("executeMethod errMsg : {}",e);
        }
        return null;
    }

    @Override
    public Object invoke(String beanName, String methodName, Object[] args, Class<?>[] parameterTypes) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, MyFreemarkerException {
       // if (find(beanName) && ApplicationContextUtil.getBean(beanName)==null) { return null; }

        try {
            Object bean = BeanUtils.getBean(beanName);
            return MethodUtils.invokeExactMethod(bean,methodName,args,parameterTypes);
        } catch (NoSuchMethodException|IllegalAccessException e) {
            log.info("executeMethod failed，{}::{}",beanName,methodName);
            log.info("executeMethod errMsg : {}",e);
            throw e;
        }
    }

    /*private boolean find(String beanName) throws MyFreemarkerException {
        if(!cacheBean.containsKey(beanName)){
            String javaCode = getJavaCodeByBeanName(beanName);
            if(StringUtils.isBlank(javaCode)){
                log.info("executeMethod loadBean  failed,javaCode not found by beanName {}",beanName);
                return false;
            }
            try {
                loadBean(javaCode,beanName);
            } catch (Exception e) {
                log.info("executeMethod loadBean  failed，{}::{}",beanName);
                log.info("executeMethod loadBean errMsg : {}",e);
                throw new MyFreemarkerException(String.format("执行bean找不到，且无法加载，beanName %s", beanName));
            }
        }
        return true;
    }*/


    /*private String getJavaCodeByBeanName(String beanName) {
        DccClass dccClass = dccClassService.getByBeanName(beanName);
        if(dccClass!=null){
            return dccClass.getJavaCode();
        }
        return null;
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void syncFromDB() {
        // 一个select，就不放出来了
        List<DccClass> dccClasses = dccClassService.getAll();
        dccClasses.forEach(dccClass -> {
            if(!cacheBean.containsKey(dccClass.getBeanName()) || (cacheBean.get(dccClass.getBeanName()).getUpdatedTime().isBefore(dccClass.getUpdatedTime()))){
                loadBean(dccClass.getBeanName(),dccClass.getJavaCode());
            }
        });
    }*/
}


