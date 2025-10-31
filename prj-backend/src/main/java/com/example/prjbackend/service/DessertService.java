package com.example.prjbackend.service;

import com.example.prjbackend.domain.Dessert;
import com.example.prjbackend.domain.DessertDetail;
import com.example.prjbackend.domain.DessertQueryParam;
import com.example.prjbackend.mapper.DessertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DessertService {
    @Autowired
    DessertMapper dessertMapper;

    public List<DessertDetail> list(DessertQueryParam param) {
        return dessertMapper.list(param);
    }

    public int add(Dessert dessert) {
        return dessertMapper.add(dessert);
    }

    public int edit(Dessert dessert) {
        return dessertMapper.edit(dessert);
    }

    public int delete(Long[] ids) {
        return dessertMapper.delete(ids);
    }
}
