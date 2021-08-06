package org.thebreak.roombooking.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PageResult<T> {
    private long totalRows;
    private int totalPages;
    private int pageSize;       // request page size
    private int contentSize;    // actual returned content size
    private int currentPage;
    private T content;

    public PageResult() {

    }
}
