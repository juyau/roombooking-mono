package com.brothermiles.studyon.common.model.response;

import lombok.ToString;


@ToString
public enum CommonCode implements ResultCode{

    SUCCESS(true,10000,"Success!"),
    FAIL(false,11111,"Failed!"),
    UNAUTHENTICATED(false,10001,"Access denied. This operation requires authentication."),
    UNAUTHORISE(false,10002,"Access denied. This operation requires authorization."),
    INVALID_PARAM(false, 10003,"Request param invalid."),

    ITEM_NOT_EXIST(false, 10004,"This item not exist."),

    SERVER_ERROR(false,99999,"System busy, please try again later.");


//    private static ImmutableMap<Integer, CommonCode> codes ;

    boolean success;

    int code;

    String message;
    private CommonCode(boolean success,int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }

    @Override
    public boolean success() {
        return success;
    }
    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }


}
