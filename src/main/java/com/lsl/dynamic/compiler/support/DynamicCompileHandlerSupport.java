package com.lsl.dynamic.compiler.support;

import com.lsl.dynamic.compiler.classload.NextClassLoader;
import com.lsl.exception.MyFreemarkerException;
import com.lsl.dynamic.inject.before.BeforeBeanDefinitionRegistrar;
import com.lsl.utils.file.FileUtils;
import com.lsl.utils.strings.MyStrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.stereotype.Service;

import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static com.lsl.common.enums.CharInfoEnum.POINT;

/**
 * 动态编译的支持类
 * @Description: TODO
 * @Author: lsl
 * @Data:2023-01-04 13:36
 * @Projectname: Kathryn
 * @Filename: DynamicBeanHandlerImpl
 */
@Slf4j
@Service
public class DynamicCompileHandlerSupport {

    /****
     * import的 Bean定义的注入者
     */
    @Autowired
    private BeforeBeanDefinitionRegistrar customImportBeanDefinitionRegistrar;

    /****
     * 真正的spring 容器核心类
     */
    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

    /*****
     * 1. 动态创建 java 文件
     * @param resourceName 资源文件下目录
     * @param javaCode java代码
     * @param beanName class名字
     * @param packageSuffix 包名前缀
     * @throws IOException 异常
     */
    public File createJavaFile(String resourceName, String javaCode, String beanName, String packageSuffix) throws IOException {

        return FileUtils.createTempFileWithFileNameAndContent(resourceName,beanName, ".java",javaCode.getBytes(),packageSuffix);
    }

    /*******
     * 2. 动态编译 java 文件 为class
     * @param file java文件
     * @param beanName 类名
     * @throws MyFreemarkerException
     * @throws IOException
     */
    public void dynamicCompile(File file, String beanName) throws MyFreemarkerException, IOException {
        // 把生成的.java文件编译成.class文件
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager manage = compiler.getStandardFileManager(null, null, null);

        List<String> options = new ArrayList<String>();

        options.add("-classpath");
        StringBuilder sb = new StringBuilder();
        URLClassLoader urlClassLoader = (URLClassLoader) Thread.currentThread().getContextClassLoader();
        for (URL url : urlClassLoader.getURLs()) {
            sb.append(url.getFile()).append(File.pathSeparator);
        }
        options.add(sb.toString());
        options.add("-d");
        options.add(DynamicCompileHandlerSupport.class.getProtectionDomain().getCodeSource().getLocation().getPath());

        Iterable iterable = manage.getJavaFileObjects(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, manage, null, options, null, iterable);
        Boolean isOk = task.call();
        if(isOk){
            log.info("{} {}",beanName,"-编译成功");
        }else{
            throw new MyFreemarkerException(String.format("动态编译失败，className %s", beanName));
        }
        manage.close();
    }

    /****
     * 3. 读取编译的class 信息
     * @param file java 文件
     * @param beanName 类名
     * @param packageSuffix 包路径
     * @throws MalformedURLException
     */
    public Class loadBean(File file, String beanName, String packageSuffix) throws MalformedURLException {

        log.info("loadBean，loadClass {} start, to {}",beanName,file.getParent());
        URL[] urls = new URL[]{new URL("file:/"+file.getParent()+"/")};
        URLClassLoader loader = new NextClassLoader(urls, Thread.currentThread().getContextClassLoader());

        Class c = null;
        try {
            c = loader.loadClass(MyStrUtils.pointConvert(packageSuffix)+POINT.symbol()+beanName);
            return c;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        log.info("loadBean，loadClass {} end",beanName);
        return c;
    }

    /*****
     * 4. 动态注入class
     * @param beanName 类名
     */
    public void dynamicInjectBean(String beanName){
        log.info("loadBean，inject bean to IOC, {} start",beanName);
        customImportBeanDefinitionRegistrar.registerBeanDefinitions(null,defaultListableBeanFactory);
    }

    /***
     * 5. 调用构造方法 只是为了注入到BaseTag
     * @param aClass
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public void createForBaseTag(Class aClass) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?>[] constructors = aClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            constructor.newInstance();
        }
    }
}


