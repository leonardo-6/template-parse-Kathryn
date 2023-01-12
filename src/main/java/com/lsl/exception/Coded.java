package com.lsl.exception;

/**
 * 异常信息
 * @Description: 异常处理用
 * @Author: lsl
 * @Data:2022-12-12 17:24
 * @Projectname: Kathryn
 * @Filename: Coded
 */
public interface Coded {
    int getCode();

    String getMessage();

    Throwable getCause();

    StackTraceElement[] getStackTrace();

    void throwThis() throws Exception;
}
