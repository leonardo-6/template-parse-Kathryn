package com.lsl.utils.check;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.lsl.common.constant.ComponentConstant.COMPARE_SIZE;


/**
 * 值校验的工具类 --支持多个及数组形式
 * describe 常用类型的值校验 工具类
 * @author LuShuL 12304
 * @date 2022/11/3 1:08
 * @version V1.0
 */
public class CheckValueUtil {

    /**
     * 0. 校验数据类型的值 是否为空
     * @param objs 对象
     * @return true or false
     */
    public static boolean notBlank(Object... objs){
        boolean result = false;

        List<Boolean> resultList = null;

        if(null == objs || objs.length < COMPARE_SIZE){
            return false;
        }

        if(objs.length > COMPARE_SIZE){
            resultList = new ArrayList<>();
        }

        for (Object obj : objs) {
            // 1. 校验这个值受否为空
            if (null != obj) {
                // 2. 校验这个值是否为 字符串
                if (obj.getClass() == String.class) {
                    result = StringUtils.isNoneBlank(obj.toString());
                    if(objs.length > COMPARE_SIZE ){ resultList.add(result);}
                // 3. 校验这个值是否为 List集合
                } else if (obj instanceof List) {
                    result = ((List<?>) obj).size() >= COMPARE_SIZE;
                    if(objs.length > COMPARE_SIZE ){ resultList.add(result);}
                // 4. 校验这个值是否为 Map集合
                } else if (obj instanceof Map) {
                    result = !(((Map<?, ?>) obj).isEmpty());
                    if(objs.length > COMPARE_SIZE ){ resultList.add(result);}
                } else if (obj instanceof Set) {
                    result = !((Set<?>) obj).isEmpty();
                    if(objs.length > COMPARE_SIZE ){ resultList.add(result);}
                } else {
                    result = true;
                }
            }else{
                return false;
            }
        }

        // 当集合存在并不满足的情况下，直接返回false
        if(resultList != null && resultList.size() > COMPARE_SIZE){
            for (Boolean CheckResult : resultList) {
                if(!CheckResult){
                    return false;
                }
            }
        }

        return result;
    }

    /**
     * 0. 校验数据类型的值 是否为空
     * @param objs 对象
     * @return true or false
     */
    public static boolean notBlankOne(Object... objs){
        boolean result = false;

        List<Boolean> resultList = null;

        if(null == objs || objs.length < COMPARE_SIZE){
            return false;
        }

        if(objs.length > COMPARE_SIZE){
            resultList = new ArrayList<>();
        }

        for (Object obj : objs) {
            // 1. 校验这个值受否为空
            if (null != obj) {
                // 2. 校验这个值是否为 字符串
                if (obj.getClass() == String.class) {
                    result = StringUtils.isNoneBlank(obj.toString());
                    if(objs.length > COMPARE_SIZE ){ resultList.add(result);}
                    // 3. 校验这个值是否为 List集合
                } else if (obj instanceof List) {
                    result = ((List<?>) obj).size() >= COMPARE_SIZE;
                    if(objs.length > COMPARE_SIZE ){ resultList.add(result);}
                    // 4. 校验这个值是否为 Map集合
                } else if (obj instanceof Map) {
                    result = !(((Map<?, ?>) obj).isEmpty());
                    if(objs.length > COMPARE_SIZE ){ resultList.add(result);}
                } else if (obj instanceof Set) {
                    result = !((Set<?>) obj).isEmpty();
                    if(objs.length > COMPARE_SIZE ){ resultList.add(result);}
                } else {
                    result = true;
                }
            }else{
                return false;
            }
        }

        // 当集合存在并不满足的情况下，直接返回false
        // 只要有一个成立就是true
        if(resultList != null && resultList.size() > COMPARE_SIZE){
            for (Boolean CheckResult : resultList) {
                if(CheckResult){
                    return true;
                }
            }
        }

        return result;
    }

    /**
     * 1. 传入的数组 里面是否有空的,一个都不能为空
     * @param objs 对象
     * @return true or false
     */
    public static boolean checkNotBlanks(Object[] objs){
        boolean result = false;

        if(null == objs && objs.length < COMPARE_SIZE){
            return false;
        }

        for (Object obj : objs) {
            result =notBlank(obj);
            if(!result){
                return false;
            }
        }

        return result;
    }

}
