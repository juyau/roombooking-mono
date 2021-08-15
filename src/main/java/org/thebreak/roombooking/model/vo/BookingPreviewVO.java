package org.thebreak.roombooking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.thebreak.roombooking.model.BookingTimeRange;

import java.time.LocalDateTime;
import java.util.List;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class BookingPreviewVO {
    private String id;
    private String roomTitle;
    private String roomCity;
    private List<BookingTimeRange> bookedTime;
    private String status;
    private long totalHours;
    private long paidAmount;
    private LocalDateTime bookedAt;
}
