package com.example.prjbackend.service;

import com.example.prjbackend.domain.Employee;
import com.example.prjbackend.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    public int add(Employee employee) {
        return employeeMapper.insert(employee);
    }

    public List<Employee> list(Employee employee) {
        return employeeMapper.list(employee);
    }

    public int edit(Employee employee) {
        return employeeMapper.edit(employee);
    }

    public int delete(Long[] ids) {
        return employeeMapper.delete(ids);
    }
}
