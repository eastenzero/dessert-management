package com.example.prjbackend.controller;

import com.example.prjbackend.common.core.controller.BaseController;
import com.example.prjbackend.common.core.domain.AjaxResult;
import com.example.prjbackend.common.core.page.TableDataInfo;
import com.example.prjbackend.domain.Category;
import com.example.prjbackend.service.CategoryService;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController extends BaseController {
    @Autowired
    CategoryService categoryService;

    @PostMapping
    public AjaxResult add(@RequestBody Category category){
       return toAjax(categoryService.add(category));
    }

    @GetMapping
    public TableDataInfo list(Category category){
        startPage();
        List<Category> list=categoryService.list(category);
        return getDataByPage(list);
    }

    @PutMapping
    public AjaxResult edit(@RequestBody Category category){
       return toAjax(categoryService.edit(category));
    }

    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids){
        return toAjax(categoryService.delete(ids));
    }
}
