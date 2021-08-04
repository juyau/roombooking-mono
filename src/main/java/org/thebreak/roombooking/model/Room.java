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
import org.thebreak.roombooking.common.base.BaseEntity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;


@Document(value = "room")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Room extends BaseEntity {

    @Field("name")
    @NotEmpty
    @Size(min = 3, max = 50, message = "name must be between 3 to 50 characters.")
    private String name;

    @Field("type")
    @NotEmpty
    private String type;

//    @Field("city")
//    private String city;
//
//    @Field("address")
//    private String address;
//
//    @Field("room_number")
//    private int roomNumber;

//    @Field("description")
//    private String description;
//
//    @Field("floor")
//    private int floor;
//
//    @Field("size")
//    private int size;
//
//    @Field("max_people")
//    private int maxPeople;
//
//    @Field("price")
//    private int price;
//
//    @Field("discount")
//    private int discount;
//
//    @Field("rating")
//    private int rating;
//
//    @Field("commentCount")
//    private int commentCount;
//
//    @Field("bookedTime")
//    private List<LocalDateTime> bookedTime;
//
//    @Field("images")
//    private List<String> images;
//
//    @Field("facilities")
//    private List<String> facilities;


}
