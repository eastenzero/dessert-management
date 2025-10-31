package com.example.prjbackend.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Dessert { //CUD
    Long id;
    String name;
    String descp;
    String photoUrl;
    Double price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date releaseDate;
    Long catId;
}
