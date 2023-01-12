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
public enum WatcherEnum {

    // 类别 分类
    COMPILE_RULES(1,"compile","rules","观察 编译的规则"),
    COMPILE_EXECUTE(2,"compile","execute","观察 编译的执行"),
    SQL_EXECUTE(3,"sql","execute","观察 sql的执行");

    /***
     * 观察者分类的id
     */
    private  Integer id;
    /***
     * 观察者大分类的名称
     */
    private String type;

    /***
     * 观察者具体名称
     */
    private String value;

    /***
     * 描述
     */
    private String description;

    public static String getNameById(String id){
        WatcherEnum[] values = values();
        for(WatcherEnum nameEnum : values){
            if(nameEnum.getId().equals(id)){
                return nameEnum.name();
            }
        }
        return null ;
    }

    /***
     * 防止不让用switch case 常量问题
     * @param value 脱敏值
     * @return 枚举类
     */
    public static WatcherEnum getByValue(String value) {
        for (WatcherEnum ele : values()) {
            if (ele.getValue().equals(value)) {
                return ele;
            }
        }
        return null;
    }

    public static WatcherEnum getById(Integer id){
        WatcherEnum[] values = values();
        for(WatcherEnum nameEnum : values){
            if(nameEnum.getId().equals(id)){
                return nameEnum;
            }
        }
        return null ;
    }
}
