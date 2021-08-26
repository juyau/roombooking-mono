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
    @Schema(example = "userIdFromKeyCloak")
    private String userId;

    @Field("contact")
    @Schema(description = "contact person info")
    private BookingContact contact;

    @Field("remark")
    @Schema(example = "We need 2 extra white boards, thanks")
    private String remark;

    @Field("room")
    @Schema(example = "booked room detail info")
    private Room room;

    @Field("total_hours")
    @Schema(example = "2 (in hours)")
    private long totalHours = 0L;

    @Field("total_amount")
    @Schema(example = "1999")
    private long totalAmount = 0L;

    @Field("booked_time")
    @Schema(example = "example")
    private List<BookingTimeRange> bookedTime;

    @Field("status")
    @Schema(example = "use enum for 1 booked, 2 paid, 3 attained, 4 reviewed, 5 refund, 6 overdue")
    private String status;

    @Field("paid_amount")
    @Schema(example = "19999")
    private Long paidAmount = 0L;

    @Field("booked_at")
    private LocalDateTime bookedAt;
}
