package com.lsl.exception;

/**
 * 异常信息描述 基础信息接口类
 * describe 描述异常信息
 * @author LuShuL 12304
 * @version V1.0
 * @date 2022/5/9 16:30
 */
public interface ResultCodeInterface {

    /** 错误描述 */
    String getMsg();

    /** 错误码 */
    Integer getCode();
}
