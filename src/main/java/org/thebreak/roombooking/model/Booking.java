package org.thebreak.roombooking.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;


@Document(value = "booking")
@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Booking extends BaseEntity {

    @Field("user")
    @Schema(example = "should be with detail user info, only use userId for now")
    private String userId;

    @Field("room")
    @Schema(example = "booked room detail info")
    private Room room;

    @Field("total_hours")
    @Schema(example = "2 (in hours)")
    private long totalHours;

    @Field("booked_time")
    @Schema(example = "example")
    private List<BookingTimeRange> bookedTime;

    @Field("status")
    @Schema(example = "use enum for 1 booked, 2 paid, 3 attained, 4 reviewed, 5 refund, 6 overdue")
    private String status;

    @Field("paid_amount")
    @Schema(example = "19999")
    private long paidAmount;

    @Field("booked_at")
    private LocalDateTime bookedAt;
}
