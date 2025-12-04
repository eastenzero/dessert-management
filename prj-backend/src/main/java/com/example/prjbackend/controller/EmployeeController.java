package com.example.prjbackend.controller;

import com.example.prjbackend.common.core.controller.BaseController;
import com.example.prjbackend.common.core.domain.AjaxResult;
import com.example.prjbackend.common.core.page.TableDataInfo;
import com.example.prjbackend.domain.Employee;
import com.example.prjbackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/employee")
public class EmployeeController extends BaseController {

    @Autowired
    EmployeeService employeeService;

    @PostMapping
    public AjaxResult add(@RequestBody Employee employee) {
        return toAjax(employeeService.add(employee));
    }

    @GetMapping
    public TableDataInfo list(Employee employee) {
        startPage();
        List<Employee> list = employeeService.list(employee);
        return getDataByPage(list);
    }

    @PutMapping
    public AjaxResult edit(@RequestBody Employee employee) {
        return toAjax(employeeService.edit(employee));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(employeeService.delete(ids));
    }
}
