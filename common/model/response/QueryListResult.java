package com.brothermiles.studyon.common.model.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class QueryListResult<T> {
    //data list
    private List<T> list;
    //data count
    private long total;
}
