package org.thebreak.roombooking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.thebreak.roombooking.model.BookingTimeRange;
import org.thebreak.roombooking.model.Room;

import java.time.LocalDateTime;
import java.util.List;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class BookingVO{

    private String id;
    private String userId;
    private Room room;
    private long totalHours;
    private List<BookingTimeRange> bookedTime;
    private String status;
    private long paidAmount;
    private LocalDateTime bookedAt;
}
