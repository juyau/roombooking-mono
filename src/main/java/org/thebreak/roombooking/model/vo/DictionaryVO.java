package org.thebreak.roombooking.model.vo;

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



@ToString
@NoArgsConstructor
@Data
@AllArgsConstructor
public class DictionaryVO {
    @Id
    private String id;
    private String name;
    private List<String> values;

}
