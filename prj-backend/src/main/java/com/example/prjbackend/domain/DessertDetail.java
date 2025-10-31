package com.example.prjbackend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class DessertDetail { //R
    Long id;
    String name;
    String descp;
    String photoUrl;
    Double price;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "CMT+8") //json回前端
    Date releaseDate;
    Long catId;
    String categoryName;
}
