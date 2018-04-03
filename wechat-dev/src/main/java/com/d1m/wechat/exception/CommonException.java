package com.d1m.wechat.exception;


public class CommonException extends RuntimeException {

    private String code;

    public CommonException(String code){
        this.code = code;
    }

    public CommonException(String code, String message){
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
