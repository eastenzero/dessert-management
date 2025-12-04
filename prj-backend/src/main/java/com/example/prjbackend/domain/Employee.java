package com.example.prjbackend.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Employee {
    Long id;
    String name;
    String gender;
    Long deptId;
    String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date hireDate;
    Double salary;
    String descp;
}
