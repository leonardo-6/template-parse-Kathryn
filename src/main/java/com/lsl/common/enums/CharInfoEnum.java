package com.lsl.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 字符串相关的 信息
 * describe 字符串有关的枚举信息
 * @author LuShuL 12304
 * @version V1.0
 * @date 2022/5/12 11:10
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
public enum CharInfoEnum {

    // 字符信息
    APOSTROPHE("'"),
    COMMA(","),
    APOSTROPHE_COMMA("',"),
    SLASH("/"),
    COLON(":"),
    POINT("."),
    POWER("^"),
    BLANK(" "),
    EMPTY("");

    /***
     * 具体的字符信息
     */
    private String symbol;

}
