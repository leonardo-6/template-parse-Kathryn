package com.lsl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 数据库类型 枚举类
 * describe 枚举 工具
 * @author LuShuL 12304
 * @date 2022/5/3 1:08
 * @version V1.0
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum DynamicCompileEnum {
    // 枚举对象 --各个数据库类型
    PACKAGE(0,"package"),
    IMPORT_BASE_TAGE(1,"importBaseTage"),
    IMPORT_CHECK_UTIL(2,"importCheckUtil"),
    IMPORT_COMMON(3,"importCommon"),
    CLASS_NAME(4,"className"),
    CLASS_METHOD(5,"classMethod"),
    CLASS_END(6,"classEnd");



    /***
     * 数据源类型id
     */
    private  Integer id;
    /***
     * 数据源类型名称
     */
    private String type;

    public static String getNameById(String id){
        DynamicCompileEnum[] values = values();
        for(DynamicCompileEnum nameEnum : values){
            if(nameEnum.getId().equals(id)){
                return nameEnum.name();
            }
        }
        return null ;
    }

    /***
     * 防止不让用switch case 常量问题
     * @param id 数据库类型id
     * @return 枚举类
     */
    public static DynamicCompileEnum getById(String id) {
        for (DynamicCompileEnum ele : values()) {
            if (ele.getId().equals(id)) {
                return ele;
            }
        }
        return null;
    }

    public static String[] getAllTypes(){
        String[] resultArray = new String[DynamicCompileEnum.values().length];
        for (int i = 0; i < DynamicCompileEnum.values().length; i++) {
            resultArray[i] = DynamicCompileEnum.values()[i].type;
        }
        return resultArray;
    }

    public static DynamicCompileEnum getByType(String type) {
        for (DynamicCompileEnum ele : values()) {
            if (ele.getType().equals(type)) {
                return ele;
            }
        }
        return null;
    }



}
