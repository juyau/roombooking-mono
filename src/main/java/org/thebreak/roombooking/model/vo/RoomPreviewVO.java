package org.thebreak.roombooking.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;


@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class RoomPreviewVO {

    private String id;
    private String title;
    private String address;
    private Integer roomNumber;
    private String type;
    private String city;
    private String description;
    private String availableType;
    private int floor;
    private int size;
    private int maxPeople;
    private long price;
    private int discount;
    private int rating;
    private int commentCount;
    private List<String> images;
    private List<String> facilities;

}
