package com.brothermiles.studyon.common.exception;

import com.brothermiles.studyon.common.model.response.ResultCode;

public class CustomException extends RuntimeException{
    private final ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        super("Error Code: " + resultCode.code() + " Error Message: " + resultCode.message());
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
