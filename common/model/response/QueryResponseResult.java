package com.brothermiles.studyon.common.model.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryResponseResult<T> extends ResponseResult {

    private T data;

    public QueryResponseResult(ResultCode resultCode,T data){
        super(resultCode);
       this.data = data;
    }

}
