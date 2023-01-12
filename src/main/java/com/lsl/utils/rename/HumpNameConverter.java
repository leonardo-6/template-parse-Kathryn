package com.lsl.utils.rename;

import com.google.common.base.CaseFormat;
import com.lsl.utils.check.CheckValueUtil;

import java.util.*;

/**
 * 将 指定的值转换为驼峰命名规范
 * describe 驼峰命名 工具类
 * @author LuShuL 12304
 * @date 2022/5/3 1:08
 * @version V1.0
 */
public class HumpNameConverter {

    /***
     * 1. 转换 以map组成的 list集合 将key值转换为 驼峰命名
     * @param list 需转化的集合
     * @return List 转化完成的
     */
    public static List convertListMapKey (List<Map<String, Object>> list){
            // 1. 判断 list 集合是否含有元素
            if(!CheckValueUtil.notBlank(list)){
                return  list;
            }

            List newList = new ArrayList<>();

            // 2. 遍历list 集合获取当前的map
            for (Map map : list) {
                newList.add(traverseMap(map));
            }

            // 3. 返回转换后的结果
           return newList;
    }

    /**
     * 2. 遍历 map 转换key值 但是结果key为String 类型
     * @param map 单个map集合
     * @return key值转驼峰
     */
    public static Map traverseMap(Map map){
        // 1. 判断map是否含有元素
        if(!CheckValueUtil.notBlank(map)){
            return map;
        }

        Map<String, Object> newMap = new HashMap<>();

        // 2. 获取 map集合的 key值
        Iterator<Object> iter = map.keySet().iterator();

        // 3. 根据 map的 key 值获取 value值
        while(iter.hasNext()){
            Object key = iter.next();
            // 获取value值
            Object value = map.get(key);
            // 根据下划线转成驼峰
            String newKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, key.toString());

            newMap.put(newKey,value);
        }
        return newMap;
    }

    /***
     * 3.1. 转换 以map组成的 list集合 将key值转换为 传入的名字
     * @param list 需转化的集合
     * @param nameMap 需要修改的名字 - （原名 - 新名）组合
     * @return List 转化完成的
     */
    public static List convertListMapKey (List<Map<String, Object>> list,Map<String,String> nameMap){
        // 1. 判断 list 集合是否含有元素
        if(!CheckValueUtil.notBlank(list)){
            return  list;
        }

        List newList = new ArrayList<>();

        // 2. 遍历list 集合获取当前的map
        for (Map map : list) {
            newList.add(traverseMap(map,nameMap));
        }

        // 3. 返回转换后的结果
        return newList;
    }


    /**
     * 3.2. 遍历 map 转换key值 但是结果key为String 类型
     * @param map 单个map集合
     * @return key值转驼峰
     */
    public static Map traverseMap(Map map,Map<String,String> ruleMap){
        // 1. 判断map是否含有元素
        if(!CheckValueUtil.notBlank(map)){
            return map;
        }

        Map<String, Object> newMap = new HashMap<>();

        // 2. 获取 map集合的 key值
        Iterator<Object> iter = map.keySet().iterator();

        // 3. 根据 map的 key 值获取 value值
        while(iter.hasNext()){
            Object key = iter.next();

            // 3.1. 遍历规则修改的map
            Iterator iterator = ruleMap.keySet().iterator();
            while(iterator.hasNext()){
                Object ruleKey = iterator.next();
                if(key.equals(ruleKey.toString())){
                    // 获取value值
                    Object value = map.get(key);

                    newMap.put(ruleMap.get(ruleKey),value);
                }
            }
        }
        return newMap;
    }


    /**
     * 3.根据下划线 转驼峰命名
     * @param line
     * @param firstIsUpperCase
     * @return
     */
    /*private static String underlineToCamel(String line, boolean ... firstIsUpperCase) {
        String str = "";

        if(StringUtils.isBlank(line)){
            return str;
        } else {
            line = line.toLowerCase(Locale.ROOT);
            StringBuilder sb = new StringBuilder();
            String [] strArr;
            // 不包含下划线，且第二个参数是空的
            if(!line.contains("_") && firstIsUpperCase.length == 0){
                sb.append(line.substring(0, 1).toLowerCase()).append(line.substring(1));
                str = sb.toString();
            } else if (!line.contains("_") && firstIsUpperCase.length != 0){
                if (!firstIsUpperCase[0]) {
                    sb.append(line.substring(0, 1).toLowerCase()).append(line.substring(1));
                    str = sb.toString();
                } else {
                    sb.append(line.substring(0, 1).toUpperCase()).append(line.substring(1));
                    str = sb.toString();
                }
            } else if (line.contains("_") && firstIsUpperCase.length == 0) {
                strArr = line.split("_");
                for (String s : strArr) {
                    sb.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
                }
                str = sb.toString();
                str = str.substring(0, 1).toLowerCase() + str.substring(1);
            } else if (line.contains("_") && firstIsUpperCase.length != 0) {
                strArr = line.split("_");
                for (String s : strArr) {
                    sb.append(s.substring(0, 1).toUpperCase()).append(s.substring(1));
                }
                if (!firstIsUpperCase[0]) {
                    str = sb.toString();
                    str = str.substring(0, 1).toLowerCase() + str.substring(1);
                } else {
                    str = sb.toString();
                }
            }
        }
        return str;
    }*/
}
