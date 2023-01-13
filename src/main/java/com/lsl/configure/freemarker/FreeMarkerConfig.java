package com.lsl.configure.freemarker;

import com.lsl.configure.freemarker.parameters.FreemarkerParametersConfigure;
import com.lsl.configure.freemarker.support.FreemarkerSupport;
import com.lsl.configure.freemarker.tags.JavaTags;
import com.lsl.configure.freemarker.tags.SelectTags;
import com.lsl.configure.freemarker.tags.UpperTags;
import com.lsl.exception.MyFreemarkerException;
import com.lsl.observer.subject.CompileSubject;
import com.lsl.observer.watch.CompileExecuteWatcher;
import com.lsl.utils.check.CheckValueUtil;
import com.lsl.common.enums.DefaultTagEnum;
import com.lsl.common.enums.DynamicCompileEnum;
import com.lsl.common.enums.WatcherEnum;
import com.lsl.utils.file.FileUtils;
import com.lsl.utils.strings.MyStrUtils;
import freemarker.core.TemplateElement;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.lsl.common.constant.FreemarkerConstant.*;
import static com.lsl.common.enums.CharInfoEnum.POINT;
import static com.lsl.common.enums.CharInfoEnum.SLASH;
import static com.lsl.common.enums.DynamicCompileEnum.*;
import static com.lsl.common.enums.WatcherEnum.COMPILE_EXECUTE;

/**
 * freemarker的核心配置类
 * @Description: 在这里完成相关核心准备工作
 * @Author: lsl
 * @Data:2022-12-08 17:35
 * @Projectname: Kathryn
 * @Filename: FreeMarkerConfig
 */
@Configuration
public class FreeMarkerConfig {

    /***
     * 1. 模板容器
     *  -- 对应： 大模板 --- 小模板集合
     */
    public static final ConcurrentHashMap<String, List<String>> MOTHER_TEMPLATES = new ConcurrentHashMap<>();

    /***
     * 2. 模板容器
     *  -- 对应： 方法 -- 小模板
     */
    public static final ConcurrentHashMap<String, Template> CHILDREN_TEMPLATE = new ConcurrentHashMap<>();

    /****
     * 3. 模板容器
     * 模板名称 对应 MOTHER_TEMPLATES 的 key
     */
    public static final ConcurrentHashMap<String, String> TEMPLATE_NAME= new ConcurrentHashMap<>();


    /****
     * 4. 传参的收集器
     */
    public static final Map<String, Object> ROOT = new HashMap<>();

    /***
     * freemarker配置文件的 信息
     *  -- 类型
     */
    @Autowired
    private FreemarkerParametersConfigure parameterConfigure;

    /***
     * 配置文件集合
     * --类型
     *     -- 文件名 + 文件路径
     */
    private Map<String, Set<Map.Entry<String, String>>> collectMap = new HashMap<>();

    /****
     * freemarker 配置信息
     */
    protected freemarker.template.Configuration configuration;

    @Autowired
    private UpperTags upperTags;

    @Autowired
    private JavaTags javaTags;

    @Autowired
    private SelectTags selectTags;

    /****
     * 动态编译模板的 映射信息
     */
    private Map<DynamicCompileEnum, Template> dynamicCompileTemplate = new HashMap<>();

    /*****
     * 动态编译主题
     */
    @Autowired
    private CompileSubject compileSubject;

    /****
     * 动态编译的监听器
     */
    @Autowired
    private CompileExecuteWatcher executeWatcher;

    /***
     * 0. 读取配置文件 获取需要 FreeMarker的参数
     *  -- 并且将文件夹下的文件
     */
    @PostConstruct
    public  void collectParseFile() {
        String[] configFileNames = parameterConfigure.getConfigFileNames();
        if (!CheckValueUtil.notBlank(configFileNames)) {
            return;
        }
        // FREEMARKER_RESOURCE_NAME+SLASH.symbol()+
        for (String configFileName : configFileNames) {
            collectMap.put(configFileName,FileUtils.getFileNames(SLASH.symbol().equals(parameterConfigure.getFreemarkerResourceName())?
                    MyStrUtils.spliceFirstSymbol(SLASH.symbol()+configFileName):
                    MyStrUtils.spliceFirstSymbol(parameterConfigure.getFreemarkerResourceName()+SLASH.symbol()+configFileName)));
        }

        // 添加监听器
        compileSubject.attach(executeWatcher);
    }

    /*****
     * 1. 配置 freemarker的 配置类
     * @return
     * @throws IOException
     * @throws MyFreemarkerException
     */
    @Bean
    public freemarker.template.Configuration getConfiguration() throws IOException, MyFreemarkerException {

        // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
        configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_30);

        // 第二步：设置模板文件所在的路径。
        String rootPath = Thread.currentThread().getContextClassLoader().getResource(MyStrUtils.spliceFirstSymbol(parameterConfigure.getFreemarkerResourceName())).getPath();
        configuration.setDirectoryForTemplateLoading(new File(rootPath));

        // 第三步：设置模板文件使用的字符集。一般就是utf-8.
        configuration.setDefaultEncoding(UTF_8);

        // 第四步：加载一个模板，创建一个模板对象。
        // -- 各个子模板的生成
        generateChildTemplate();

        // 第五步： 添加自定义标签
        setSharedVariable();

        // 第六步: 根据母亲模板 动态编译用户sql
        dynamicCompile();

        // 第七步： 封装一个 初始的 数据模型
        packageParameter();

        return configuration;
    }



    /****
     * 4.生成子模板
     */
    public void generateChildTemplate() throws MyFreemarkerException {
        // 1. 获取所有标签
        String[] allTags = collectAllTags();

        // 2. 获取某类型下的所有文件
        if (CheckValueUtil.notBlank(collectMap)) {

            for(Map.Entry<String, Set<Map.Entry<String, String>>> entryMap:collectMap.entrySet()){

                Set<Map.Entry<String, String>> entries = collectMap.get(entryMap.getKey());

                for (Map.Entry<String, String> entry : entries) {
                    try {
                        Template template = configuration.getTemplate(entry.getKey());
                        collectChildrenTemplates(template,configuration,allTags,entryMap.getKey());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /***
     * 4.1 .合并两个数组
     * @return
     */
    public String[] collectAllTags() throws MyFreemarkerException {
        String[] defaultTags = DefaultTagEnum.getAllTypes();
        String[] customTags = parameterConfigure.getCustomTags();

        return MyStrUtils.mergerArray(defaultTags,customTags);
    }

    /***
     * 4.2. 根据大模板 去获取拆分的模板
     * @param template 模板
     * @return
     */
    public void collectChildrenTemplates(Template template, freemarker.template.Configuration configuration, String[] tags,String resourceName) {
        // 0. 是否传入的模板为空
        if (!CheckValueUtil.notBlank(template)) { return; }
        if (!CheckValueUtil.notBlank(tags)) { return; }

        // 1. 获取模板的 根模板
        TemplateElement te = template.getRootTreeNode();

        // 2. 产生一个模板集合 -- 存入当前模板的 小模板
        List<Template> templateList = new ArrayList<>();
        List<String> childNames = new ArrayList<>();

        // 3.遍历大模板的内容
        for (Enumeration children = te.children(); children.hasMoreElements(); ) {

            // 3.1. 获取每个大模板下的 小模板内容
            Object obj = children.nextElement();

            // 3.2. 校验小模板的内容是否为空
            if (CheckValueUtil.notBlank(obj.toString().trim())) {

                // 3.3. 遍历是否为 ”某个标签开头为依据“
                for (String tag : tags) {
                    // 3.3.1. 校验是否为 某个标签开头
                    if (FreemarkerSupport.patternAt(obj.toString().trim()).equals(tag)) {

                        // 3.3.2. 生成文件
                        String[] nameArray = FreemarkerSupport.removePointSuffix(template.getName());
                        String sourcePath = MyStrUtils.pointConvertForReflex(nameArray[0]);
                        String templateName = nameArray[1] + "_" + FreemarkerSupport.getMethodName(obj.toString());

                        //templateName = FreemarkerUtil.getMotherTemplateNameForReflex(resourceName,template.getSourceName()) + templateName;
                        // 3.3.3. 文件生成成功就获取
                        if (FileUtils.createFreeMarkSqlFile(MyStrUtils.spliceFirstSymbol(parameterConfigure.getFreemarkerResourceName()),sourcePath,templateName, obj.toString())) {
                            try {
                                // 3.3.3.1. 根据指定路径生成的文件 获取生成对应的小模板
                                Template templateTemp = configuration.getTemplate(sourcePath+TEMP_PATH + templateName + FREEMARKER_FILE_SUFFIX);

                                // 3.3.3.2. 将大模板切割成小模板保存
                                templateList.add(templateTemp);
                                String childTemplateName = FreemarkerSupport.getChildTemplateName(parameterConfigure.getFreemarkerResourceName(), template.getSourceName(), obj.toString());
                                childNames.add(childTemplateName);

                                // 3.3.3.3. 根据+方法名 拼接
                                if (CHILDREN_TEMPLATE.containsKey(childTemplateName)) {
                                    continue;
                                }
                                CHILDREN_TEMPLATE.put(childTemplateName,templateTemp);
                                TEMPLATE_NAME.put(templateTemp.getSourceName(),FREEMARKER_DYNAMIC_COMPILE_PREFIX + FreemarkerSupport.getMotherTemplateName(parameterConfigure.getFreemarkerResourceName(),template.getSourceName()));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }
        }
        MOTHER_TEMPLATES.put(FreemarkerSupport.getMotherTemplateName(parameterConfigure.getFreemarkerResourceName(),template.getSourceName()), childNames);

    }


    /**
     * 5. 添加自定义标签
     */
    public void setSharedVariable() {
        /***
         * 向freemarker配置中添加共享变量;
         * 它使用Configurable.getObjectWrapper()来包装值，因此在此之前设置对象包装器是很重要的。（即上一步的builder.build().wrap操作）
         * 这种方法不是线程安全的;使用它的限制与那些修改设置值的限制相同。
         * 如果使用这种配置从多个线程运行模板，那么附加的值应该是线程安全的。
         */
        try {
            configuration.setSharedVariable("Upper", upperTags);
            configuration.setSharedVariable("Java", javaTags);
            configuration.setSharedVariable("Select", selectTags);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /*****
     * 6. 动态编译 用户指定的模板 映射成类
     */
    private void dynamicCompile(){
        if (!CheckValueUtil.notBlank(MOTHER_TEMPLATES)) {
            return;
        }
        Map<String, String> map = new HashMap<>();
        for (String bigKey : MOTHER_TEMPLATES.keySet()) {
            // 1. 获取生成 动态类信息
            if(bigKey.startsWith("compile")){
                continue;
            }
            String className = MyStrUtils.getClassName(bigKey);
            String packagePath = MyStrUtils.getPackagePath(bigKey);
            List<String> methodNames = MyStrUtils.getMethodNames(MOTHER_TEMPLATES.get(bigKey));
            String baseTag = parameterConfigure.getBaseTag();
            String checkUtil = parameterConfigure.getCheckUtil();

            // 2. 获取小模板信息
            if (!CheckValueUtil.notBlank(dynamicCompileTemplate)){
                getJavaTemplate();
            }

            // 3. 生成对应的class代码
            String javaCode = MyStrUtils.createJavaCode(dynamicCompileTemplate,className,packagePath,methodNames,baseTag,checkUtil);
            map.put(packagePath+ POINT.symbol()+className,javaCode);
        }
        Map<WatcherEnum, Object> messageMap = new HashMap<>();
        messageMap.put(COMPILE_EXECUTE,map);
        compileSubject.notify(messageMap);

    }

    /****
     * 6.1. 收集模板对应信息
     * @return
     */
    private Map<DynamicCompileEnum, Template> getJavaTemplate(){

        if (!CheckValueUtil.notBlank(MOTHER_TEMPLATES)) {
            return dynamicCompileTemplate;
        }

        for (String bigKey : MOTHER_TEMPLATES.keySet()) {
            if (bigKey.startsWith("compile")){
                List<String> values = MOTHER_TEMPLATES.get(bigKey);
                if (CheckValueUtil.notBlank(values)) {
                    for (String value : values) {
                        Template template = CHILDREN_TEMPLATE.get(value);
                        collectClassTemplate(dynamicCompileTemplate,template);
                    }
                }
            }
        }

        return dynamicCompileTemplate;

    }

    /*****
     * 6.1.1. 收集模板 映射的 类模板的枚举
     * @param map
     * @param template
     */
    private void collectClassTemplate(Map<DynamicCompileEnum, Template> map,Template template){
        if (!CheckValueUtil.notBlank(template)) {
            return ;
        }
        String methodName = FreemarkerSupport.getMethodName(template.toString());
        switch (Objects.requireNonNull(DynamicCompileEnum.getByType(methodName))){
            case PACKAGE:
                map.put(PACKAGE,template);
                break;
            case IMPORT_BASE_TAGE:
                map.put(IMPORT_BASE_TAGE,template);
                break;
            case IMPORT_CHECK_UTIL:
                map.put(IMPORT_CHECK_UTIL,template);
                break;
            case  IMPORT_COMMON:
                map.put(IMPORT_COMMON,template);
                break;
            case CLASS_NAME:
                map.put(CLASS_NAME,template);
                break;
            case CLASS_METHOD:
                map.put(CLASS_METHOD,template);
                break;
            case  CLASS_END:
                map.put(CLASS_END,template);
                break;
            default:
                break;
        }
    }

    /*****
     * 7. 包装一个传参的 集合
     */
    public void packageParameter(){
        ROOT.put(CUSTOMER_PARAMETERS_MAP,new HashMap<>());
    }

}
