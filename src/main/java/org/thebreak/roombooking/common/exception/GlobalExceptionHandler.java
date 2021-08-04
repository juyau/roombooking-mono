package org.thebreak.roombooking.common.exception;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.thebreak.roombooking.model.response.CommonCode;
import org.thebreak.roombooking.model.response.ResponseResult;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
//    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
//    public ResponseResult httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
//        return new ResponseResult(405, e.getMessage());
//    }


    @ExceptionHandler(value = CustomException.class)
    public ResponseResult customExceptions(CustomException e){
        return new ResponseResult(e.getCommonCode());
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseResult allOtherExceptions(Exception e){

        // missing request body;
        if(e instanceof HttpMessageNotReadableException){
            return ResponseResult.fail("Required request body is missing.");
        }


        // param type not match, such as ObjectId length or type not match;
        if(e instanceof MethodArgumentTypeMismatchException){
            System.out.println("MethodArgumentTypeMismatchException");
            return ResponseResult.fail(CommonCode.INVALID_PARAM.getMessage());
        }

        // http method not supported; - - need to check
        if(e instanceof MethodArgumentNotValidException){
            System.out.println("MethodArgumentNotValidException");
            return ResponseResult.fail(e.getMessage());
        }

         // http method not supported;
        if(e instanceof HttpRequestMethodNotSupportedException){
            System.out.println("HttpRequestMethodNotSupportedException");
            return ResponseResult.fail(e.getMessage());
        }

        e.printStackTrace();

        return ResponseResult.fail();
    }

}
