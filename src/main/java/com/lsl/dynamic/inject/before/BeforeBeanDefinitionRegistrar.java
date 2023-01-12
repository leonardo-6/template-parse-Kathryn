package com.lsl.dynamic.inject.before;

import com.lsl.utils.check.CheckValueUtil;
import com.lsl.dynamic.compiler.impl.DynamicCompileHandlerImpl;
import com.lsl.utils.strings.MyStrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: TODO
 * @Author: lsl
 * @Data:2023-01-04 15:28
 * @Projectname: Kathryn
 * @Filename: CustomImportBeanDefinitionRegistart
 */
@Slf4j
@Component
public class BeforeBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    private ConcurrentHashMap<String, Class> DYNAMIC_BEAN = DynamicCompileHandlerImpl.DYNAMIC_BEAN;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 1. 根据注入的FactoryBean 获取 BeanDefinitionBuilder
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(BeforeFactoryBean.class);

        // 2. 用 BeanDefinitionBuilder 新增 beanDefinition
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

        if (!CheckValueUtil.notBlank(DYNAMIC_BEAN)) {
            log.info("当前无 动态编译类 需要注入 容器");
            return;
        }

        for (String beanName : DYNAMIC_BEAN.keySet()) {
            if (registry.containsBeanDefinition(MyStrUtils.firstCharacterToLower(MyStrUtils.getClassName(beanName)))) {
                continue;
            }

            // 2.1. 给类注入属性
            // -- 实质通过set方式 塞入对应属性
            beanDefinition.getPropertyValues().add("templateClass",DYNAMIC_BEAN.get(beanName));

            // 3. 通过 BeanDefinitionRegistry 注册新的 beanDefinition 到容器
            // -- 相当于将FactoryBean put到 BeanDefinition 中
            registry.registerBeanDefinition(MyStrUtils.firstCharacterToLower(MyStrUtils.getClassName(beanName)),beanDefinition);
        }
    }

}
