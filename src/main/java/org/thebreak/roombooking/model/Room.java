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

    @Field("title")
    @NotEmpty
    @Size(min = 3, max = 300, message = "title must be between 3 to 300 characters.")
    private String title;

    @Field("address")
    @NotEmpty
    private String address;

    @Field("room_number")
    @NotEmpty
    private Integer roomNumber;

//    @Field("type")
//    @NotEmpty
//    private String type;

//    @Field("city")
//    private String city;


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
