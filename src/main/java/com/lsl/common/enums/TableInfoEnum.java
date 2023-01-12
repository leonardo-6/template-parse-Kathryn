package com.lsl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 表信息的枚举
 * describe 表相关信息
 * @author LuShuL 12304
 * @version V1.0
 * @date 2022/5/11 19:52
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public enum TableInfoEnum {

    // 查询出来直接获取的信息 -- 针对某些情况的命名问题
    TABLE_NAME_ORIGIN("table_name"),
    DB_NAME_ORIGIN("database_name"),
    OCCUPIED_SPACE_ORIGIN("table_size"),
    // 表信息
    TABLE_IDS("TABLE_IDS"),
    TABLE_NAMES("TABLE_NAMES"),
    TABLE_NUM("TABLE_NUM"),
    DB_NAME("DB_NAME"),
    TABLE_NAME("TABLE_NAME"),
    TOTAL_DATA_TIME("TOTAL_DATA_TIME"),
    QUERY_COUNT("QUERY_COUNT"),
    OCCUPIED_SPACE("OCCUPIED_SPACE");

    /***
     * 表的信息
     */
    private String info;
}
