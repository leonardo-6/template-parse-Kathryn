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
public enum SqlHandleEnum {
    // sql执行的方式
    PARSE_SQL(1,"ParseSql","解析sql语句"),
    EXECUTE_SQL(2,"ExecuteSQl","执行sql语句");



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
        SqlHandleEnum[] values = values();
        for(SqlHandleEnum nameEnum : values){
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
    public static SqlHandleEnum getById(String id) {
        for (SqlHandleEnum ele : values()) {
            if (ele.getId().equals(id)) {
                return ele;
            }
        }
        return null;
    }
}
