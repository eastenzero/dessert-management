package com.example.prjbackend.common.core.page;
import lombok.Data;
@Data
public class PageDomain {
    private Integer pageNum; // 起始索引
    private Integer pageSize;//每页显示数量
    private String orderByColumn; // 排序列
    private String isAsc = "asc"; //排序方式 "desc" 或者 "asc"
    public String getOrderBy() {
        if (orderByColumn==null || orderByColumn.trim().equals("")) {
            return "";
        }
        return orderByColumn + " " + isAsc;
    }
    public void setIsAsc(String isAsc) {
        this.isAsc = "ascending".equals(isAsc)?"asc":"desc";
    }
}
