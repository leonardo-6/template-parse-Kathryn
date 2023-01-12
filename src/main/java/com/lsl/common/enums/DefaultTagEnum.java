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
public enum DefaultTagEnum {
    // 枚举对象 --各个数据库类型
    SELECT(0,"Select"),
    CREATE(1,"Create"),
    UPDATE(2,"Update"),
    DELETE(3,"Delete"),
    JAVA(4,"Java");



    /***
     * 数据源类型id
     */
    private  Integer id;
    /***
     * 数据源类型名称
     */
    private String type;

    public static String getNameById(String id){
        DefaultTagEnum[] values = values();
        for(DefaultTagEnum nameEnum : values){
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
    public static DefaultTagEnum getById(String id) {
        for (DefaultTagEnum ele : values()) {
            if (ele.getId().equals(id)) {
                return ele;
            }
        }
        return null;
    }

    public static String[] getAllTypes(){
        String[] resultArray = new String[DefaultTagEnum.values().length];
        for (int i = 0; i < DefaultTagEnum.values().length; i++) {
            resultArray[i] = DefaultTagEnum.values()[i].type;
        }
        return resultArray;
    }

}
