package com.lsl.dynamic.inject.next;

import com.lsl.annotation.NextMapperScan;
import com.lsl.utils.check.CheckValueUtil;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.io.File;
import java.net.URL;

/**
 * 实际 动态注入的位置
 * @Description: 以 @import 的方式动态注入
 * @Author: lsl
 * @Data:2023-01-10 16:28
 * @Projectname: Kathryn
 * @Filename: MyImportBeanDefinitionRegistrar
 */
public class NextImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

        // 1. 获取 用户添加的dao 扫包路径
        AnnotationAttributes mapperScanAttrs = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(NextMapperScan.class.getName()));

        // 2. 获取MyMapperScan的value即包名的数组
        String[] values = (String[]) mapperScanAttrs.get("value");

        // 3. 获取包下所有 接口
        ClassLoader classLoader = NextImportBeanDefinitionRegistrar.class.getClassLoader();

        // 4.0 . 校验是否存在扫包的路径
        if (!CheckValueUtil.notBlank(values)) {
            return;
        }

        // 4.1. 开始扫包
        for (String value : values) {

            // 5. 获取对应的url路径
            URL url = classLoader.getResource(value.replace(".", "/"));
            // 5.1. 获取包路径的目录
            File file = new File(getRootPath(url));

            // 5.2. 获取包目录下所有文件名
            String[] names = file.list();

            // 5.3. 遍历所有文件名
            for (String name : names) {

                // 5.3.1. 获取文件后缀的位置
                int pos = name.indexOf('.');

                // 5.3.2. 切 只获取文件名
                if (-1 != pos) {
                    // UserMapper.class -> UserMapper
                    name = name.substring(0, pos);
                }

                // 6. 通过 MyFactoryBean的方式 获取 BeanDefinitionBuilder
                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(NextFactoryBean.class);
                AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

                // 6.1. 以构造方法的方式 添加对应的class
                // --- com.demo.dao.UserMapper
                beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(value + "." + name);

                // 7. 加工beanName符合容器风格
                // --- UserMapper - > userMapper
                String beanName = name.substring(0, 1).toLowerCase().concat(name.substring(1));

                // 8. Bean 注入到容器
                registry.registerBeanDefinition(beanName, beanDefinition);
            }
        }
    }

    public String getRootPath(URL url) {
        String fileUrl = url.getFile();
        int pos = fileUrl.indexOf('!');

        if (-1 == pos) {
            return fileUrl;
        }

        return fileUrl.substring(5, pos);
    }
}
