package com.brothermiles.studyon.common.exception;

import com.brothermiles.studyon.common.model.response.CommonCode;
import com.brothermiles.studyon.common.model.response.ResponseResult;
import com.brothermiles.studyon.common.model.response.ResultCode;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionCatch {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    // use EXCEPTIONS to save the map of exception type and resultCode;
    // ImmutableMap cannot be changed after created, and it's thread safe.
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;

    // use builder to create exception
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    // all other exceptions handler
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){
        LOGGER.error("catch exception : {}\r\nexception: ", e.getMessage(), e);
        if(EXCEPTIONS == null)
            EXCEPTIONS = builder.build();
        final ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        final ResponseResult responseResult;
        if(resultCode != null){
            responseResult = new ResponseResult(resultCode);
        } else {
            responseResult = new ResponseResult(CommonCode.SERVER_ERROR);
        }
        return responseResult;
    }

    // Custom exception handler
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e){
        LOGGER.error("catch exception : {}\r\nexception: ", e.getMessage(), e);
        ResultCode resultCode = e.getResultCode();
        return new ResponseResult(resultCode);
    }

    static {
        // put in the usual exceptions
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }
}
