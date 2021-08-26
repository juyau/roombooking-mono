package org.thebreak.roombooking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.thebreak.roombooking.model.BookingContact;
import org.thebreak.roombooking.model.BookingTimeRange;

import java.time.LocalDateTime;
import java.util.List;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class BookingPreviewVO {
    private String id;
    private BookingContact contact;
    private String remark;
    private String roomTitle;
    private String roomCity;
    private List<BookingTimeRange> bookedTime;
    private String status;
    private long totalHours;
    private long totalAmount;
    private long paidAmount;
    private LocalDateTime bookedAt;
}
