package com.example.prjbackend.domain;

import lombok.Data;

@Data
public class DessertQueryParam {
    Long catId;
    String name;
    String descp;
    Double price1;
    Double price2;

    public void setName(String name) {
        this.name = name;
        if("".equals(this.name)){
            this.name=null;
        }
    }

    public void setDescp(String descp) {
        this.descp = descp;
        if("".equals(this.descp)){
            this.descp=null;
        }
    }
}
