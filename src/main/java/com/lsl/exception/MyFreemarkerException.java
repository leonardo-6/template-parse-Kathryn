package com.lsl.exception;

/**
 * 当前项目的异常处理
 * @Description: 对当前项目抛异常的处理机制
 * @Author: lsl
 * @Data:2022-12-12 17:26
 * @Projectname: Kathryn
 * @Filename: MyFreemarkerException
 */
public class MyFreemarkerException extends CodedException{
    public static final int INSTANCE_NOT_FOUND = 404;
    public static final int IO_ERROR = 409;
    public static final int PARSE_ERROR = 401;
    private static final long serialVersionUID = 7336681897520100911L;

    public MyFreemarkerException(int code) {
        super(code);
    }

    public MyFreemarkerException(String msg) {
        super(msg);
    }

    public MyFreemarkerException(int code, String msg) {
        super(code, msg);
    }

    public MyFreemarkerException(Throwable e) {
        super(e);
    }

    public MyFreemarkerException(Throwable e, int code) {
        super(e, code);
    }

    public MyFreemarkerException(Throwable e, String msg) {
        super(e, msg);
    }

    public MyFreemarkerException(Throwable e, int code, String msg) {
        super(e, code, msg);
    }

    public boolean isInstanceNotFound() {
        return this.code == 404;
    }
}
