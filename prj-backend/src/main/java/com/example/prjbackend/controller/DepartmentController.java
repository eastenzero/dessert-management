package com.example.prjbackend.controller;

import com.example.prjbackend.common.core.controller.BaseController;
import com.example.prjbackend.common.core.domain.AjaxResult;
import com.example.prjbackend.common.core.page.TableDataInfo;
import com.example.prjbackend.domain.Department;
import com.example.prjbackend.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/department")
public class DepartmentController extends BaseController {
    @Autowired
    DepartmentService departmentService;

    @PostMapping
    public AjaxResult add(@RequestBody Department department) {
        return toAjax(departmentService.add(department));
    }

    @GetMapping
    public TableDataInfo list(Department department) {
        startPage();
        List<Department> list = departmentService.list(department);
        return getDataByPage(list);
    }

    @PutMapping
    public AjaxResult edit(@RequestBody Department department) {
        return toAjax(departmentService.edit(department));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(departmentService.delete(ids));
    }
}
