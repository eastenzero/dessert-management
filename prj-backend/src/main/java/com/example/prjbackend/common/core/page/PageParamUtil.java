package com.example.prjbackend.common.core.page;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class PageParamUtil {
    public static final String PAGENUM = "pageNum"; // 起始索引
    public static final String PAGESIZE = "pageSize";//每页显示数量
    public static final String ORDERCOLUMN = "orderByColumn"; //排序列
    public static final String ISASC = "isAsc"; //排序方式 "desc" 或者 "asc"

    private static String getParameter(String name)   {
        ServletRequestAttributes attributes =(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest().getParameter(name);
    }
    //构建分页对象
    public static PageDomain createPageRequest()  {
        PageDomain pageDomain = new PageDomain();
        pageDomain.setPageNum(Integer.valueOf(getParameter(PAGENUM)==null?"1":getParameter(PAGENUM)));
        pageDomain.setPageSize(Integer.valueOf(getParameter(PAGESIZE)==null?"10":getParameter(PAGESIZE)));
        pageDomain.setOrderByColumn(getParameter(ORDERCOLUMN));
        pageDomain.setIsAsc(getParameter(ISASC));
        return pageDomain;
    }
}
