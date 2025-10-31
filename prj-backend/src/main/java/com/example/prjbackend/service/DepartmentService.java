package com.example.prjbackend.service;

import com.example.prjbackend.domain.Department;
import com.example.prjbackend.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;

    public int add(Department department) {
        return departmentMapper.insert(department);
    }

    public List<Department> list(Department department) {
        return departmentMapper.list(department);
    }

    public int edit(Department department) {
        return departmentMapper.edit(department);
    }

    public int delete(Long[] ids) {
        return departmentMapper.delete(ids);
    }
}
