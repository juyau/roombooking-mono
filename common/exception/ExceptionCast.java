package com.brothermiles.studyon.common.exception;

import com.brothermiles.studyon.common.model.response.ResultCode;

public class ExceptionCast {
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    }
}
