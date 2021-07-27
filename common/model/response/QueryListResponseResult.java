package com.brothermiles.studyon.common.model.response;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QueryListResponseResult<T> extends ResponseResult {

    QueryListResult<T> queryListResult;

    public QueryListResponseResult(ResultCode resultCode, QueryListResult<T> queryListResult){
        super(resultCode);
       this.queryListResult = queryListResult;
    }



}
