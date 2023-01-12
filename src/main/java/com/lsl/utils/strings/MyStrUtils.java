package com.lsl.utils.strings;

import com.lsl.exception.MyFreemarkerException;
import com.lsl.utils.check.CheckValueUtil;
import com.lsl.common.constant.FreemarkerConstant;
import com.lsl.common.enums.DynamicCompileEnum;
import com.lsl.common.enums.ExceptionInfoEnum;
import com.lsl.common.enums.TableInfoEnum;
import com.lsl.configure.freemarker.support.FreemarkerSupport;
import freemarker.template.Template;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

import static com.lsl.common.enums.CharInfoEnum.*;
import static com.lsl.common.enums.DynamicCompileEnum.*;


/**
 * 字符串 工具
 * describe 鉴于某些工具类没有的方法 - 有些可能有
 * @author LuShuL 12304
 * @version V1.0
 * @date 2022/5/12 9:32
 */
public class MyStrUtils {

    /**
     * 工具类 --- 1
     * 表名 转成 sql 钟可被 in的形式
     * @param list
     * @return
     */
    public static <T> String listConvertStr(List<T> list, ExceptionInfoEnum exceptionMsg) throws MyFreemarkerException {
        // 1. 校验传入是否为空
        if(!CheckValueUtil.notBlank(list)){
            throw new MyFreemarkerException(exceptionMsg.getMsg());
        }

        // 2. 构建一个拼接字符串的工具
        StringBuffer sb = new StringBuffer();

        // 3. 遍历拼接表名 信息
        for (Object obj : list) {
            // 3.1. String类型的拼接
            if (obj.getClass() == String.class) {
                sb.append(APOSTROPHE.symbol()).append(obj).append(APOSTROPHE_COMMA.symbol());

                // 3.2. 这里就不是很工具了 因为我这里只给这个类用 -- 针对某个对象的拼接(多个还得抽一层方法)
            }/*else if(obj.getClass() == Type.class){
                sb.append(APOSTROPHE.symbol()).append(((Type)obj).getTypeId().trim()).append(APOSTROPHE_COMMA.symbol());

                // 3.3. 没有指定的拼接字符 直接抛出问题
            }*/else {
                throw new MyFreemarkerException(ExceptionInfoEnum.OBJECT_NOT_SUPPORT.getMsg());
            }
        }
        // 4. 去掉最后一个 逗号 返回拼接结果
        return sb.deleteCharAt(sb.length()-1).toString();
    }

    /**
     * 工具类 ---2
     * 根据传入的 ，拼接的表切割返回集合
     * @param str
     * @return
     */
    public static List<String> strToList(String str) throws MyFreemarkerException {
        if(!CheckValueUtil.notBlank(str)){
            throw new MyFreemarkerException(ExceptionInfoEnum.INPUT_VALUE_BLANK.getMsg());
        }

        List<String> tableNameList = new ArrayList<>();
        String[] split = str.split(COMMA.symbol());
        for (String tableName : split) {
            tableNameList.add(tableName.trim().toUpperCase(Locale.ROOT));
        }
        return tableNameList;
    }

    /**
     * 工具类3 -- 合并多个表名到一个map集合
     * @param map 集合
     * @param tableName 表名
     */
    public static void mergeTipTable(Map<String, Object> map, String tableName, TableInfoEnum tableInfo){

        // 0. 创建拼接字符串工具
        StringBuffer sb = new StringBuffer();

        // 1. 校验是否存在表名
        if(CheckValueUtil.notBlank(map.get(tableInfo.name()))){
            // 1.1. 将有表信息的数据 拼接进表名
            sb.append(map.get(tableInfo.name())).append(COMMA.symbol()).append(tableName);
            map.put(tableInfo.name(),sb.toString());
            return ;
        }

        // 2. 不存在已有表名 直接新增
        map.put(tableInfo.name(),tableName);
    }

    /***
     * 工具类4 -- 将数组拼成字符串
     * @param arrays  字符串数组
     * @param infoEnum  异常信息枚举
     * @return 字符串
     */
    public static String spliceString(String [] arrays,ExceptionInfoEnum infoEnum) throws MyFreemarkerException {
        // 1. 判断传入条件是否有值
        if(!CheckValueUtil.notBlank(arrays)){
            throw new MyFreemarkerException(infoEnum.getMsg());
        }

        // 2. 拼接数组的字符串
        StringBuffer sb = new StringBuffer();
        for (String array : arrays) {
            sb.append(array.trim()).append(COMMA.symbol());
        }

        // 3. 返回拼接后的结果
        return sb.substring(0, sb.length() - 1).toString();
    }

    /***
     * 工具类 5 --将list集合 转成 string 数组
     * @param list 当前已经拥有的操作权限
     * @return String数组
     */
    public static String[] getArrayByList(List list){
        // 1.校验当前集合存在
        if(!CheckValueUtil.notBlank(list)){
            // 表明当前无 操作权限
            return new String[0];
        }

        // 2. 创建空集合
        String[] resultArray = null;

        // 3. 遍历根据 逗号截取权限
        for (Object element : list) {
            // 获取的操作 拆分
            for (String auth : element.toString().split(COMMA.symbol())) {
                resultArray = ArrayUtils.add(resultArray,auth);
            }
        }

        return resultArray;
    }

    /****
     * 工具类 6 -- 获取两个String数组中  不同的元素
     * @param apply 申请的元素数组
     * @param auths 已经存在的元素数组
     * @return Set<String>
     */
    public static String[] getDifferentSet(String[] apply, String[] auths) {

        // 1. 用来存放两个数组中不同的元素
        String[] diffArray = null;

        // 2. 用来存放数组auths中的元素
        Set<String> temp = new HashSet();

        // 3. 判断 授权权限是否存在
        if(CheckValueUtil.notBlank(auths)){
            for (int i = 0; i < auths.length; i++) {
                //3.1. 把数组授权信息auths中的元素放到Set中，可以去除重复的元素
                temp.add(auths[i].trim());
            }
        }

        // 4. 判断申请的权限 是否存在
        if(CheckValueUtil.notBlank(apply)){
            for (int j = 0; j < apply.length; j++) {
                // 4.1. 利用set去重, 添加 申请中不同的权限
                if (temp.add(apply[j].trim())) {
                    diffArray = ArrayUtils.add(diffArray,apply[j].trim());
                }
            }
        }

        return diffArray;
    }

    public static String[] toStrArray(Object obj) throws MyFreemarkerException {
        if (!CheckValueUtil.notBlank(obj)) {
            throw new MyFreemarkerException(ExceptionInfoEnum.INPUT_VALUE_BLANK.getMsg());
        }

        if (obj instanceof List){
            String[] strArray = new String[((List) obj).size()];

            Object[] objs = ((List) obj).toArray();
            for (int i = 0; i < ((List) obj).size(); i++) {
                if (!(objs[i] instanceof String)){
                    throw new MyFreemarkerException(ExceptionInfoEnum.INPUT_VALUE_BLANK.getMsg());
                }
                strArray[i] = (String) objs[i];
            }

            return strArray;
        }else {
            if (!(obj instanceof String)){
                throw new MyFreemarkerException(ExceptionInfoEnum.INPUT_VALUE_BLANK.getMsg());
            }
            return new String[]{String.valueOf(obj)};
        }
    }

    /***
     * 0.合并两个数组
     * @return
     */
    public static String[] mergerArray(String[] firstArray,String[] secondArray) throws MyFreemarkerException {

        if (!CheckValueUtil.notBlank(firstArray)) {
            throw new MyFreemarkerException(505,"");
        }
        if (!CheckValueUtil.notBlank(secondArray)) {
            throw new MyFreemarkerException(505,"");
        }

        // 保存第一个数组长度
        int firstLength = firstArray.length;

        // 保存第二个数组长度
        int secondLength = secondArray.length;

        // 扩容
        firstArray = Arrays.copyOf(firstArray, firstLength + secondLength);

        // 将第二个数组与第一个数组合并
        System.arraycopy(secondArray, 0, firstArray, firstLength, secondLength);

        // 输出数组
        return firstArray;
    }


    /**
     * 首字母大写
     *
     * @param srcStr
     * @return
     */
    public static String spliceFirstSymbol(String srcStr) {
        if (!CheckValueUtil.notBlank(srcStr)) {
            return "";
        }
        if(srcStr.startsWith("/") || srcStr.startsWith("\\")){
            return srcStr.substring(1);
        }
       return srcStr;
    }

    /**
     * 首字母大写
     *
     * @param srcStr
     * @return
     */
    public static String firstCharacterToUpper(String srcStr) {
        if (!CheckValueUtil.notBlank(srcStr)) {
            return "";
        }
        return srcStr.substring(0, 1).toUpperCase() + srcStr.substring(1);
    }

    /**
     * 首字母大写
     *
     * @param srcStr
     * @return
     */
    public static String firstCharacterToLower(String srcStr) {
        if (!CheckValueUtil.notBlank(srcStr)) {
            return "";
        }
        return srcStr.substring(0, 1).toLowerCase(Locale.ROOT) + srcStr.substring(1);
    }


    /**
     * 替换字符串并让它的下一个字母为大写
     *
     * @param srcStr
     * @param org
     * @param ob
     * @return
     */
    public static String replacePointAndFirstToUpper(String srcStr, String org, String ob) {
        String newString = "";
        int first = 0;
        while (srcStr.indexOf(org) != -1) {
            first = srcStr.indexOf(org);
            if (first != srcStr.length()) {
                newString = newString + srcStr.substring(0, first) + ob;
                srcStr = srcStr.substring(first + org.length(), srcStr.length());
                srcStr = firstCharacterToUpper(srcStr);
            }
        }
        newString = newString + srcStr;
        return newString;
    }

    /**
     * 替换字符串并让它的下一个字母为大写
     *
     * @param srcStr
     * @param org
     * @param ob
     * @return
     */
    public static String pointAfterFirstToUpper(String srcStr, String org, String ob) {
        if (!CheckValueUtil.notBlank(srcStr)) {
            return srcStr;
        }

        if(srcStr.contains(org)){
            int last = srcStr.lastIndexOf(org);
            if(srcStr.length()> last+1){
              return srcStr.substring(0,last+1) + srcStr.substring(last+1, last+2).toUpperCase() + srcStr.substring(last+2);
            }
        }else {
            return firstCharacterToUpper(srcStr);
        }
        return srcStr;
    }

    public static String pointConvert(String srcStr) {
        if (!CheckValueUtil.notBlank(srcStr)) {
            return "";
        }

        if (srcStr.contains("/")){
            srcStr = srcStr.replaceAll("/",".");
        }

        if (srcStr.contains("\\")){
            srcStr = srcStr.replaceAll("\\\\",".");
        }

        return srcStr;
    }

    public static String pointConvertForReflex(String srcStr) {
        if (!CheckValueUtil.notBlank(srcStr)) {
            return "";
        }

        if (srcStr.contains("\\")){
            srcStr = srcStr.replaceAll("\\\\","/");
        }

        if (srcStr.contains(".")){
            srcStr = srcStr.replaceAll("\\.","/");
        }

        return srcStr;
    }

    /*****
     * 获取 class 的名称
     * @param source
     * @return
     */
    public static String getClassName(String source){
        if (!CheckValueUtil.notBlank(source)) {
            return source;
        }

        if (source.contains(".")){
            return source.substring(source.lastIndexOf(".")+1);
        }
        return source;
    }

    /*****
     * 获取 package  的包名
     * @param source
     * @return
     */
    public static String getPackagePath(String source){
        if (!CheckValueUtil.notBlank(source)) {
            return source;
        }

        if (source.contains(".")){
            source = source.substring(0,source.lastIndexOf("."));
        }

        if (source.startsWith(".")){
            source = source.substring(1);
            if(source.startsWith(FreemarkerConstant.FREEMARKER_DYNAMIC_COMPILE_PREFIX)){
                return source;
            }
          return  FreemarkerConstant.FREEMARKER_DYNAMIC_COMPILE_PREFIX + source.substring(1);
        }else {
            if(source.startsWith(FreemarkerConstant.FREEMARKER_DYNAMIC_COMPILE_PREFIX)){
                return source;
            }
            return   FreemarkerConstant.FREEMARKER_DYNAMIC_COMPILE_PREFIX + source;
        }
    }

    /****
     * 收集方法名
     * @param methods 方法名集合
     * @return
     */
    public static List<String> getMethodNames(List<String> methods){
        if (!CheckValueUtil.notBlank(methods)) {
            return methods;
        }

        List<String> list = new ArrayList<>();
        for (String method : methods) {
            if (method.contains(".")){
                list.add(method.substring(method.lastIndexOf(".")+1));
            }
        }
        return list;
    }

    public static String createJavaCode(Map<DynamicCompileEnum, Template> map,String className,
                                        String packagePath,List<String> methodNames,String baseTag,String checkUtil){
        if (!CheckValueUtil.notBlank(map)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Map<DynamicCompileEnum, String> javaTemplate = new HashMap<>();
        for (DynamicCompileEnum dynamicCompileEnum : map.keySet()) {
            switch (Objects.requireNonNull(dynamicCompileEnum)){
                case PACKAGE:
                    Template template = map.get(PACKAGE);
                    String packageResult = FreemarkerSupport.preExecuteJava(template);
                    javaTemplate.put(PACKAGE,String.format(packageResult,packagePath));
                    break;
                case IMPORT_BASE_TAGE:
                    javaTemplate.put(IMPORT_BASE_TAGE,String.format(FreemarkerSupport.preExecuteJava(map.get(IMPORT_BASE_TAGE)),baseTag));
                    break;
                case IMPORT_CHECK_UTIL:
                    javaTemplate.put(IMPORT_CHECK_UTIL,String.format(FreemarkerSupport.preExecuteJava(map.get(IMPORT_CHECK_UTIL)),checkUtil));
                    break;
                case  IMPORT_COMMON:
                    javaTemplate.put(IMPORT_COMMON, FreemarkerSupport.preExecuteJava(map.get(IMPORT_COMMON)));
                    break;
                case CLASS_NAME:
                    javaTemplate.put(CLASS_NAME,String.format(FreemarkerSupport.preExecuteJava(map.get(CLASS_NAME)),className,className,className));
                    break;
                case CLASS_METHOD:
                    if (CheckValueUtil.notBlank(methodNames)) {
                        StringBuilder methodSb = new StringBuilder();
                        for (String methodName : methodNames) {
                            methodSb.append(String.format(FreemarkerSupport.preExecuteJava(map.get(CLASS_METHOD)),methodName));
                        }
                        javaTemplate.put(CLASS_METHOD,methodSb.toString());
                    }
                    break;
                case  CLASS_END:
                    javaTemplate.put(CLASS_END, FreemarkerSupport.preExecuteJava(map.get(CLASS_END)));
                    break;
                default:
                    break;
            }
        }
        // 按顺序拼接
        sb.append(javaTemplate.get(PACKAGE))
            .append(javaTemplate.get(IMPORT_BASE_TAGE))
              .append(javaTemplate.get(IMPORT_CHECK_UTIL))
                .append(javaTemplate.get(IMPORT_COMMON))
                  .append(javaTemplate.get(CLASS_NAME))
                   .append(CheckValueUtil.notBlank(javaTemplate.get(CLASS_METHOD))?javaTemplate.get(CLASS_METHOD):"")
                     .append(javaTemplate.get(CLASS_END));

        return sb.toString();
    }

}
