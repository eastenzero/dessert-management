package com.example.prjbackend.domain;

import lombok.Data;

@Data
public class User {
    Long id;
    String username;
    String password;
    Integer active;
}
