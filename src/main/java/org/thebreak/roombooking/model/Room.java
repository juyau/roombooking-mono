package org.thebreak.roombooking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;


@Document(value = "room")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Room {
    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("type")
    private String type;

    @Field("city")
    private String city;

    @Field("address")
    private String address;

    @Field("room_number")
    private int roomNumber;

    @Field("description")
    private String description;

    @Field("floor")
    private int floor;

    @Field("size")
    private int size;

    @Field("max_people")
    private int maxPeople;

    @Field("price")
    private int price;

    @Field("discount")
    private int discount;

    @Field("rating")
    private int rating;

    @Field("commentCount")
    private int commentCount;

    @Field("bookedTime")
    private List<LocalDateTime> bookedTime;

    @Field("images")
    private List<String> images;

    @Field("facilities")
    private List<String> facilities;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


}
