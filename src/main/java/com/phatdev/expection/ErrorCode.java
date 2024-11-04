package com.phatdev.expection;

import lombok.Data;


public enum ErrorCode {
    AUTHENTICATED(1005,"Unauthorized"),
    USER_NOT_EXISTED(1004,"User NOT existed"),
    USER_EXISTED(1002,"User existed"),
    KEY_INVALID(1003,"Invalid message key"),
    PASSWORD_INVALID(1001,"Password must be at least 8 a character"),
    USERNAME_INVALID(1000,"User must be at least 3 a character"),
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized exception"),;
    ;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
