package org.thebreak.roombooking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.thebreak.roombooking.model.BookingTimeRange;
import org.thebreak.roombooking.model.Room;

import java.util.List;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class RoomWithBookedTimeVO extends Room {
    private List<BookingTimeRange> bookedTime;

}
