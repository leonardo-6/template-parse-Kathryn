package com.lsl.configure.freemarker;

import com.lsl.configure.freemarker.support.FreemarkerSupport;
import com.lsl.utils.check.CheckValueUtil;
import com.lsl.common.constant.FreemarkerConstant;
import freemarker.core.Environment;
import freemarker.template.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 所有自定义标签的父类，负责调用具体的子类方法
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/9/18 16:19
 * @since 1.8
 */
@Slf4j
public abstract class BaseTag implements TemplateDirectiveModel {

    private static final ConcurrentHashMap<String, Class> classBucket = new ConcurrentHashMap<>();
    private String clazzPath = null;

    /****
     * 1. 获取目标的类路径
     * @param targetClassPath 目标类路径
     */
    public BaseTag(String targetClassPath) {
        clazzPath = targetClassPath;
        if (classBucket.get(clazzPath) == null) {
            try {
                Class clazz = Class.forName(clazzPath);
                classBucket.put(clazzPath, clazz);
            } catch (ClassNotFoundException e) {
                log.error("无法从[{}]获取对应的class", clazzPath, e);
            }
        }
    }


    /****
     * 2. 获取 方法名
     * @param params 参数
     * @return
     */
    private String getId(Map params) {
        return this.getParam(params, "id");
    }

    private String getParameters(Map params) {
        return this.getParam(params, "parameter");
    }

    /****
     * 3. 获取参数
     * @param params
     * @param paramName
     * @return
     */
    String getParam(Map params, String paramName) {
        Object value = params.get(paramName);
        return value instanceof SimpleScalar ? ((SimpleScalar) value).getAsString() : null;
    }

    /****
     * 4. 获取页面的大小
     * @param params
     * @return
     */
    protected int getPageSize(Map params) {
        int pageSize = 10;
        String pageSizeStr = this.getParam(params, "pageSize");
        if (!CheckValueUtil.notBlank(pageSizeStr)) {
            pageSize = Integer.parseInt(pageSizeStr);
        }
        return pageSize;
    }

    /***
     * 5. 验证 入参是否有值
     * @param params
     * @throws TemplateModelException
     */
    private void verifyParameters(Map params) throws TemplateModelException {
        String permission = this.getId(params);
        if (permission == null || permission.length() == 0) {
            throw new TemplateModelException("The 'name' tag attribute must be set.");
        }
    }


    /****
     * 6. 获取 DefaultObjectWrapper 的方法
     * @return
     */
    private DefaultObjectWrapper getBuilder() {
        return new DefaultObjectWrapperBuilder(Configuration.VERSION_2_3_30).build();
    }

    /***
     * 7. 获取 页面的模板
     * @param o
     * @return
     * @throws TemplateModelException
     */
    private TemplateModel getModel(Object o) throws TemplateModelException {
        return this.getBuilder().wrap(o);
    }

    private TemplateModel getTagTemplate(Template template){
        String tag = FreemarkerSupport.patternAt(template.toString().trim());
       // return configuration.getSharedVariable(tag);
        return null;
    }

    /****
     * 将当前模板的路径 与 动态生成的用户类 关联
     * @param classPath 类路径
     * @return 字节码文件
     */
    private Class getMethodParamClass(String classPath){
        //String paramClass = FileUtils.getMethodParamClass(filePath);
        if (CheckValueUtil.notBlank(classPath) && CheckValueUtil.notBlank(classBucket.get(classPath))) {
            return classBucket.get(classPath);
        }
        return null;
    }

    /****
     * 将当前模板的方法参数 传递
     * @return 字节码文件
     */
    private Map getMethodParameter(Environment environment){
        try {
            TemplateModel  customerParametersMap = environment.getGlobalVariables().get(FreemarkerConstant.CUSTOMER_PARAMETERS_MAP);
            Object wrappedObject = ((DefaultMapAdapter) customerParametersMap).getWrappedObject();

            if (!CheckValueUtil.notBlank((Map) wrappedObject)) {
                return new HashMap();
            }
            return (Map) wrappedObject;
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }
        return new HashMap();
    }

    /****
     * 8. 核心方法 执行自定义标签的 处理
     * @param environment
     * @param map
     * @param templateModels
     * @param templateDirectiveBody
     * @throws TemplateException
     * @throws IOException
     */
    @Override
    public void execute(Environment environment, Map map, TemplateModel[] templateModels, TemplateDirectiveBody templateDirectiveBody) throws TemplateException, IOException {
        // 1.1 验证模板入参
        this.verifyParameters(map);

        // 1.2. 获取模板 id-- 方法名
        String methodName = getId(map);

        // 1.3. 获取模板参数 parameter -- 参数
        String parameters = getParameters(map);

        Method method = null;
        // 2. 获取 方法的字节码文件
        Class clazz =  getMethodParamClass(FreeMarkerConfig.TEMPLATE_NAME.get(environment.getCurrentTemplate().getSourceName()));

        try {
            // 3. 通过反射的方式 调用获取参数的方法
            if (clazz != null && (method = clazz.getDeclaredMethod(methodName, Map.class)) != null) {
                method.setAccessible(true);
                // 核心处理，调用子类的具体方法，获取返回值
                Object res = method.invoke(clazz.getConstructor().newInstance(), getMethodParameter(environment));
                environment.setVariable(parameters, getModel(res));
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException| IllegalArgumentException|InstantiationException e) {
            log.error("无法获取[{}]的方法，或者调用[{}]方法发生异常", clazzPath, method, e);
        }
        templateDirectiveBody.render(environmentHandle(environment));
    }

    public Writer environmentHandle(Environment environment){
        return environment.getOut();
    }
}
