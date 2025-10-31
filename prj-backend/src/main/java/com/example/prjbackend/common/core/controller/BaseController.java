package com.example.prjbackend.common.core.controller;

import com.github.pagehelper.PageHelper; //PageHelper分页插件
import com.github.pagehelper.PageInfo;   //PageHelper分页插件
import com.example.prjbackend.common.core.page.PageDomain;
import com.example.prjbackend.common.core.page.TableDataInfo;
import com.example.prjbackend.common.core.page.PageParamUtil;

import com.example.prjbackend.common.core.domain.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * web层通用数据处理
 *
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 初始化分页对象，设置分页参数
    protected void startPage()
    {
        //从前端获取分页参数
        PageDomain pageDomain = PageParamUtil.createPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = pageDomain.getOrderBy();
        //初始化实现分页的对象
        PageHelper.startPage(pageNum, pageSize, orderBy);
    }

    //返回分页数据
    protected TableDataInfo getDataByPage(List<?> list)
    {
        TableDataInfo tableData = new TableDataInfo();
        tableData.setCode(200);
        tableData.setMsg("查询成功");
        tableData.setRows(list);
        tableData.setTotal(new PageInfo(list).getTotal());
        return tableData;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)    {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }
}
