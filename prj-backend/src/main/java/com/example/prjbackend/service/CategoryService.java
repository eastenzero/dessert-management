package com.example.prjbackend.service;

import com.example.prjbackend.domain.Category;
import com.example.prjbackend.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;
    public int add(Category category) {
        return categoryMapper.insert(category);
    }

    public List<Category> list(Category category) {
        return categoryMapper.list(category);
    }

    public int edit(Category category) {
       return categoryMapper.edit(category);
    }


    public int delete(Long[] ids) {
        return categoryMapper.delete(ids);
    }
}
