package com.lsl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Description: TODO
 * @Author: lushul 12314
 * @Data:2022-11-14 19:05
 * @Projectname: ehrview
 * @Filename: CategoryEnum
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum CommonEnum {
    // 查询 脱敏规则的方法
    TO_STRING(0,"toString","Object里面的方法"),
    METHOD(1,"method","方法的对象"),
    SQL(2,"sql","sql语句"),
    TYPE(3,"type","类型");



    /***
     * 分类的id
     */
    private  Integer id;
    /***
     * 分类的名称
     */
    private String value;

    private String description;

    public static String getNameById(String id){
        CommonEnum[] values = values();
        for(CommonEnum nameEnum : values){
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
    public static CommonEnum getById(String id) {
        for (CommonEnum ele : values()) {
            if (ele.getId().equals(id)) {
                return ele;
            }
        }
        return null;
    }
}
