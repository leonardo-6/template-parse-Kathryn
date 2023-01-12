package com.lsl.exception;

/**
 * 异常处理的封装类
 * @Description: 异常
 *  @Author: lsl
 * @Data:2022-12-12 17:25
 * @Projectname: Kathryn
 * @Filename: CodedException
 */
public class CodedException extends Exception implements Coded{
    protected static final long serialVersionUID = -8481811634176212223L;
    protected int code = 500;

    public CodedException() {
    }

    public CodedException(int code) {
        this.code = code;
    }

    public CodedException(String msg) {
        super(msg);
    }

    public CodedException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public CodedException(Throwable e) {
        super(e);
    }

    public CodedException(Throwable e, int code) {
        super(e);
        this.code = code;
    }

    public CodedException(Throwable e, String msg) {
        super(msg, e);
    }

    public CodedException(Throwable e, int code, String msg) {
        super(msg, e);
        this.code = code;
    }

    public CodedException(int code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public void throwThis() throws Exception {
        throw this;
    }
}
