package org.thebreak.roombooking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.List;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class RoomVO {
    @Id
    private String id;
    private String title;
    private String address;
    private Integer roomNumber;

}
