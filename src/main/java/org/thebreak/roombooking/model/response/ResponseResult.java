package org.thebreak.roombooking.model.response;

import lombok.Data;

@Data
public class ResponseResult<T>  {

    private Boolean success;
    private int code;
    private String message;
    private Long timestamp;
    private T data;

    public ResponseResult(){
        this.timestamp = System.currentTimeMillis();
    }

    public ResponseResult(CommonCode commonCode){
        this.success = commonCode.getSuccess();
        this.code = commonCode.getCode();
        this.message = commonCode.getMessage();
        this.timestamp = System.currentTimeMillis();
    }
    public static <T> ResponseResult<T> success(T data){
        ResponseResult<T> responseResult = new ResponseResult<>(CommonCode.SUCCESS);
//        responseResult.setSuccess(true);
//        responseResult.setCode(CommonCode.SUCCESS.getCode());
//        responseResult.setMessage(CommonCode.SUCCESS.getMessage());
        responseResult.setData(data);
        return responseResult;
    }

    public static <T> ResponseResult<T> success(){
        return new ResponseResult<>(CommonCode.SUCCESS);
    }

    public static <T> ResponseResult<T> fail(){
        ResponseResult<T> responseResult = new ResponseResult<>(CommonCode.FAILED);
//        responseResult.setSuccess(false);
//        responseResult.setCode(resultCode.getCode());
//        responseResult.setMessage(resultCode.getMessage());
        return responseResult;
    }

    public static <T> ResponseResult<T> fail(String message){
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setSuccess(false);
        responseResult.setCode(CommonCode.SYSTEM_ERROR.getCode());
        responseResult.setMessage(message);
        return responseResult;
    }
}
