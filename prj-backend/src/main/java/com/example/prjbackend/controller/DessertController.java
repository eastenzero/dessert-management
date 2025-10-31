package com.example.prjbackend.controller;

import com.example.prjbackend.common.core.controller.BaseController;
import com.example.prjbackend.common.core.domain.AjaxResult;
import com.example.prjbackend.common.core.page.TableDataInfo;
import com.example.prjbackend.domain.Category;
import com.example.prjbackend.domain.Dessert;
import com.example.prjbackend.domain.DessertDetail;
import com.example.prjbackend.domain.DessertQueryParam;
import com.example.prjbackend.service.CategoryService;
import com.example.prjbackend.service.DessertService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/dessert")
public class DessertController extends BaseController {
    @Autowired
    DessertService dessertService;

    @GetMapping
    public TableDataInfo list(DessertQueryParam param){
        startPage();
        List<DessertDetail> list=dessertService.list(param);
        return getDataByPage(list);
    }

    @PostMapping("/upload")  // /dessert/upload
    public String uploadPhoto(MultipartFile file) throws IOException { // a.jpg
        String ext = "."+FilenameUtils.getExtension(file.getOriginalFilename());
        String pre=UUID.randomUUID().toString().replace("-","");
        String filePath="../photo/"+pre+ext; // 8312312409AD....292.jpg
        FileUtils.copyInputStreamToFile(file.getInputStream(),new File(filePath));
        return filePath.replace("..","");
    }
    @PostMapping
    public AjaxResult add(@RequestBody Dessert dessert){
        return toAjax(dessertService.add(dessert));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody Dessert dessert){
        return toAjax(dessertService.edit(dessert));
    }


    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids){
        return toAjax(dessertService.delete(ids));
    }

}
