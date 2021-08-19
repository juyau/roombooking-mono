package org.thebreak.roombooking.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "6118bac4b28c5e2f4d882c17")
    private String roomId;
    @Schema(example = "{\"start\":\"2021-08-22T22:00\",\n" +
            "        \"end\":\"2021-08-22T23:00\"}")
    private List<BookingTimeRange> bookingTime;
}
