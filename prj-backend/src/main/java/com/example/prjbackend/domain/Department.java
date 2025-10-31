package com.example.prjbackend.domain;

import lombok.Data;

@Data
public class Department {
    Long id;
    String name;
    String leader;
    String phone;
    String descp;
}
