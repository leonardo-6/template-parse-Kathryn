package com.lsl.configure.freemarker.support;

import com.alibaba.fastjson2.JSONObject;
import com.lsl.configure.freemarker.FreeMarkerConfig;
import com.lsl.utils.check.CheckValueUtil;
import com.lsl.common.enums.CharInfoEnum;
import com.lsl.utils.strings.MyStrUtils;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.lsl.common.constant.FreemarkerConstant.CUSTOMER_PARAMETERS_MAP;

/**
 * freemarker支持类
 * @Description: 针对freemarker的util
 * @Author: lsl
 * @Data:2022-12-09 17:08
 * @Projectname: Kathryn
 * @Filename: FreemarkerUtil
 */
@Slf4j
public class FreemarkerSupport {

    /***
     * 1. 根据内容获取 id的名称
     * @param content 传入的模板内容
     * @return
     */
    public static String getChildTemplateName(String resourceName,String templatePath,String content) {
        StringBuilder sb = new StringBuilder();

        String motherTemplateName = getMotherTemplateName(resourceName, templatePath);
        sb.append(motherTemplateName).append(CharInfoEnum.POINT.symbol());
        String methodName = getMethodName(content);
        sb.append(methodName);

        return sb.toString();
    }

    public static String getMotherTemplateName(String resourceName,String templatePath) {
        // 1. 去除前缀的符号
        resourceName = MyStrUtils.spliceFirstSymbol(resourceName);
        templatePath = MyStrUtils.spliceFirstSymbol(templatePath);

        // 2. 生成拼接符号
        StringBuilder sb = new StringBuilder();
        // 2.1. 存在根目录 就添加根目录
        if (CheckValueUtil.notBlank(resourceName)) {
            sb.append(MyStrUtils.pointConvert(resourceName)).append(CharInfoEnum.POINT.symbol());
        }

        // 3. 处理尾缀的 内容
        String pointAndFirstToUpper = MyStrUtils.replacePointAndFirstToUpper(templatePath, CharInfoEnum.POINT.symbol(), CharInfoEnum.EMPTY.symbol());
        String pointConvert = MyStrUtils.pointConvert(pointAndFirstToUpper);

        // 4. 最后一位 点号的后一位 大写
        pointConvert = MyStrUtils.pointAfterFirstToUpper(pointConvert,CharInfoEnum.POINT.symbol(), CharInfoEnum.EMPTY.symbol());
        sb.append(pointConvert);

        return sb.toString();
    }

    /****
     * 转成 左斜杠 的路径名
     * @param resourceName
     * @param templatePath
     * @return
     */
    public static String getMotherTemplateNameForReflex(String resourceName,String templatePath) {
        if (!CheckValueUtil.notBlank(resourceName)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(resourceName).append(CharInfoEnum.SLASH.symbol());

        String pointAndFirstToUpper = MyStrUtils.replacePointAndFirstToUpper(templatePath, CharInfoEnum.POINT.symbol(), CharInfoEnum.EMPTY.symbol());
        String pointConvert = MyStrUtils.pointConvertForReflex(pointAndFirstToUpper);
        // 最后一位 点号的后一位 大写
        pointConvert = MyStrUtils.pointAfterFirstToUpper(pointConvert,CharInfoEnum.SLASH.symbol(), CharInfoEnum.EMPTY.symbol());
        sb.append(pointConvert).append(CharInfoEnum.SLASH.symbol());

        return sb.toString();
    }


    /***
     * 1. 根据内容获取 id的名称
     * @param content 传入的模板内容
     * @return
     */
    public static String getMethodName(String content) {
        if (!CheckValueUtil.notBlank(content.trim())) {
            return "";
        }

        return getIdValue(patternId(content.trim()));
    }

    /***
     * 1.1. 正则处理 id的 内容获取
     * @param source 源文件内容
     * @return id = "xxx"
     */
    public static String patternId(String source) {
        Pattern pattern = Pattern.compile("id\\s*=\\s*\"\\s*\\w*\\s*\"");
        Matcher matcher = pattern.matcher(source);
        matcher.find();
        return matcher.group();
    }

    /***
     * 1.2. 获取 id的值
     * @param source 进一步处理后的内容
     * @return 获取 = 后面的内容
     */
    public static String getIdValue(String source) {
        if (CheckValueUtil.notBlank(source) && source.contains("=")) {
            String[] split = source.split("=");
            return split[1].replaceAll("\"", "").trim();
        }
        return null;
    }

    /***
     * 2. 正则处理 id的 内容获取
     * @param source 源内容
     * @return 获取@的内容
     */
    public static String patternAt(String source) {
        if (!CheckValueUtil.notBlank(source.trim())) {
            return "";
        }

        Matcher matcher = null;
        try {
            Pattern pattern = Pattern.compile("(?<=@)\\S+");
            matcher = pattern.matcher(source);

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            if (matcher.find()) {
                return matcher.group().trim();
            }
            return "";
        }
    }


    /*****
     * 3. 去掉后缀处理 并且拿到相对路径前缀
     * @param methodName 方法名
     * @return
     */
    public static String[] removePointSuffix(String methodName) {
        if (!CheckValueUtil.notBlank(methodName)) {
            return null;
        }

        String[] strArray = new String[2];

        if (methodName.contains(".")) {

            String[] split = methodName.split("\\.");
            methodName =  split[0].trim();
        }

        if(methodName.contains("\\")){
            strArray[0]= methodName.substring(0, methodName.lastIndexOf("\\"));
            strArray[1]= methodName.substring(methodName.lastIndexOf("\\")+1 );
        }else {
            strArray[0]= "";
            strArray[1]= methodName;
        }
        return strArray;
    }

    /****
     * 4. 根据大模板的名称 和 方法名 -- 获取小模板
     * @param templateName 模板名
     * @param methodName 方法名
     * @return 小模板
     */
    public static Template getMethodTemp(String templateName, String methodName) {
        /*List<Template> templateList = MOTHER_TEMPLATES.get(templateName);
        if (!CheckValueUtil.notBlank(templateList)) {
            return null;
        }

        for (Template template : templateList) {
            String method = patternId(template.toString().trim());
            if (CheckValueUtil.notBlank(methodName) && methodName.trim().equalsIgnoreCase(getIdValue(method))) {
                return template;
            }
        }*/
        return null;
    }

    /******
     * 5. 根据方法名 获取执行的最终sql
     * @param childTemplate 子模板
     * @return 解析后的结果
     */
    public static String preExecuteJava(Template childTemplate){

        String result = null;

        try (Writer out = new StringWriter();) {
            // 第七步：调用模板对象的process方法输出文件。
            childTemplate.process( new HashMap<>(),out);
            result = ((StringWriter) out).getBuffer().toString();
        } catch (Exception e) {
            log.error("请求信息,异常信息：{}",e);
        }
        return result;
    }

    /******
     * 6. 根据方法名 获取执行的最终sql
     * @param childTemplate 子模板
     * @return 解析后的结果
     */
    public static String parseSql(Template childTemplate,Map map){

        String result = null;

        if (CheckValueUtil.notBlank(map)) {
            FreeMarkerConfig.ROOT.put(CUSTOMER_PARAMETERS_MAP,map);
        }

        try (Writer out = new StringWriter();) {
            // 第七步：调用模板对象的process方法输出文件。
            childTemplate.process( FreeMarkerConfig.ROOT,out);
            result = ((StringWriter) out).getBuffer().toString();
        } catch (Exception e) {
            log.error("请求信息{},异常信息：",FreeMarkerConfig.ROOT , e);
        }
        return result;
    }

    /******
     * 5. 根据方法名 获取执行的最终sql
     * @param methodName 方法名
     * @return 解析后的结果
     */
    public static String finalSql(String methodName){

        Template templateResult = FreeMarkerConfig.CHILDREN_TEMPLATE.get(methodName);

        String sql = null;

        Map<String, Object> map = new HashMap<>();

        try (Writer out = new StringWriter();) {
            // 第七步：调用模板对象的process方法输出文件。
            templateResult.process( map,out);
            sql = ((StringWriter) out).getBuffer().toString();
        } catch (Exception e) {
            log.error("请求信息{},异常信息：", JSONObject.toJSONString(map),e);
        }
        return sql;
    }
}
