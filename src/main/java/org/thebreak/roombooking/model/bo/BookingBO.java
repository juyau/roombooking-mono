package org.thebreak.roombooking.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.thebreak.roombooking.model.BookingTimeRange;

import java.util.List;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class BookingBO {
    private String roomId;
    private List<BookingTimeRange> bookingTime;
}
